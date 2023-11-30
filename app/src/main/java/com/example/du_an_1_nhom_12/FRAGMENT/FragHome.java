package com.example.du_an_1_nhom_12.FRAGMENT;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


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

public class FragHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView rc_file;
    HomeADAPTER adapter;
    EditText search_file;
    ImageView iv_clear;
    ImageButton sortFile;
    LinearLayout layoutAZ, layoutZA;
    SwipeRefreshLayout srl_home;
    ArrayList<AllFileDTO> list = new ArrayList<>();
    String TAG = "ok";
    PopupWindow popupWindow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        srl_home = (SwipeRefreshLayout) view.findViewById(R.id.srl_home);
        srl_home.setOnRefreshListener(this);
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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(search_file.getWindowToken(), 0);
                }
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
                } else {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.BorderDialogTheme);
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
                        iv_az.setImageResource(R.drawable.ic_sort_red);
                        tv_az.setTextColor(Color.parseColor("#CD3527"));
                        iv_za.setImageResource(R.drawable.sort_az_white);
                        tv_za.setTextColor(Color.parseColor("#FF000000"));
                        // set SharedPreferences
                        editor.putBoolean("check_az_visible", true);
                        editor.putBoolean("check_za_visible", false);
                        editor.putInt("iv_az_resource", R.drawable.ic_sort_red);
                        editor.putString("tv_az_color", "#CD3527");
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
                        iv_za.setImageResource(R.drawable.ic_sort_red);
                        tv_za.setTextColor(Color.parseColor("#CD3527"));
                        iv_az.setImageResource(R.drawable.sort_az_white);
                        tv_az.setTextColor(Color.parseColor("#FF000000"));
                        // Lưu các giá trị vào SharedPreferences
                        editor.putBoolean("check_az_visible", false);
                        editor.putBoolean("check_za_visible", true);
                        editor.putInt("iv_az_resource", R.drawable.sort_az_white);
                        editor.putString("tv_az_color", "#FF000000");
                        editor.putInt("iv_za_resource", R.drawable.ic_sort_red);
                        editor.putString("tv_za_color", "#CD3527");
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

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "Refresh", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<AllFileDTO> existingList = readFile(); // Đọc danh sách file đã có từ FileInputStream
                ArrayList<AllFileDTO> newList = loadFiles(); // Tải danh sách file mới từ thiết bị

                // Kiểm tra từng file trong danh sách mới tải về
                for (AllFileDTO newFile : newList) {
                    boolean fileExists = false;
                    // So sánh đường dẫn của file mới với danh sách file đã có
                    for (AllFileDTO existingFile : existingList) {
                        if (newFile.getPath().equals(existingFile.getPath())) {
                            fileExists = true;
                            break; // File đã tồn tại trong danh sách, không cần thêm vào lại
                        }
                    }
                    // Nếu file không tồn tại trong danh sách cũ, thêm nó vào danh sách
                    if (!fileExists) {
                        existingList.add(newFile);
                        Log.d("zzzzz","file: "+newFile.getTen());
                    }
                }
                // Lưu danh sách mới vào FileInputStream
                saveFile(existingList);
                list = readFile();
                adapter.setData(list);
                srl_home.setRefreshing(false);
            }
        }, 2000);
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
                    if (file.getName().endsWith(".pdf")) {
                        list.add(new AllFileDTO(R.drawable.icon_ppt2, fileName, dateModified, filePath, 0));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }
}

