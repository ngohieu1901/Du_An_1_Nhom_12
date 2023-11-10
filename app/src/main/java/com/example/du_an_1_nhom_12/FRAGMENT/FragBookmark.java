package com.example.du_an_1_nhom_12.FRAGMENT;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1_nhom_12.ADAPTER.BookmarkADAPTER;
import com.example.du_an_1_nhom_12.DATABASE.FileDATABASE;
import com.example.du_an_1_nhom_12.DTO.AllFileDTO;
import com.example.du_an_1_nhom_12.R;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FragBookmark extends Fragment {
    LinearLayout layoutAZ,layoutZA;
    RecyclerView rc_bookmark;
    BookmarkADAPTER adapter;
    ArrayList<AllFileDTO> mlistFile,list_sort;
    ImageButton sortFile;
    EditText search_file;
    ImageView iv_clear;
    int sortState = 0;
    private static final String SAVE_INT = "SAVE_INT";
    private static final String INT_VALUE = "INT_VALUE";


    private void saveInt(int value){
        SharedPreferences sharedPreference = requireContext().getSharedPreferences(SAVE_INT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.putInt(INT_VALUE, value);
        editor.apply();
    }

    private int getInt(){
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(SAVE_INT,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(INT_VALUE, 0 );
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_bookmark,container,false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences preferences = getActivity().getSharedPreferences("SAVE_DIALOG", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        mlistFile =  (ArrayList<AllFileDTO>) FileDATABASE.getInstance(getContext()).fileDAO().getListFile();
        adapter = new BookmarkADAPTER(getContext(),mlistFile);
        rc_bookmark = view.findViewById(R.id.rc_bookmark);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        rc_bookmark.setLayoutManager(linearLayoutManager);
        rc_bookmark.setAdapter(adapter);

        sortState = getInt();
        Log.d("ZZZZZ", "sort: "+ sortState);
        switch (sortState){
            case 0: {
                // không sắp xếp
                Log.d("ZZZZZ","Case 0" );
                break;
            }
            case 1: {
                // sắp xêp a -> z
                Collections.sort(mlistFile, new Comparator<AllFileDTO>() {
                    @Override
                    public int compare(AllFileDTO o1, AllFileDTO o2) {
                        return o1.getTen().compareTo(o2.getTen());
                    }
                });
                adapter.notifyDataSetChanged();
                Log.d("ZZZZZ","Case 1" );
                break;
            }
            case 2: {
                // sắp xếp z -> a
                Collections.sort(mlistFile, new NameComparator());
                adapter.notifyDataSetChanged();
                Log.d("ZZZZZ","Case 2" );
                break;
            }
        }


        sortFile = view.findViewById(R.id.sort_file_bm);
        sortFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = getLayoutInflater().inflate(R.layout.dialog_sort,null,false);
                builder.setView(view);
                Dialog dialog = builder.create();
                dialog.show();
                layoutAZ = view.findViewById(R.id.layout_az);
                layoutZA = view.findViewById(R.id.layout_za);
                ImageView check_az= view.findViewById(R.id.check_az);
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

                        Collections.sort(mlistFile, new Comparator<AllFileDTO>() {
                            @Override
                            public int compare(AllFileDTO o1, AllFileDTO o2) {
                                return o1.getTen().toLowerCase().compareTo(o2.getTen().toLowerCase());
                            }
                        });

                        Toast.makeText(getContext(), getContext().getString(R.string.toast_sortAZ), Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();
                        sortState = 1;
                        saveInt(sortState);
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

                        Collections.sort(mlistFile, new NameComparator());
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(),  getContext().getString(R.string.toast_sortZA), Toast.LENGTH_SHORT).show();
                        sortState = 2;
                        saveInt(sortState);
                    }
                });
            }
        });

        search_file = view.findViewById(R.id.search_file_bm);
        iv_clear = view.findViewById(R.id.iv_clear);
        search_file.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
                if(s.length() == 0){
                    iv_clear.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm!= null) {
                        imm.hideSoftInputFromWindow(search_file.getWindowToken(),0);
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

    }
    public class NameComparator implements Comparator<AllFileDTO> {
        @Override
        public int compare(AllFileDTO o1, AllFileDTO o2) {
            return o2.getTen().toLowerCase().compareTo(o1.getTen().toLowerCase());
        }
    }
}
