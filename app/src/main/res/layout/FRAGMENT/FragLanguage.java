package com.example.project_1.FRAGMENT;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_1.ADAPTER.LanguageFragADAPTER;
import com.example.project_1.Activity.MainManageFile;
import com.example.project_1.DTO.LanguageFragDTO;
import com.example.project_1.R;

import java.util.ArrayList;
import java.util.List;

public class FragLanguage extends Fragment {
    RecyclerView rc_lang;
    SharedPreferences preferences;
    LanguageFragADAPTER adapter;
    List<LanguageFragDTO> list;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_language,container,false);

        list = new ArrayList<>();
        list.add(new LanguageFragDTO(false,"English"));
        list.add(new LanguageFragDTO(false,"Korean"));
        list.add(new LanguageFragDTO(false,"Japanese"));
        list.add(new LanguageFragDTO(false,"French"));
        list.add(new LanguageFragDTO(false,"Hindi"));
        list.add(new LanguageFragDTO(false,"Portuguese"));
        list.add(new LanguageFragDTO(false,"Spanish"));
        list.add(new LanguageFragDTO(false,"Vietnamese"));
        list.add(new LanguageFragDTO(false,"Phillipinese"));
        list.add(new LanguageFragDTO(false,"German"));

        adapter = new LanguageFragADAPTER(getContext(),list);

        preferences  = getActivity().getSharedPreferences("pref",Context.MODE_PRIVATE);
        int selectedPosition = preferences.getInt("selected_position",-1);
        if(selectedPosition != -1){
            adapter.setSelectedItem(selectedPosition);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rc_lang = view.findViewById(R.id.rc_lang);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);
        rc_lang.setLayoutManager(linearLayoutManager);
        rc_lang.setAdapter(adapter);

        ImageView iv_back = view .findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainManageFile.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("selected_position",adapter.getSelectedItem());
        editor.apply();
    }
}
