package com.example.du_an_1_nhom_12.FRAGMENT;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.du_an_1_nhom_12.ADAPTER.HomeADAPTER;
import com.example.du_an_1_nhom_12.DTO.AllFileDTO;
import com.example.du_an_1_nhom_12.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class FragHome extends Fragment  {

    RecyclerView rc_file;
    HomeADAPTER adapter;
    EditText search_file;
    ImageView iv_clear;
    ImageButton sortFile;
    LinearLayout layoutAZ, layoutZA;
    ArrayList<AllFileDTO> list = new ArrayList<>();
    String TAG = "ok";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //save phần tử trong dialog
        SharedPreferences preferences = getActivity().getSharedPreferences("SAVE_DIALOG_HOME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        list = readFile();
        if (list.size() == 0) {
            list = loadFiles();
            saveFile(list);
        }
        adapter = new HomeADAPTER(getContext(), list);
        adapter.notifyDataSetChanged();
        rc_file = view.findViewById(R.id.rc_file);
        adapter = new HomeADAPTER(getContext(), list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rc_file.setLayoutManager(linearLayoutManager);
        rc_file.setAdapter(adapter);

        search_file = view.findViewById(R.id.search_file_home);
        iv_clear = view.findViewById(R.id.iv_clear);
        search_file.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                if (s.length() == 0) {
                    iv_clear.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(search_file.getWindowToken(), 0);
                    }
                    search_file.clearFocus();
                }else {
                    iv_clear.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        iv_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_file.setText("");
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(search_file.getWindowToken(), 0);
                }
                search_file.clearFocus();
            }
        });
        sortFile = view.findViewById(R.id.sort_file_home);
        sortFile.setOnClickListener(new View.OnClickListener() {
            boolean isChecked = false;
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.dialog_sort, null, false);
                builder.setView(view);
                Dialog dialog = builder.create();
                dialog.show();

                layoutAZ = view.findViewById(R.id.layout_az);
                layoutZA = view.findViewById(R.id.layout_za);
                ImageView check_az = view.findViewById(R.id.check_az);
                ImageView check_za = view.findViewById(R.id.check_za);
                ImageView iv_az = view.findViewById(R.id.iv_az);
                ImageView iv_za = view.findViewById(R.id.iv_za);
                TextView tv_az = view.findViewById(R.id.tv_az);
                TextView tv_za = view.findViewById(R.id.tv_za);

                // get SharedPreferences
                boolean isCheckAzVisible = preferences.getBoolean("check_az_visible", false);
                boolean isCheckZaVisible = preferences.getBoolean("check_za_visible", false);
                int ivAzResource = preferences.getInt("iv_az_resource", R.drawable.sort_az_white);
                String tvAzColor = preferences.getString("tv_az_color", "#000000");
                int ivZaResource = preferences.getInt("iv_za_resource", R.drawable.sort_az_white);
                String tvZaColor = preferences.getString("tv_za_color", "#000000");
                // Áp dụng các giá trị cho các thành phần giao diện của dialog
                check_az.setVisibility(isCheckAzVisible ? View.VISIBLE : View.INVISIBLE);
                check_za.setVisibility(isCheckZaVisible ? View.VISIBLE : View.INVISIBLE);
                iv_az.setImageResource(ivAzResource);
                tv_az.setTextColor(Color.parseColor(tvAzColor));
                iv_za.setImageResource(ivZaResource);
                tv_za.setTextColor(Color.parseColor(tvZaColor));
                layoutAZ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check_az.setVisibility(View.VISIBLE);
                        check_za.setVisibility(View.INVISIBLE);
                        iv_az.setImageResource(R.drawable.sort_az);
                        tv_az.setTextColor(Color.parseColor("#217346"));
                        iv_za.setImageResource(R.drawable.sort_az_white);
                        tv_za.setTextColor(Color.parseColor("#FF000000"));
                        // set SharedPreferences
                        editor.putBoolean("check_az_visible", true);
                        editor.putBoolean("check_za_visible", false);
                        editor.putInt("iv_az_resource", R.drawable.sort_az);
                        editor.putString("tv_az_color", "#217346");
                        editor.putInt("iv_za_resource", R.drawable.sort_az_white);
                        editor.putString("tv_za_color", "#FF000000");
                        editor.apply();
                        Collections.sort(list, new Comparator<AllFileDTO>() {
                            @Override
                            public int compare(AllFileDTO o1, AllFileDTO o2) {
                                return o1.getTen().toLowerCase().compareTo(o2.getTen().toLowerCase());
                            }
                        });
                        saveFile(list);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), getContext().getString(R.string.toast_sortAZ), Toast.LENGTH_SHORT).show();
                    }
                });

                layoutZA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        check_az.setVisibility(View.INVISIBLE);
                        check_za.setVisibility(View.VISIBLE);
                        iv_za.setImageResource(R.drawable.sort_az);
                        tv_za.setTextColor(Color.parseColor("#217346"));
                        iv_az.setImageResource(R.drawable.sort_az_white);
                        tv_az.setTextColor(Color.parseColor("#FF000000"));
                        // Lưu các giá trị vào SharedPreferences
                        editor.putBoolean("check_az_visible", false);
                        editor.putBoolean("check_za_visible", true);
                        editor.putInt("iv_az_resource", R.drawable.sort_az_white);
                        editor.putString("tv_az_color", "#FF000000");
                        editor.putInt("iv_za_resource", R.drawable.sort_az);
                        editor.putString("tv_za_color", "#217346");
                        editor.apply();

                        Collections.sort(list, new NameComparator());
                        saveFile(list);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), getContext().getString(R.string.toast_sortZA), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public class NameComparator implements Comparator<AllFileDTO> {
        @Override
        public int compare(AllFileDTO o1, AllFileDTO o2) {
            return o2.getTen().toLowerCase().compareTo(o1.getTen().toLowerCase());
        }
    }

    public void saveFile(ArrayList<AllFileDTO> list) {

        try {
            FileOutputStream fileOutputStream = getActivity().openFileOutput("KEY_NAME", getActivity().MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "Sava: ", e);
        }
    }

    public ArrayList readFile() {
        ArrayList<AllFileDTO> list = new ArrayList<>();
        try {
            FileInputStream fileInputStream = getActivity().openFileInput("KEY_NAME");
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            list = (ArrayList<AllFileDTO>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "Read: ", e);
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private ArrayList<AllFileDTO> loadFiles() {
        ArrayList<AllFileDTO> list = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory().toString();
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: " + files.length);
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && (file.getName().endsWith(".pptx") || file.getName().endsWith(".docx") || file.getName().endsWith(".txt") || file.getName().endsWith(".xlsx") || file.getName().endsWith(".pdf"))) {
                    String filePath = file.getAbsolutePath();
                    Log.d("Files", "File path: " + filePath);
                    String fileName = file.getName();
                    String dateModified = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.lastModified()));
//                 Thêm một đối tượng FileItem vào danh sách
//                    if (file.getName().endsWith(".pptx")) {
//                        list.add(new AllFileDTO(R.drawable.ppt_icon, fileName, dateModified, filePath, 0));
//                    } else if (file.getName().endsWith(".docx")) {
//                        list.add(new AllFileDTO(R.drawable.word_icon, fileName, dateModified, filePath, 0));
//                    } else if (file.getName().endsWith(".txt")) {
//                        list.add(new AllFileDTO(R.drawable.txt_icon, fileName, dateModified, filePath, 0));
//                    } else if (file.getName().endsWith(".xlsx")) {
//                        list.add(new AllFileDTO(R.drawable.excel_icon, fileName, dateModified, filePath, 0));
//                    } else
                        if (file.getName().endsWith(".pdf")) {
                        list.add(new AllFileDTO(R.drawable.icon_ppt2, fileName, dateModified, filePath, 0));
                    }
                }
            }
        }
        return list;
    }
    

}

