package com.example.grafika;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int READ_CODE = 342;
    private Button mRedBtn;
    private Button mGreenBtn;
    private Button mBlueBtn;
    private Button mBlackBtn;
    private Button mEreaseBtn;
    private DrawView powierzchniaRysunku;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //tworzymy menu na podstawie definicji w pliku XML
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveImage();
                break;
            case R.id.action_browse:
                System.out.println("browse");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRedBtn = findViewById(R.id.red_btn);
        mGreenBtn = findViewById(R.id.green_btn);
        mBlueBtn = findViewById(R.id.blue_btn);
        mBlackBtn = findViewById(R.id.black_btn);
        mEreaseBtn = findViewById(R.id.erease_btn);
        powierzchniaRysunku = (DrawView) findViewById(R.id.powierzchniaRysunku);
        powierzchniaRysunku.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                powierzchniaRysunku.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = powierzchniaRysunku.getHeight(); //height is ready
                int width = powierzchniaRysunku.getWidth(); //height is ready
                powierzchniaRysunku.init(height,width);
            }
        });
    }

    private void browseImagesPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED ||
                android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
//            browseImages();
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_CODE);
        }

    }

    //    public static Bitmap loadBitmapFromView(View v) {
//
//        DisplayMetrics dm = getResources().getDisplayMetrics();
//        v.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY),
//                View.MeasureSpec.makeMeasureSpec(dm.heightPixels, View.MeasureSpec.EXACTLY));
//        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
//        ((RelativeLayout) v).setGravity(Gravity.CENTER);
//        Bitmap returnedBitmap = Bitmap.createBitmap(v.getMeasuredWidth(),
//                v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(returnedBitmap);
//        v.draw(c);
//
//        return returnedBitmap;
//    }
    private void saveImage() {
        // getting the bitmap from DrawView class
        Bitmap bmp = powierzchniaRysunku.save();

        // opening a OutputStream to write into the file
        OutputStream imageOutStream = null;

        ContentValues cv = new ContentValues();

        // name of the file
        cv.put(MediaStore.Images.Media.DISPLAY_NAME, "drawing.png");

        // type of the file
        cv.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        // location of the file to be saved
        cv.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);

        // get the Uri of the file which is to be created in the storage
        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
        try {
            // open the output stream with the above uri
            imageOutStream = getContentResolver().openOutputStream(uri);

            // this method writes the files in storage
            bmp.compress(Bitmap.CompressFormat.PNG, 100, imageOutStream);

            // close the output stream after use
            imageOutStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}