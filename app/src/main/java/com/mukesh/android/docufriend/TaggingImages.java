package com.mukesh.android.docufriend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import net.doo.snap.lib.detector.ContourDetector;


public class TaggingImages extends AppCompatActivity {

    private ImageView imageView;
    private Button buuton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tagging_images);

        String path = getIntent().getStringExtra("image");

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        ImageView imageView = (ImageView) findViewById(R.id.scannedImage);


        final ContourDetector detector = new ContourDetector();
        detector.detect(bitmap);
        final Bitmap documentImage = detector.processImageAndRelease(bitmap, detector.getPolygonF(), ContourDetector.IMAGE_FILTER_NONE);

        imageView.setImageBitmap(documentImage);






    }
}
