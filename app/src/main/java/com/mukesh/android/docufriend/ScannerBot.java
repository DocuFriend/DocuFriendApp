package com.mukesh.android.docufriend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.doo.snap.camera.AutoSnappingController;
import net.doo.snap.camera.CameraOpenCallback;
import net.doo.snap.camera.ContourDetectorFrameHandler;
import net.doo.snap.camera.PictureCallback;
import net.doo.snap.camera.ScanbotCameraView;
import net.doo.snap.lib.detector.ContourDetector;
import net.doo.snap.lib.detector.DetectionResult;
import net.doo.snap.ui.PolygonView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import io.scanbot.sdk.ui.camera.ShutterButton;


public class ScannerBot extends AppCompatActivity implements PictureCallback, ContourDetectorFrameHandler.ResultHandler{

    private TextView userGuidanceHint;
    private ShutterButton shutterButton;
    private boolean flashEnabled = false;
    private Button autoSnappingToggleButton;
    private boolean autoSnappingEnabled = true;
    private AutoSnappingController autoSnappingController;
    private ContourDetectorFrameHandler frameHandler;
    private PolygonView polygonView;
    private long lastUserGuidanceHintTs = 0L;

    ScanbotCameraView scanbotCameraView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_bot);
        getSupportActionBar().hide();
        scanbotCameraView = (ScanbotCameraView) findViewById(R.id.camera);
        scanbotCameraView.setCameraOpenCallback(new CameraOpenCallback() {
            @Override
            public void onCameraOpened() {
                scanbotCameraView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scanbotCameraView.setAutoFocusSound(false);
                        scanbotCameraView.continuousFocus();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            scanbotCameraView.setShutterSound(false);
                        }
                    }
                }, 700);
            }
        });

        frameHandler = ContourDetectorFrameHandler.attach(scanbotCameraView);
        polygonView = (PolygonView) findViewById(R.id.polygonView);
        frameHandler.addResultHandler(polygonView);
        autoSnappingController = AutoSnappingController.attach(scanbotCameraView,frameHandler);
        autoSnappingController.setSensitivity(0.8f);

        scanbotCameraView.addPictureCallback(this);

        userGuidanceHint = findViewById(R.id.userGuidanceHint);

        shutterButton = findViewById(R.id.shutterButton);
        shutterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanbotCameraView.takePicture(false);
            }
        });
        shutterButton.setVisibility(View.VISIBLE);

        findViewById(R.id.flashToggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashEnabled = !flashEnabled;
                scanbotCameraView.useFlash(flashEnabled);
            }
        });

        autoSnappingToggleButton = findViewById(R.id.autoSnappingToggle);
        autoSnappingToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoSnappingEnabled = !autoSnappingEnabled;
                setAutoSnapEnabled(autoSnappingEnabled);
            }
        });

        autoSnappingToggleButton.post(new Runnable() {
            @Override
            public void run() {
                setAutoSnapEnabled(autoSnappingEnabled);
            }
        });



    }




    private static void persistImage(Bitmap bitmap, String name) {
        String filename = name;
        File sd = Environment.getExternalStorageDirectory();
        File dest = new File(sd, filename);
        try {
            Log.d("creating ", "trying to make a image");
            FileOutputStream out = new FileOutputStream(dest);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.d("creating2 ", "made image check" + dest.getAbsolutePath().toString());

            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        scanbotCameraView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        scanbotCameraView.onPause();
    }

    @Override
    public boolean handleResult(final ContourDetectorFrameHandler.DetectedFrame detectedFrame) {
        // Here you are continuously notified about contour detection results.
        // For example, you can show a user guidance text depending on the current detection status.
        userGuidanceHint.post(new Runnable() {
            @Override
            public void run() {
                showUserGuidance(detectedFrame.detectionResult);
            }
        });

        return false; // typically you need to return false
    }

    private void showUserGuidance(final DetectionResult result) {
        if (!autoSnappingEnabled) {
            return;
        }

        if (System.currentTimeMillis() - lastUserGuidanceHintTs < 400) {
            return;
        }

        switch (result) {
            case OK:
                userGuidanceHint.setText("Don't move");
                userGuidanceHint.setVisibility(View.VISIBLE);
                break;
            case OK_BUT_TOO_SMALL:
                userGuidanceHint.setText("Move closer");
                userGuidanceHint.setVisibility(View.VISIBLE);
                break;
            case OK_BUT_BAD_ANGLES:
                userGuidanceHint.setText("Perspective");
                userGuidanceHint.setVisibility(View.VISIBLE);
                break;
            case ERROR_NOTHING_DETECTED:
                userGuidanceHint.setText("No Document");
                userGuidanceHint.setVisibility(View.VISIBLE);
                break;
            case ERROR_TOO_NOISY:
                userGuidanceHint.setText("Background too noisy");
                userGuidanceHint.setVisibility(View.VISIBLE);
                break;
            case OK_BUT_BAD_ASPECT_RATIO:
                userGuidanceHint.setText("Wrong aspect ratio.\n Rotate your device.");
                userGuidanceHint.setVisibility(View.VISIBLE);
                break;
            case ERROR_TOO_DARK:
                userGuidanceHint.setText("Poor light");
                userGuidanceHint.setVisibility(View.VISIBLE);
                break;
            default:
                userGuidanceHint.setVisibility(View.GONE);
                break;
        }

        lastUserGuidanceHintTs = System.currentTimeMillis();
    }

    @Override
    public void onPictureTaken(byte[] image, int imageOrientation) {
        Intent in1 = new Intent(this, TaggingImages.class);
        Bundle extras = new Bundle();
//        extras.putByteArray("image", image);
        extras.putInt("orientation", imageOrientation);
        in1.putExtra("bundle", extras);
        startActivity(in1);
    }

    private void setAutoSnapEnabled(boolean enabled) {
        autoSnappingController.setEnabled(enabled);
        frameHandler.setEnabled(enabled);
        polygonView.setVisibility(enabled ? View.VISIBLE : View.GONE);
        autoSnappingToggleButton.setText("Automatic " + (enabled ? "ON":"OFF"));
        if (enabled){
            shutterButton.showAutoButton();
        }
        else {
            shutterButton.showManualButton();
            userGuidanceHint.setVisibility(View.GONE);
        }
    }
}
