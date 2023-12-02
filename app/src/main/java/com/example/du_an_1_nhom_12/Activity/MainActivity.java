package com.example.du_an_1_nhom_12.Activity;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.du_an_1_nhom_12.R;


public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean lang_selected = preferences.getBoolean("lang_selected",false);
        boolean started = preferences.getBoolean("started",false);
        boolean permission = preferences.getBoolean("permission",false);
        progressBar = findViewById(R.id.loading_bar);


        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (lang_selected){
                    showNotification();
                    //dc chon
                    Intent intent = new Intent(MainActivity.this,MainViewPager.class);
                    startActivity(intent);
                    finishAffinity();
                    if (started) {
                        //dc chon
                        Intent intent0 = new Intent(MainActivity.this,PermissionActivity.class);
                        intent0.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent0);
                        finishAffinity();
                        if (permission){
                            //dc chon
                            Intent intent1 = new Intent(MainActivity.this,MainManageFile.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent1);
                            finishAffinity();
                        }else {
                            //chua dc chon
                            Intent intent2 = new Intent(MainActivity.this,PermissionActivity.class);
                            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent2);
                            finishAffinity();
                        }
                    }else {
                        //chua dc chon
                        Intent intent3 = new Intent(MainActivity.this, MainViewPager.class);
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent3);
                        finishAffinity();
                    }
                }else {
                    //chua dc chon
                    Intent intent4 = new Intent(MainActivity.this, LanguageActivity.class);
                    intent4.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent4);
                    finishAffinity();
                }
            }
        }.start();
    }
    private void showNotification(){
        // toạ intent cho activity
        Intent notificationItent = new Intent(this,HuongDanActivity.class );
        notificationItent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationItent, PendingIntent.FLAG_IMMUTABLE);
        //xây dựng thông báo

        Bitmap logo = BitmapFactory.decodeResource(getResources(),R.drawable.img_1);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel_id")
                .setSmallIcon(R.drawable.more_icon)
                .setContentTitle("Thông báo")
                .setContentText("Nhấp vào để mở")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)

                // thiết lập ảnh to
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(logo)
                        .bigLargeIcon(null)
                )

                .setAutoCancel(true);
        // hiển thị thông báo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1 , builder.build());
    }
}