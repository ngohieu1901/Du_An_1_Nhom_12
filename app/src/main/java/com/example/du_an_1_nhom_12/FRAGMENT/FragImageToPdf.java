package com.example.du_an_1_nhom_12.FRAGMENT;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.du_an_1_nhom_12.ADAPTER.ImageADAPTER;
import com.example.du_an_1_nhom_12.Constants;
import com.example.du_an_1_nhom_12.DTO.ImageDTO;
import com.example.du_an_1_nhom_12.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragImageToPdf extends Fragment {

    private static final String TAG = "IMAGE_LIST_TAG";

    private static final int STORAGE_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 101;

    private String[] cameraPermissions;
    private String[] storagePermissions;

    private Uri imageUri = null;

    private FloatingActionButton addImageFab;
    private RecyclerView imagesRv;
    private ImageButton ibPdf, ibDelete;

    private ArrayList<ImageDTO> allImageDTOArrayList;
    private ImageADAPTER imageADAPTER;

    private ProgressDialog progressDialog;

    private Context mContext;

    public FragImageToPdf() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        mContext = context;
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_frag_image_to_pdf, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        addImageFab = view.findViewById(R.id.addImageFab);
        imagesRv = view.findViewById(R.id.imagesRv);
        ibPdf = view.findViewById(R.id.ibPdf);
        ibDelete = view.findViewById(R.id.ibDelete);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

//        loadImages();

        addImageFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showInputImageDialog();
            }
        });

        //delete
        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Delete Images")
                        .setMessage("Are you sure you want to delete All/Selected images?")
                        .setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteImages(true);
                            }
                        })
                        .setNeutralButton("Delete Selected", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteImages(false);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        ibPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Convert To PDF")
                        .setMessage("Convert All/Selected Image to PDF")
                        .setPositiveButton("Convert All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                convertImagesToPdf(true);
                            }
                        })
                        .setNeutralButton("Convert Selected", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                convertImagesToPdf(false);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

        loadImages();

    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setHasOptionsMenu(true);
//
//    }

    private void convertImagesToPdf(boolean convertAll){
        Log.d(TAG, "convertImagesToPdf: convertAll: "+convertAll);

        progressDialog.setMessage("Converting to PDF...");
        progressDialog.show();

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Handler handler = new Handler(Looper.getMainLooper());

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: BG work start...");

                ArrayList<ImageDTO> imagesToPdfList = new ArrayList<>();

                if (convertAll) {

                    imagesToPdfList = allImageDTOArrayList;

                } else {

                    for (int i = 0; i < allImageDTOArrayList.size(); i++) {

                        if (allImageDTOArrayList.get(i).isChecked()) {

                            imagesToPdfList.add(allImageDTOArrayList.get(i));

                        }

                    }

                }

                Log.d(TAG, "run: imagesToPdfList size: "+imagesToPdfList.size());
                
                try {

                    File root = new File(mContext.getExternalFilesDir(null), Constants.PDF_FOLDER);
                    root.mkdirs();

                    long timestamp = System.currentTimeMillis();
                    String fileName = timestamp + ".pdf";

                    Log.d(TAG, "run: fileName: "+fileName);

                    File file = new File(root, fileName);

                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    PdfDocument pdfDocument = new PdfDocument();

                    for (int i = 0; i < imagesToPdfList.size(); i++) {

                        Uri imageToAdInPdfUri = imagesToPdfList.get(i).getImageUri();

                        try {
                            Bitmap bitmap;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(mContext.getContentResolver(), imageToAdInPdfUri));

                            } else {

                                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageToAdInPdfUri);

                            }

                            bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, false);

                            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), i+1).create();

                            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                            Paint paint = new Paint();
                            paint.setColor(Color.WHITE);

                            Canvas canvas = page.getCanvas();
                            canvas.drawPaint(paint);
                            canvas.drawBitmap(bitmap, 0f, 0f, null);

                            pdfDocument.finishPage(page);
                            bitmap.recycle();

                        } catch (Exception e) {
                            Log.e(TAG, "run: ", e);
                        }

                    }

                    pdfDocument.writeTo(fileOutputStream);
                    pdfDocument.close();

                } catch (Exception e) {

                    progressDialog.dismiss();
                    Log.e(TAG, "run: ", e);
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        Log.d(TAG, "run: Converted...");
                        progressDialog.dismiss();
                        Toast.makeText(mContext, "Converted...", Toast.LENGTH_SHORT).show();
                        
                    }
                });

            }
        });
    }

    private void deleteImages(boolean deleteAll){

        ArrayList<ImageDTO> imagesToDeleteList = new ArrayList<>();

        if (deleteAll) {
            imagesToDeleteList = allImageDTOArrayList;
        } else {

            for (int i = 0; i < allImageDTOArrayList.size(); i++) {
                if (allImageDTOArrayList.get(i).isChecked()){
                    imagesToDeleteList.add(allImageDTOArrayList.get(i));
                }
            }
        }

        for (int i = 0; i < imagesToDeleteList.size(); i++) {

            try {
                String pathOfImageToDelete = imagesToDeleteList.get(i).getImageUri().getPath();

                File file = new File(pathOfImageToDelete);

                if (file.exists()) {
                    boolean isDelete = file.delete();

                    Log.d(TAG, "deleteImages: isDelete: "+isDelete);
                }

            } catch (Exception e) {
                Log.e(TAG, "deleteImages: ", e);
            }
        }

        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();

        loadImages();

    }

    private void loadImages(){
        Log.d(TAG, "loadImages: ");
        allImageDTOArrayList = new ArrayList<>();
        imageADAPTER = new ImageADAPTER(mContext, allImageDTOArrayList);

        imagesRv.setAdapter(imageADAPTER);

        File folder = new File(mContext.getExternalFilesDir(null), Constants.IMAGES_FOLDER);
        
        if (folder.exists()) {
            Log.d(TAG, "loadImages: folder exists");
            File[] files = folder.listFiles();
            if (files != null) {
                Log.d(TAG, "loadImages: Folder exists and have image");

                for (File file: files) {
                    Log.d(TAG, "loadImages: fileName: "+file.getName());

                    Uri imageUri = Uri.fromFile(file);

                    ImageDTO imageDTO = new ImageDTO(imageUri, false);

                    allImageDTOArrayList.add(imageDTO);
                    imageADAPTER.notifyItemInserted(allImageDTOArrayList.size());

                }

            } else {
                Log.d(TAG, "loadImages: Folder exists but empty");
            }
        } else {
            Log.d(TAG, "loadImages: Folder doesn't exists");
        }
    
    }
    
    private void saveImageToAppLevelDirectory(Uri imageUriToBeSaved) {
        Log.d(TAG, "saveImageToAppLevelDirectory: ");
        try {
            Bitmap bitmap;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(mContext.getContentResolver(), imageUriToBeSaved));
            } else {
                bitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), imageUriToBeSaved);

                File directory = new File(mContext.getExternalFilesDir(null), Constants.IMAGES_FOLDER);
                directory.mkdirs();

                long timestamp = System.currentTimeMillis();
                String fileName = timestamp+ "jpeg";

                File file = new File(mContext.getExternalFilesDir(null), ""+Constants.IMAGES_FOLDER +"/" + fileName);

                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    Log.d(TAG, "saveImageToAppLevelDirectory: Image Saved");
                    Toast.makeText(mContext, "Image Saved", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Log.e(TAG, "saveImageToAppLevelDirectory: ", e);
                    Log.d(TAG, "saveImageToAppLevelDirectory: Failed to save image due to "+e.getMessage());
                    Toast.makeText(mContext, "Failed to save image due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            Log.e(TAG, "saveImageToAppLevelDirectory: ", e);
            Log.d(TAG, "saveImageToAppLevelDirectory: Failed to perpare image due to "+e.getMessage());
            Toast.makeText(mContext, "Failed to perpare image due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showInputImageDialog(){
        Log.d(TAG, "showInputImageDialog: ");
        PopupMenu popupMenu = new PopupMenu(mContext, addImageFab);

        popupMenu.getMenu().add(Menu.NONE, 1, 1, "CAMERA");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "GALLERY");

        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == 1) {
                    Log.d(TAG, "onMenuItemClick: Camera is clicked, check if camera permissions are granted or not");
                    if (checkCameraPermission()) {
                        pickImageCamera();
                    } else {
                        requestCameraPermissions();
                    }
                } else if (itemId == 2){
                    Log.d(TAG, "onMenuItemClick: Gallery is clicked, check if storage permission is granted or not");
                    if (checkStoragePermission()) {
                        pickImageGallery();
                    } else {
                        requestStoragePermission();
                    }
                }

                return true;
            }
        });

    }

    private void pickImageGallery(){
        Log.d(TAG, "pickImageGallery: ");
        
        Intent intent = new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Log.d(TAG, "onActivityResult: Picked image gallery: " + imageUri);
                        //save the picked image
                        saveImageToAppLevelDirectory(imageUri);

                        ImageDTO imageDTO = new ImageDTO(imageUri, false);
                        allImageDTOArrayList.add(imageDTO);
                        imageADAPTER.notifyItemInserted(allImageDTOArrayList.size());
                    } else {
                        Toast.makeText(mContext, "Cancelled...", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private void pickImageCamera(){
        Log.d(TAG, "pickImageCamera: ");
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "TEMP IMAGE TITLE");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "TEMP IMAGE DESCRIPTION");

        imageUri = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.d(TAG, "onActivityResult: Picked image camera: " + imageUri);
                        saveImageToAppLevelDirectory(imageUri);

                        ImageDTO imageDTO = new ImageDTO(imageUri, false);
                        allImageDTOArrayList.add(imageDTO);
                        imageADAPTER.notifyItemInserted(allImageDTOArrayList.size());
                    } else {
                        Toast.makeText(mContext, "Cancelled...", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private boolean checkStoragePermission(){
        Log.d(TAG, "checkStoragePermission: ");
        boolean result = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return result;
    }

    private void requestStoragePermission(){
        Log.d(TAG, "requestStoragePermission: ");
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        Log.d(TAG, "checkCameraPermission: ");
        boolean cameraResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean storageResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return cameraResult && storageResult;
    }

    private void requestCameraPermissions() {
        Log.d(TAG, "requestCameraPermissions: ");
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_REQUEST_CODE) {

            if (grantResults.length > 0) {

                boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (cameraAccepted && storageAccepted) {
                    Log.d(TAG, "onRequestPermissionsResult: both permissions (Camera & Gallery) are granted, we can launch camera intent");
                    pickImageCamera();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: Camera & Storage permission are required");
                    Toast.makeText(mContext, "Camera & Storage permission are required", Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.d(TAG, "onRequestPermissionsResult: Cancelled");
                Toast.makeText(mContext, "Cancelled...", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == STORAGE_REQUEST_CODE) {

            if (grantResults.length > 0) {
                boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                if (storageAccepted) {
                    Log.d(TAG, "onRequestPermissionsResult: storage permission granted, we can launch gallery intent");
                    pickImageGallery();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: storage permission denied, can't launch gallery intent");
                    Toast.makeText(mContext, "Storage permission is required...", Toast.LENGTH_SHORT).show();
                }

            } else {
                Log.d(TAG, "onRequestPermissionsResult: Cancelled");
                Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show();
            }

        }

    }
}