package com.example.grafika;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class BrowseActivity extends AppCompatActivity implements
        FragmentList.OverviewFragmentActivityListener {
        private boolean showDetails = false;
        private Image currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if(getIntent().getExtras() != null){
            if(intent.hasExtra("currentImage")){
                currentImage = intent.getExtras().getParcelable("currentImage");
                System.out.println("Image id:" + currentImage.getContentUri());
            }
            if(intent.hasExtra("showDetails")){
                showDetails = intent.getExtras().getBoolean("showDetails");
                System.out.println(showDetails);
            }
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if(showDetails){
                setContentView(R.layout.activity_browse_landscape);
                FragmentDetails fragment = (FragmentDetails)
                        getSupportFragmentManager()
                                .findFragmentById(R.id.detailFragment);
                if (fragment != null && fragment.isInLayout()) {
                    fragment.setImage(currentImage);
                }

            }
            else{
                System.out.println("bez show details");
                setContentView(R.layout.activity_browse);
            }

        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(showDetails) {
                Intent detailsIntent = new Intent(this, ImagePreviewActivity.class);
                detailsIntent.putExtra("currentImage",currentImage);
                finish();
                startActivity(detailsIntent);
            }else{
                setContentView(R.layout.activity_browse);
            }
        }
    }
    @Override
    public void onItemSelected(String msg) {

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }
}
