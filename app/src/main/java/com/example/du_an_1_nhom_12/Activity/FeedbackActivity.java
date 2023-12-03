package com.example.du_an_1_nhom_12.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.du_an_1_nhom_12.R;
import com.example.du_an_1_nhom_12.SUPPORT.OnSingleClickListener;

public class FeedbackActivity extends AppCompatActivity {

    private EditText editTextFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        editTextFeedback = findViewById(R.id.editTextFeedback);
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
                sendFeedback();
            }
        });
    }

    private void sendFeedback() {
        String feedbackText = editTextFeedback.getText().toString().trim();

        if (!feedbackText.isEmpty()) {
            // Lấy thông tin về phiên bản và tên thiết bị
            String versionName = "1.1.1"; // Đổi thành phiên bản thực tế của ứng dụng
            String deviceName = Build.MODEL;

            // Tạo nội dung email với thông tin được thêm vào
            String emailContent = "PDF Viewer - Scanner PDF\n\n"
                    + "version: " + versionName + "\n"
                    + "device: " + deviceName + "\n"
                    + "content: " + feedbackText;

            // Tạo Intent để mở ứng dụng Gmail
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("message/rfc822");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"z3dat1905@gmail.com"}); // Điền địa chỉ email của bạn
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from PDF Viewer - Scanner PDF\n");
            emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send feedback using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(FeedbackActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(FeedbackActivity.this, "Please enter feedback before sending.", Toast.LENGTH_SHORT).show();
        }
    }
}
