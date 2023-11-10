package com.example.du_an_1_nhom_12.Activity;

import static android.os.Build.VERSION.SDK_INT;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.du_an_1_nhom_12.R;


public class PermissionActivity extends AppCompatActivity {
    TextView tv_continue;
    Switch sw_allow;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CODE = 123;

    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.language));
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = preferences.edit();

        sw_allow = findViewById(R.id.sw_allow);
        tv_continue = findViewById(R.id.tv_continue);

        tv_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("permission", true);
                editor.apply();
                startActivity(new Intent(PermissionActivity.this, MainManageFile.class));
                finish();
            }
        });

        sw_allow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (isChecked) {
                        if (Environment.isExternalStorageManager()) {
                            sw_allow.setChecked(true);
                            tv_continue.setVisibility(View.INVISIBLE);
                        } else {
                            new AlertDialog.Builder(PermissionActivity.this)
                                    .setTitle("Request permissions")
                                    .setMessage("Please grant permission to use the application.")
                                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Yêu cầu quyền
                                            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                            intent.addCategory("android.intent.category.DEFAULT");
                                            intent.setData(Uri.parse(String.format("package:%s", getApplicationContext().getPackageName())));
                                            startActivityForResult(intent, REQUEST_CODE);
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Không đồng ý, tắt Switch và ẩn TextView
                                            sw_allow.setChecked(false);
                                            tv_continue.setVisibility(View.INVISIBLE);
                                        }
                                    })
                                    .show();
                        }
                    } else {
                        tv_continue.setVisibility(View.GONE);
                    }
                    //android < 11
                } else {
                    if (isChecked) {
                        // Kiểm tra quyền truy cập bộ nhớ
                        if (ContextCompat.checkSelfPermission(PermissionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            // đc cấp
                            tv_continue.setVisibility(View.VISIBLE);
                        } else {
                            // Chưa đc cấp
                            requestStoragePermission();
                        }
                    } else {
                        // Switch tắt, ẩn TextView
                        tv_continue.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Request permissions")
                    .setMessage("Please grant permission to use the application.")
                    .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Yêu cầu quyền
                            ActivityCompat.requestPermissions(PermissionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
                        }
                    })
                    .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Không đồng ý, tắt Switch và ẩn TextView
                            sw_allow.setChecked(false);
                            tv_continue.setVisibility(View.INVISIBLE);
                        }
                    })
                    .show();
        } else {
            // Yêu cầu quyền
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền được cấp, hiển thị TextView
                sw_allow.setChecked(true);
                tv_continue.setVisibility(View.VISIBLE);
            } else {
                // Quyền bị từ chối, tắt Switch và ẩn TextView
                sw_allow.setChecked(false);
                tv_continue.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    sw_allow.setChecked(true);
                    tv_continue.setVisibility(View.VISIBLE);
                    sw_allow.setEnabled(false);
                } else {
                    sw_allow.setChecked(false);
                    tv_continue.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}


