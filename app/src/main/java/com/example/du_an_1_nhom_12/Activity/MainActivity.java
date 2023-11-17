package com.example.du_an_1_nhom_12.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.du_an_1_nhom_12.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean lang_selected = preferences.getBoolean("lang_selected",false);
        boolean started = preferences.getBoolean("started",false);
        boolean permission = preferences.getBoolean("permission",false);

        CountDownTimer countDownTimer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (lang_selected){
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

}