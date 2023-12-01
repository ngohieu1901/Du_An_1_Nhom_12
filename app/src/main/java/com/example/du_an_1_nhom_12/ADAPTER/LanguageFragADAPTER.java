package com.example.du_an_1_nhom_12.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1_nhom_12.DTO.LanguageFragDTO;
import com.example.du_an_1_nhom_12.R;
import com.example.du_an_1_nhom_12.SUPPORT.LanguageStatic;
import com.example.du_an_1_nhom_12.SUPPORT.OnSingleClickListener;


import java.util.List;

public class LanguageFragADAPTER extends RecyclerView.Adapter<LanguageFragADAPTER.ViewHolder>{
    Context context;
    List<LanguageFragDTO> list;
    int selectedItem = -1;

    public LanguageFragADAPTER(Context context, List<LanguageFragDTO> list) {
        this.context = context;
        this.list = list;
    }

    public int getSelectedItem() {
        return selectedItem;
    }
    public void setSelectedItem(int position){
        selectedItem = position;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frag_lang,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_ten.setText(list.get(position).getTen());
        // kiểm tra position có được chọn hay không
        if (position == selectedItem){// selectedItem = -1, nếu position được chọn -> hiển thị tv_CHECK
            holder.iv_check.setVisibility(View.VISIBLE);
        }else {
            holder.iv_check.setVisibility(View.GONE);
        }
        holder.layout.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Log.e("onClick", "onClick: " + position);
                if (position == 0){
                    LanguageStatic.setLanguage(context,"en");
                }else if(position == 1){
                    LanguageStatic.setLanguage(context,"jp");
                }else if(position == 2){
                    LanguageStatic.setLanguage(context,"fr");
                }else if(position == 3){
                    LanguageStatic.setLanguage(context,"hi");
                } else if (position == 4) {
                    LanguageStatic.setLanguage(context,"sp");
                }else {
                    Toast.makeText(context, context.getString(R.string.toast_lang), Toast.LENGTH_SHORT).show();
                }

                int preSelected = selectedItem;// Lưu vị trí được chọn trước đó
                selectedItem = position;// Đặt lại vị trí được chọn = vị trí hiện tại
                notifyItemChanged(preSelected);
                notifyItemChanged(selectedItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_ten;
        ImageView iv_check;
        CardView card_lang;
        LinearLayout layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_ten = itemView.findViewById(R.id.tv_ten);
            iv_check = itemView.findViewById(R.id.iv_check);
            card_lang = itemView.findViewById(R.id.card_lang);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
