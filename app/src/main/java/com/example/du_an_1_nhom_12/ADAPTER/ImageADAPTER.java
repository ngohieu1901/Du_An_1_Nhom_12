package com.example.du_an_1_nhom_12.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.du_an_1_nhom_12.Activity.ImageViewActivity;
import com.example.du_an_1_nhom_12.DTO.ImageDTO;
import com.example.du_an_1_nhom_12.R;

import java.util.ArrayList;

public class ImageADAPTER extends RecyclerView.Adapter<ImageADAPTER.HolderImage> {

    private Context context;
    private ArrayList<ImageDTO> imageDTOArrayList;

    public ImageADAPTER(Context context, ArrayList<ImageDTO> imageDTOArrayList) {
        this.context = context;
        this.imageDTOArrayList = imageDTOArrayList;
    }

    @NonNull
    @Override
    public HolderImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_image, parent, false);
        return new HolderImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderImage holder, int position) {

        ImageDTO imageDTO = imageDTOArrayList.get(position);

        Uri imageUri = imageDTO.getImageUri();

        Glide.with(context)
                .load(imageUri)
                .placeholder(R.drawable.ic_image_black)
                .into(holder.imageIv);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ImageViewActivity.class);
                intent.putExtra("imageUri", ""+imageUri);
                context.startActivity(intent);
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                imageDTO.setChecked(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return imageDTOArrayList.size();
    }


    class HolderImage extends RecyclerView.ViewHolder {
        ImageView imageIv;
        CheckBox checkBox;
        public HolderImage(@NonNull View itemView) {
            super(itemView);
            imageIv = itemView.findViewById(R.id.imageIv);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }

}
