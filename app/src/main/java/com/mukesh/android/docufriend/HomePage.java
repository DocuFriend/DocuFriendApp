package com.mukesh.android.docufriend;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.scanbot.sdk.ScanbotSDK;
import io.scanbot.sdk.ScanbotSDKInitializer;

public class HomePage extends AppCompatActivity {

    Button cameraButton;

    private final String licenseKey =
            "Gibzvky/7mrq9OJI+UFbk3zlc5hMub" +
                    "fRoxHcqy+fXnSwxA67VNyq/qowSHFv" +
                    "plR3Vh0yLXwNJws6EYzg7y/uAK/bmz" +
                    "eCxcwW7UxVo+l8BAx5MsGgwPnqWi2d" +
                    "YpZ6yyd9bAkjEOT17lr0e7qcyvzHwK" +
                    "KH0vpTvT/HntJ7pGvFNl6IOtdE7yG2" +
                    "MwkFFfkrzLkU1GwukceGjhzwx2JBRJ" +
                    "TJdiRcNQt3nBmdGC6ByJPwCQgTao6H" +
                    "6YAOBXQI+BH0RYWoWg2ek5Z1A2aePt" +
                    "GLz41ZriHFYQ129xOA/AkaOMOA3RHP" +
                    "60eXOTdsw8Q8Gd2mJdVdETTp7fTQvn" +
                    "m36lWhVyiESw==\nU2NhbmJvdFNESw" +
                    "pjb20ubXVrZXNoLmFuZHJvaWQuZG9j" +
                    "dWZyaWVuZAoxNTM5OTkzNTk5CjU5MA" +
                    "oz\n";

    private int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new ScanbotSDKInitializer().license(getApplication(), licenseKey).initialize(getApplication());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        ScanbotSDK scanbotSDK = new ScanbotSDK(this);
        if(scanbotSDK.isLicenseValid()) {
            getGoingNormal();
        } else {
            Toast liscenceToast = new Toast(this);
            liscenceToast.setText("Liscence for ScanBot Expired");
            liscenceToast.show();
        }
    }

    private void getGoingNormal() {
        cameraButton = (Button) findViewById(R.id.camerabutton);
        cameraButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomePage.this, ScannerBot.class);
                        startActivity(intent);
                    }
                }
        );
    }

    private void checkPermission(String method) {
        if (method.equals("Camera")){
            if (ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomePage.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_OPEN_CAMERA);

            }
        } else if (method.equals("Storage")) {
            if (ContextCompat.checkSelfPermission(HomePage.this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(HomePage.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_OPEN_CAMERA);

            }
        }

    }



}
