package com.example.du_an_1_nhom_12.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRatingBar;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.du_an_1_nhom_12.R;

public class RateAppActivity extends AppCompatActivity {
    private float userRate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_app);

        ImageView ratingImage = findViewById(R.id.ratingImage);
        AppCompatRatingBar ratingBar = findViewById(R.id.ratingBar);
        Button rateUs = findViewById(R.id.rateUs);
        TextView notNow = findViewById(R.id.notNow);

        notNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v <= 0) {
                    ratingImage.setImageResource(R.drawable.non);
                } else if (v <= 1) {
                    ratingImage.setImageResource(R.drawable.one);
                } else if (v <= 2) {
                    ratingImage.setImageResource(R.drawable.two);
                } else if (v <= 3) {
                    ratingImage.setImageResource(R.drawable.three);
                } else if (v <= 4) {
                    ratingImage.setImageResource(R.drawable.four);
                } else if (v <= 5) {
                    ratingImage.setImageResource(R.drawable.five);
                }

                animateImage(ratingImage);

                userRate = v;

            }
        });

    }

    private void animateImage(ImageView ratingImage){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }

}