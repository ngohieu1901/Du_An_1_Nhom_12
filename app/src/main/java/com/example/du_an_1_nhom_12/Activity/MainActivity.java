package com.example.du_an_1_nhom_12.Activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.du_an_1_nhom_12.R;
import com.example.du_an_1_nhom_12.SUPPORT.NotifyConfig;

import java.util.Date;


public class MainActivity extends AppCompatActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showNotification();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean lang_selected = preferences.getBoolean("lang_selected",false);
        boolean started = preferences.getBoolean("started",false);
        boolean permission = preferences.getBoolean("permission",false);
        progressBar = findViewById(R.id.loading_bar);
        //object: CounDownTimer(2000,1000){
        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }
            @Override
            public void onFinish() {
                if (lang_selected){
                    //dc chon
                    Intent intent = new Intent(MainActivity.this, MainViewPager.class);
                    startActivity(intent);
                    finishAffinity();
                    if (started) {
                        //dc chon
                        Intent intent0 = new Intent(MainActivity.this, PermissionActivity.class);
                        intent0.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent0);
                        finishAffinity();
                        if (permission){
                            //dc chon
                            Intent intent1 = new Intent(MainActivity.this, ManageFileActivity.class);
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

    public void showNotification(){
        //Khai bao intent de nhan tuong tac khi bam vao notify
        Intent intendDemo = new Intent(this, TutorialActivity.class);
        intendDemo.putExtra("duLieu","Dữ liệu gửi từ notifi vao acti");
        //tạo stack để gọi acti
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(MainActivity.this);
        stackBuilder.addNextIntentWithParentStack(intendDemo);
        //Lay pendingIntent de truyen vao notifi
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE);
        //Khoi tao layout cho Notify
        Bitmap logo = BitmapFactory.decodeResource(getResources(),R.drawable.bg_notify);
        Notification customNotification = new NotificationCompat.Builder(MainActivity.this, NotifyConfig.CHANEL_ID)
                .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Tips and Tricks use PDF Manager")
                .setContentIntent(resultPendingIntent)//intend nhan tuong tac
                //thiet lap ảnh to
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(logo)
                        .bigLargeIcon(null)
                )
                .setLargeIcon(logo)
                .setColor(Color.RED)
                .setAutoCancel(true)
                .build();
        //Khoi tao manager de quan notifi
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
        // moi lan hien thi notifi can tao 1 cai ID cho thong bao rieng
        int id_notify = (int) new Date().getTime();// lay chuoi time la phu hop
        //lenh hien thi notify
        notificationManagerCompat.notify(id_notify,customNotification);
    }
}