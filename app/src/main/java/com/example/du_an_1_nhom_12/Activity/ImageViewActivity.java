package com.example.du_an_1_nhom_12.Activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.du_an_1_nhom_12.R;

public class ImageViewActivity extends AppCompatActivity {

    private ImageView imageIv;
    private String image;

    private static final String TAG = "IMAGE_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("ImageView");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
//        getSupportActionBar().setTitle("ImageView");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imageIv = findViewById(R.id.imageIv);

        image = getIntent().getStringExtra("imageUri");
        Log.d(TAG, "onCreate: Image: "+image);

        Glide.with(this)
                .load(image)
                .placeholder(R.drawable.ic_image_black)
                .into(imageIv);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}