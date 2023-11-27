package com.example.du_an_1_nhom_12.Activity;

import static android.os.Build.VERSION.SDK_INT;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;


import com.example.du_an_1_nhom_12.FRAGMENT.FragBookmark;
import com.example.du_an_1_nhom_12.FRAGMENT.FragHome;
import com.example.du_an_1_nhom_12.FRAGMENT.FragSetting;
import com.example.du_an_1_nhom_12.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainManageFile extends AppCompatActivity {
    FragmentManager manager;
    FragHome fragHome;
    FragBookmark fragBookmark;
    FragSetting fragSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_manage_file);
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.main_file));
        }

        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav);
        fragHome = new FragHome();
        fragBookmark = new FragBookmark();
        fragSetting = new FragSetting();

        manager = getSupportFragmentManager();
        manager.beginTransaction().add(R.id.frag_container_file, fragHome).commit();

        bottom_nav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                if (item.getItemId() == R.id.home) {
                    manager.beginTransaction().replace(R.id.frag_container_file, fragHome).commit();
                } else if (item.getItemId() == R.id.bookmark) {
                    manager.beginTransaction().replace(R.id.frag_container_file, fragBookmark).commit();
                } else if (item.getItemId() == R.id.setting) {
                    manager.beginTransaction().replace(R.id.frag_container_file, fragSetting).commit();
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.BorderDialogTheme);
        View v = getLayoutInflater().inflate(R.layout.dialog_quit_app, null, false);
        builder.setView(v);
        AlertDialog dialog = builder.create();
        dialog.show();

        Button btn_cancel = v.findViewById(R.id.btn_cancel2);
        Button btn_quit = v.findViewById(R.id.btn_quit);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}