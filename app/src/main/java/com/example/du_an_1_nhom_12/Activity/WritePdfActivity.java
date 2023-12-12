package com.example.du_an_1_nhom_12.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.du_an_1_nhom_12.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WritePdfActivity extends AppCompatActivity {
    private static final int STORAGE_CODE = 1000;
    ImageView back_writepdf;
    EditText mTextEt;
    Button mSaveBtn;
    Button mCancelBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_pdf);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.language));
        }
        mTextEt = findViewById(R.id.textEt);
        mSaveBtn = findViewById(R.id.saveBtn);
        mCancelBtn = findViewById(R.id.cancelBtn);
        back_writepdf = findViewById(R.id.back_writepdf);

        back_writepdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WritePdfActivity.this, ManageFileActivity.class));
                finish();
            }
        });
        mCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WritePdfActivity.this, ManageFileActivity.class));
                finish();
            }
        });

    mSaveBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                        PackageManager.PERMISSION_DENIED){
                    //permission was not grandted, request it
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions, STORAGE_CODE);
                }
                else {
                    savePdf();
                }
            }
            else {
                // system OS < Marshmallow no required to check runtime permission
            }
        }
    });
}
    private void savePdf(){

        Document mDoc = new Document();
        //pdf file name
        String mFileName = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(System.currentTimeMillis());
        //pdf file path
        String mFilePath = Environment.getExternalStorageDirectory() + "/" + mFileName + ".pdf";
        try {
            PdfWriter.getInstance(mDoc , new FileOutputStream(mFilePath));
            // open the document dor writign
            mDoc.open();
            //get text form document for writing
            String mText = mTextEt.getText().toString();
            // add autor of te document (opitional)
            mDoc.addAuthor("Atif Pervaiz");
            // add paragrag tto the document
            mDoc.add(new Paragraph(mText));
            // close the document
            mDoc.close();
            //sjow tin nhan da luu , it will show file name and file pat
            Toast.makeText(this,mFileName + ".pdf\nis saved to \n"+ mFileName, Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case STORAGE_CODE:{
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    //permission was grandted from popup
                    savePdf();
                }else {
                    //permissionwas
                    Toast.makeText(this,"Permission denied...!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}