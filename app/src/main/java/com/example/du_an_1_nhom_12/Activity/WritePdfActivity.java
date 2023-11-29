package com.example.du_an_1_nhom_12.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.du_an_1_nhom_12.R;

public class WritePdfActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1000;

    EditText mTextEt;
    Button mSaveBtn;
    Button mCancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_pdf);
        mTextEt = findViewById(R.id.textEt);
        mSaveBtn = findViewById(R.id.saveBtn);
        mCancelBtn = findViewById(R.id.cancelBtn);
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WritePdfActivity.this, MainManageFile.class));
                finish();
            }
        });
    }
}