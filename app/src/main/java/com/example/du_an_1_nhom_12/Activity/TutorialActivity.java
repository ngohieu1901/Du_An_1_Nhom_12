package com.example.du_an_1_nhom_12.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.du_an_1_nhom_12.ADAPTER.ImageAdapter;
import com.example.du_an_1_nhom_12.FRAGMENT.FragSetting;
import com.example.du_an_1_nhom_12.R;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends AppCompatActivity {
    private ImageAdapter pdfTextAdapter;
    private ImageAdapter pdfTextAdapter1;
    private ImageAdapter pdfTextAdapter2;
    private ImageAdapter pdfTextAdapter3;
    private ImageAdapter convertTextAdapter;
    private ImageAdapter convertTextAdapter1;
    private ImageView imageViewback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        imageViewback = findViewById(R.id.backButton);

        imageViewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TutorialActivity.this, ManageFileActivity.class));
                finish();
            }
        });
        // Khởi tạo các thành phần
        initViews();

        // Hiển thị danh sách ảnh cho phần "Hướng dẫn tạo file PDF text"
        List<String> pdfTextImages = new ArrayList<>();
        pdfTextImages.add("android.resource://" + getPackageName() + "/" + R.drawable.edit_1);  // Thêm đường dẫn hình ảnh của bạn vào đây
        pdfTextImages.add("android.resource://" + getPackageName() + "/" + R.drawable.anhsua2);
        pdfTextImages.add("android.resource://" + getPackageName() + "/" + R.drawable.anhsua3);
        pdfTextAdapter = new ImageAdapter(this, pdfTextImages);
        RecyclerView pdfTextRecyclerView = findViewById(R.id.pdfTextRecyclerView);
        pdfTextRecyclerView.setAdapter(pdfTextAdapter);
        pdfTextRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Hiển thị danh sách ảnh cho phần "Hướng dẫn convert img to PDF"
        List<String> convertTextImages = new ArrayList<>();
        convertTextImages.add("android.resource://" + getPackageName() + "/" + R.drawable.edit_2);  // Thêm đường dẫn hình ảnh của bạn vào đây
        convertTextImages.add("android.resource://" + getPackageName() + "/" + R.drawable.edit_2);
        convertTextImages.add("android.resource://" + getPackageName() + "/" + R.drawable.edit_3);
        convertTextAdapter = new ImageAdapter(this, convertTextImages);
        RecyclerView convertTextRecyclerView = findViewById(R.id.convertTextRecyclerView);
        convertTextRecyclerView.setAdapter(convertTextAdapter);
        convertTextRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    private void initViews() {
        TextView pdfTextTitle = findViewById(R.id.pdfTextTitle);
        TextView convertTextTitle = findViewById(R.id.convertTextTitle);
        // Thêm nội dung cho các tiêu đề
        pdfTextTitle.setText("Hướng dẫn tạo file PDF text");
        convertTextTitle.setText("Hướng dẫn chuyển ảnh thành file PDF");
    }
}