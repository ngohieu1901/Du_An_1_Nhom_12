package com.example.du_an_1_nhom_12.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.du_an_1_nhom_12.R;
import com.example.du_an_1_nhom_12.SUPPORT.OnSingleClickListener;

public class FeedbackActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Button btn_cancel = findViewById(R.id.btn_cancel1);
        Button btn_send = findViewById(R.id.btn_send);
        btn_cancel.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        btn_send.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Toast.makeText(FeedbackActivity.this, getString(R.string.toast_sent), Toast.LENGTH_SHORT).show();
            }
        });
    }
}