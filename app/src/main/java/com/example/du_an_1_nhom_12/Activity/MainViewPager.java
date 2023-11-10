package com.example.du_an_1_nhom_12.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.du_an_1_nhom_12.ADAPTER.ViewPagerADAPTER;
import com.example.du_an_1_nhom_12.R;


import me.relex.circleindicator.CircleIndicator;

public class MainViewPager extends AppCompatActivity {
    CircleIndicator indicator;
    ViewPager pager;
    ViewPagerADAPTER adapter;
    TextView tv_skip;
    Button btn_next;
    private static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view_pager);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();

        tv_skip = findViewById(R.id.tv_skip);
        pager = findViewById(R.id.pager);
        indicator = findViewById(R.id.indicator);
        btn_next = findViewById(R.id.btn_next);

        adapter = new ViewPagerADAPTER(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(adapter);

        indicator.setViewPager(pager);

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(2);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pager.getCurrentItem() < 2) {
                    pager.setCurrentItem(pager.getCurrentItem() + 1);
                } else if (pager.getCurrentItem() == 2) {
                    editor.putBoolean("started", true);
                    editor.apply();
                    Intent intent = new Intent(MainViewPager.this, PermissionActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    tv_skip.setText("");
                    btn_next.setText(R.string.btn_start);
                } else {
                    tv_skip.setText(R.string.tv_skip);
                    btn_next.setText(R.string.btn_next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE){
//            startActivity(new Intent(MainViewPager.this,MainManageFile.class));
//            finish();
//        }
//    }
}