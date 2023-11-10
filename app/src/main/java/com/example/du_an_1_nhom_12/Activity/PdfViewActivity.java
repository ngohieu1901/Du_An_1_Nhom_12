package com.example.du_an_1_nhom_12.Activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.du_an_1_nhom_12.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.util.FitPolicy;

import java.io.File;

public class PdfViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_pdf));
        }
        PDFView pdfView = findViewById(R.id.pdf_view);
        ImageButton btn_back = findViewById(R.id.btn_back);
        TextView tv_name = findViewById(R.id.tv_name);
        TextView tv_count = findViewById(R.id.tv_count);

        String name = getIntent().getStringExtra("name");
        tv_name.setText(name);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final boolean[] isCountVisible = {true};
        String path = getIntent().getStringExtra("path");
        try {
            File file = new File(path);
            if (file.exists()){
                pdfView.fromFile(file)
                        .defaultPage(0)
                        .onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                tv_count.setText((page + 1) + "/" + pageCount);
                                if (isCountVisible[0]){
                                    isCountVisible[0] = false;
                                    tv_count.setVisibility(View.VISIBLE);
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            tv_count.setVisibility(View.GONE);
                                            isCountVisible[0] = true;
                                        }
                                    }, 2000);
                                }
                            }
                        })
                        .swipeHorizontal(false)
                        .pageFitPolicy(FitPolicy.WIDTH)
                        .enableAntialiasing(true)
                        .spacing(15)
                        .pageFling(true)
                        .load();

            }else {
                Toast.makeText(this, this.getString(R.string.toast_exists), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

}