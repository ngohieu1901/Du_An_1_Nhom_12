package com.example.du_an_1_nhom_12.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.pdf.PdfDocument;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.du_an_1_nhom_12.Constants;
import com.example.du_an_1_nhom_12.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;

public class ScanPdfActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 100;

    String folderPath = Environment.getDataDirectory().getAbsolutePath() + "/storage/emulated/0/";

    File directory = new File("/sdcard/");

    Button camera, upload, convert;
    ImageView img;
    TextView title;
    ProgressBar progressBar;

    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_pdf);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.language));
        }
        askPermission();

//        camera = findViewById(R.id.convertor_toll_camera);
        upload = findViewById(R.id.convertor_toll_upload);
        convert = findViewById(R.id.convertor_toll_convert);
        img = findViewById(R.id.convertor_toll_imageView);
        title = findViewById(R.id.convertor_toll_fileName);
        progressBar = findViewById(R.id.progressBar);
        back = findViewById(R.id.iv_back_setting);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (directory.exists()) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    startActivityForResult(intent, REQUEST_CODE);
//                } else {
//                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(ScanPdfActivity.this, "try again", Toast.LENGTH_SHORT).show();
//                    createFolder();
//                }
//
//            }
//        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (directory.exists()) {
                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(ScanPdfActivity.this, "try again", Toast.LENGTH_SHORT).show();
                    createFolder();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.GONE);
        if (data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA,MediaStore.Images.Media.DISPLAY_NAME};
            Cursor cursor = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                cursor = getContentResolver().query(selectedImage, filePathColumn, null, null);
            }
            cursor.moveToFirst();
            int filePath = cursor.getColumnIndex(filePathColumn[0]);
            int fileName = cursor.getColumnIndex(filePathColumn[1]);
            String path = cursor.getString(filePath);
            String name = cursor.getString(fileName);
            cursor.close();
            img.setImageURI(Uri.parse(path));
            title.setText(name);
            convert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean converted = imageToPDF(path, name);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    if (!converted) {
                                        Toast.makeText(ScanPdfActivity.this, "fail", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ScanPdfActivity.this, "successful conversion", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                    thread.start();
                }
            });
        } else {
            Toast.makeText(this, "select a image", Toast.LENGTH_SHORT).show();
        }
    }

    private void askPermission(){
        if (ContextCompat.checkSelfPermission(ScanPdfActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(ScanPdfActivity.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ScanPdfActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            createFolder();
        }
    }

    private void createFolder(){
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
        directory.mkdirs();
    }

    private boolean imageToPDF(String path, String name){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            PdfDocument pdfDocument = new PdfDocument();
            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(960, 1280, 0).create();
            PdfDocument.Page page = pdfDocument.startPage(pageInfo);
            page.getCanvas().drawBitmap(bitmap, 0, 0, null);
            pdfDocument.finishPage(page);
            String changeName = getWithoutExtension(name);
            String newPath = directory + "/" + changeName + ".pdf";
            File file = new File(newPath);
            try {
                pdfDocument.writeTo(new FileOutputStream(file));
                MediaScannerConnection.scanFile(ScanPdfActivity.this, new String[]{file.toString()}, null, null);
            } catch (Exception e) {
                e.getStackTrace();
            }
            pdfDocument.close();
            return true;
        } else {
            return false;
        }
    }

    private String getWithoutExtension(String name) {
        return name.substring(0, name.lastIndexOf("."));
    }

}