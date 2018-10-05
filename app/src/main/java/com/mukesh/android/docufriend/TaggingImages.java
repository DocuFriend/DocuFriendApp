package com.mukesh.android.docufriend;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import net.doo.snap.lib.detector.ContourDetector;

public class TaggingImages extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging_images);



        Bundle bundle = getIntent().getExtras();
//        byte[] byteArray = bundle.getByteArray("image");
        int imageOrientation = bundle.getInt("orientation");
//
//        Bitmap originalBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
//        // Rotate the original image if required:
//        if (imageOrientation > 0) {
//            Matrix matrix = new Matrix();
//            matrix.setRotate(imageOrientation, originalBitmap.getWidth() / 2f, originalBitmap.getHeight() / 2f);
//            originalBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalBitmap.getWidth(), originalBitmap.getHeight(), matrix, false);
//        }
//
//        ContourDetector detector = new ContourDetector();
//        detector.detect(originalBitmap);
//
//        final Bitmap documentImage = detector.processImageAndRelease(originalBitmap, detector.getPolygonF(), ContourDetector.IMAGE_FILTER_NONE);
//
//        imageView.setImageBitmap(documentImage);
    }
}
