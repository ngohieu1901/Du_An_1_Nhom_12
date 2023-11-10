package com.example.project_1.ADAPTER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_1.DTO.LanguageDTO;
import com.example.project_1.R;
import com.example.project_1.SUPPORT.LanguageStatic;

import java.util.ArrayList;

public class LanguageADAPTER extends RecyclerView.Adapter<LanguageADAPTER.ViewHolder> {
    Context context;
    ArrayList<LanguageDTO> list_lang;
    int selectedPosition = -1;
    int row_index = -1;
    private OnItemClickListener onItemClickListener;


    public LanguageADAPTER(Context context, ArrayList<LanguageDTO> list_lang, OnItemClickListener clickListener) {
        this.context = context;
        this.list_lang = list_lang;
        this.onItemClickListener = clickListener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.img_hinh.setImageResource(list_lang.get(position).getHinh());
        holder.tv_ten.setText(list_lang.get(position).getTen());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick", "onClick: " + position);
                if (position == 0){
                    LanguageStatic.setLanguage(v.getContext(),"en");
                    onItemClickListener.onItemClick(position);
                } else if(position == 1){
                    LanguageStatic.setLanguage(v.getContext(), "sp");
                    onItemClickListener.onItemClick(position);
                } else if (position == 2){
                    Toast.makeText(context, "      Language not supported\nplease select another language", Toast.LENGTH_SHORT).show();
                    onItemClickListener.onItemClick(position);
                } else if (position == 3){
                    onItemClickListener.onItemClick(position);
                    Toast.makeText(context, "      Language not supported\nplease select another language", Toast.LENGTH_SHORT).show();
                }
                else if (position == 4) {
                    LanguageStatic.setLanguage(v.getContext(),"fr");
                    onItemClickListener.onItemClick(position);
                }
                row_index = position;
                selectedPosition = position;
                notifyDataSetChanged();
            }
        });

        holder.radioButton.setChecked(position == selectedPosition);

        if(row_index == position){
            holder.layout.setBackgroundColor(Color.parseColor("#217346"));
            holder.tv_ten.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.tv_ten.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return list_lang.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView tv_ten;
        ImageView img_hinh;
        LinearLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.rb_selected);
            tv_ten = itemView.findViewById(R.id.tv_ten);
            img_hinh = itemView.findViewById(R.id.img_hinh);
            layout = itemView.findViewById(R.id.frame_1);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
