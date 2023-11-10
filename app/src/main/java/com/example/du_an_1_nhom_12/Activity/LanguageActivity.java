package com.example.du_an_1_nhom_12.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.du_an_1_nhom_12.ADAPTER.LanguageADAPTER;
import com.example.du_an_1_nhom_12.DTO.LanguageDTO;
import com.example.du_an_1_nhom_12.R;

import java.util.ArrayList;

public class LanguageActivity extends AppCompatActivity implements LanguageADAPTER.OnItemClickListener {
    RecyclerView rc_lang;
    LanguageADAPTER adapter;
    ArrayList<LanguageDTO> list;
    private boolean openActivity = false;
    ImageView tv_check;
    private FrameLayout native_ads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.language));
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        rc_lang = findViewById(R.id.rc_lang);
        list = new ArrayList<>();
        list.add(new LanguageDTO(R.drawable.anh,"English",false));
        list.add(new LanguageDTO(R.drawable.tbn_2,"Spainish",false));
        list.add(new LanguageDTO(R.drawable.bdn_2,"Portuguese",false));
        list.add(new LanguageDTO(R.drawable.duc_2,"German",false));
        list.add(new LanguageDTO(R.drawable.phap,"French",false));

        adapter = new LanguageADAPTER(this,list,this);
        adapter.setOnItemClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rc_lang.setLayoutManager(linearLayoutManager);
        rc_lang.setAdapter(adapter);

        tv_check = findViewById(R.id.tv_check);

        tv_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!openActivity){
                    openActivity = true;
                    editor.putBoolean("lang_selected",true);
                    editor.apply();
                    startActivity(new Intent(LanguageActivity.this,MainViewPager.class));
                    finish();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            openActivity = false;
                        }
                    },1000);
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        if (position == RecyclerView.NO_POSITION || position == 2 || position == 3 ) {
            tv_check.setVisibility(View.INVISIBLE);
        } else {
            tv_check.setVisibility(View.VISIBLE);
        }
    }

}