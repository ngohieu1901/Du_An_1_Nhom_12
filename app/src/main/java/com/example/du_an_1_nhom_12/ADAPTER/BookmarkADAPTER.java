package com.example.du_an_1_nhom_12.ADAPTER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1_nhom_12.Activity.PdfViewActivity;
import com.example.du_an_1_nhom_12.DATABASE.FileDATABASE;
import com.example.du_an_1_nhom_12.DTO.AllFileDTO;
import com.example.du_an_1_nhom_12.R;
import com.example.du_an_1_nhom_12.SUPPORT.OnSingleClickListener;

import java.io.File;
import java.util.ArrayList;

public class BookmarkADAPTER extends RecyclerView.Adapter<BookmarkADAPTER.ViewHolder> implements Filterable {
    Context context;
    ArrayList<AllFileDTO> list_bookmark,list_home,list_file_old;
    HomeADAPTER adapter;
    boolean isActivityOpen = false;
    PopupWindow popupWindow;
    public BookmarkADAPTER(Context context, ArrayList<AllFileDTO> list) {
        this.context = context;
        this.list_bookmark = list;
        this.list_file_old = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final AllFileDTO allFileDTO = list_bookmark.get(position);

        holder.img_icon_file.setImageResource(allFileDTO.getHinh());
        holder.tv_ten_file.setText(allFileDTO.getTen());
        holder.tv_ngay.setText(allFileDTO.getNgay());

        if (allFileDTO.getBookmark() == 0) {
            holder.bookmark_file.setImageResource(R.drawable.star);
        } else {
            holder.bookmark_file.setImageResource(R.drawable.ic_star_gold);
        }

        holder.itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if (!isActivityOpen) {
                    isActivityOpen = true;
                    String path = list_bookmark.get(position).getPath();
                    String name = list_bookmark.get(position).getTen();
                    Intent intent = new Intent(context, PdfViewActivity.class);
                    intent.putExtra("path", path);
                    intent.putExtra("name", name);
                    context.startActivity(intent);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isActivityOpen = false;
                        }
                    }, 1000);
                }
            }
        });

        holder.menu_custom.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
//                showPopupMenu(holder.menu_custom);
                setPopupWindow();
                popupWindow.showAsDropDown(view,-20,0);
            }
            public void setPopupWindow(){
                LayoutInflater inflater = (LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.menu_popup, null);
                LinearLayout lnRename = view.findViewById(R.id.layout_rename);
                LinearLayout lnBookmark = view.findViewById(R.id.layout_bookmark);
                LinearLayout lnShare = view.findViewById(R.id.layout_menu_share);
                LinearLayout lnDelete = view.findViewById(R.id.layout_delete);
                ImageView ivStarMenu = view.findViewById(R.id.iv_star_menu);
//UPDATEEEEEEEEEEEEEEEEEE
                lnRename.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View view) {
                        popupWindow.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                        View v = inflater.inflate(R.layout.dialog_rename, null);
                        builder.setView(v);
                        Dialog dialog = builder.create();
                        dialog.show();

                        EditText ed_ten = v.findViewById(R.id.ed_ten);
                        Button btn_cancel = v.findViewById(R.id.btn_cancel);
                        Button btn_agree = v.findViewById(R.id.btn_agree);

                        ed_ten.setText(allFileDTO.getTen());
                        btn_agree.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View view) {
                                adapter = new HomeADAPTER(context,list_home);
                                list_home = adapter.readFile();
                                String path = list_bookmark.get(position).getPath();

                                File oldFile = new File(path);
                                Log.d("ZZZZZ","Path old: "+path);
                                File newFile = new File("/storage/emulated/0/"+ed_ten.getText().toString());
                                Log.d("ZZZZZ","Path new: "+ "/storage/emulated/0/"+ed_ten.getText().toString());

                                if (oldFile.exists()) {
                                    boolean success = oldFile.renameTo(new File("/storage/emulated/0/"+ed_ten.getText().toString()));
                                    if (success) {
                                        int posHome = 0;
                                        for (int i = 0; i < list_home.size(); i++){
                                            AllFileDTO fileHome = list_home.get(i);
                                            if (fileHome.getPath().equals(path)){
                                                posHome = i;
                                                fileHome.setTen(ed_ten.getText().toString());
                                                fileHome.setPath("/storage/emulated/0/"+ed_ten.getText().toString());
                                                list_home.set(posHome,fileHome);
                                                adapter.saveFile(list_home);
                                                adapter.notifyDataSetChanged();
                                                break;
                                            }
                                        }
                                        allFileDTO.setTen(ed_ten.getText().toString());
                                        allFileDTO.setPath("/storage/emulated/0/"+ed_ten.getText().toString());
                                        FileDATABASE.getInstance(context).fileDAO().updateFile(allFileDTO);
                                        loadFile();
                                        Toast.makeText(context, context.getString(R.string.toast_rename), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.toast_failed), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, context.getString(R.string.toast_exists), Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });

                        btn_cancel.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });
//BOOKMARKKKKKKKKKKKKKK
                lnBookmark.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View view) {
                        popupWindow.dismiss();

                        adapter = new HomeADAPTER(context,list_home);
                        list_home = adapter.readFile();
                        String path = list_bookmark.get(position).getPath();

                        int posHome = 0;
                        for (int i = 0; i < list_home.size(); i++){
                            AllFileDTO fileHome = list_home.get(i);
                            if (fileHome.getPath().equals(path)){
                                posHome = i;
                                fileHome.setBookmark(0);
                                list_home.set(posHome,fileHome);
                                adapter.saveFile(list_home);
                                break;
                            }
                        }

                        list_bookmark.remove(position);
                        FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                        loadFile();
                        Toast.makeText(context, context.getString(R.string.toast_bookmark), Toast.LENGTH_SHORT).show();
                    }
                });
//DELETEEEEEEEEEEEEEEEEEE
                lnDelete.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View view) {
                        popupWindow.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                        View v = inflater.inflate(R.layout.dialog_delete, null);
                        builder.setView(v);
                        Dialog dialog = builder.create();
                        dialog.show();
                        Button btn_delete = v.findViewById(R.id.btn_delete);
                        Button btn_cancel = v.findViewById(R.id.btn_cancel);
                        btn_delete.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View view) {
                                adapter = new HomeADAPTER(context,list_home);
                                list_home = adapter.readFile();
                                String path = list_bookmark.get(position).getPath();

                                File file = new File(path);
                                if (file.exists()) {
                                    Log.d("ZZZZZ", "Path: " + file.getAbsolutePath());
                                    if (file.delete()) {
                                        int posHome = 0;
                                        for (int i = 0; i < list_home.size(); i++){
                                            AllFileDTO fileHome = list_home.get(i);
                                            if (fileHome.getPath().equals(path)){
                                                posHome = i;
                                                list_home.remove(posHome);
                                                adapter.saveFile(list_home);
                                                break;
                                            }
                                        }
                                        list_bookmark.remove(position);
                                        FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                                        loadFile();
                                        Toast.makeText(context, context.getString(R.string.toast_delete), Toast.LENGTH_SHORT).show();
                                    } else {
                                        context.deleteFile(file.getName());
                                        Toast.makeText(context, context.getString(R.string.toast_failed), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, context.getString(R.string.toast_exists), Toast.LENGTH_SHORT).show();
                                }

                                dialog.dismiss();
                            }
                        });
                        btn_cancel.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View view) {
                                dialog.dismiss();
                            }
                        });
                    }
                });

                lnShare.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View view) {
                        popupWindow.dismiss();


                    }
                });
                popupWindow = new PopupWindow(view,300,LinearLayout.LayoutParams.WRAP_CONTENT,true);
            }

            private void showPopupMenu(View view) {
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.rename) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            View v = inflater.inflate(R.layout.dialog_rename, null);
                            builder.setView(v);
                            Dialog dialog = builder.create();
                            dialog.show();

                            EditText ed_ten = v.findViewById(R.id.ed_ten);
                            Button btn_cancel = v.findViewById(R.id.btn_cancel);
                            Button btn_agree = v.findViewById(R.id.btn_agree);

                            ed_ten.setText(allFileDTO.getTen());
//UPDATE
                            btn_agree.setOnClickListener(new OnSingleClickListener() {
                                @Override
                                public void onSingleClick(View view) {
                                    adapter = new HomeADAPTER(context,list_home);
                                    list_home = adapter.readFile();
                                    String path = list_bookmark.get(position).getPath();

                                    File oldFile = new File(path);
                                    Log.d("ZZZZZ","Path old: "+path);
                                    File newFile = new File("/storage/emulated/0/"+ed_ten.getText().toString());
                                    Log.d("ZZZZZ","Path new: "+ "/storage/emulated/0/"+ed_ten.getText().toString());

                                    if (oldFile.exists()) {
                                        boolean success = oldFile.renameTo(new File("/storage/emulated/0/"+ed_ten.getText().toString()));
                                        if (success) {
                                            int posHome = 0;
                                            for (int i = 0; i < list_home.size(); i++){
                                                AllFileDTO fileHome = list_home.get(i);
                                                if (fileHome.getPath().equals(path)){
                                                    posHome = i;
                                                    fileHome.setTen(ed_ten.getText().toString());
                                                    fileHome.setPath("/storage/emulated/0/"+ed_ten.getText().toString());
                                                    list_home.set(posHome,fileHome);
                                                    adapter.saveFile(list_home);
                                                    adapter.notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                            allFileDTO.setTen(ed_ten.getText().toString());
                                            allFileDTO.setPath("/storage/emulated/0/"+ed_ten.getText().toString());
                                            FileDATABASE.getInstance(context).fileDAO().updateFile(allFileDTO);
                                            loadFile();
                                            Toast.makeText(context, context.getString(R.string.toast_rename), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.toast_failed), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.toast_exists), Toast.LENGTH_SHORT).show();
                                    }

                                    dialog.dismiss();
                                }
                            });

                            btn_cancel.setOnClickListener(new OnSingleClickListener() {
                                @Override
                                public void onSingleClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            return true;
                        } else if (item.getItemId() == R.id.bookmark) {
                            adapter = new HomeADAPTER(context,list_home);
                            list_home = adapter.readFile();
                            String path = list_bookmark.get(position).getPath();

                            int posHome = 0;
                            for (int i = 0; i < list_home.size(); i++){
                                AllFileDTO fileHome = list_home.get(i);
                                if (fileHome.getPath().equals(path)){
                                    posHome = i;
                                    fileHome.setBookmark(0);
                                    list_home.set(posHome,fileHome);
                                    adapter.saveFile(list_home);
                                    break;
                                }
                            }

                            list_bookmark.remove(position);
                            FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                            loadFile();
                            Toast.makeText(context, context.getString(R.string.toast_bookmark), Toast.LENGTH_SHORT).show();
                            return true;
                        } else if (item.getItemId() == R.id.delete) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                            View v = inflater.inflate(R.layout.dialog_delete, null);
                            builder.setView(v);
                            Dialog dialog = builder.create();
                            dialog.show();
                            Button btn_delete = v.findViewById(R.id.btn_delete);
                            Button btn_cancel = v.findViewById(R.id.btn_cancel);
//DELETE
                            btn_delete.setOnClickListener(new OnSingleClickListener() {
                                @Override
                                public void onSingleClick(View view) {
                                    adapter = new HomeADAPTER(context,list_home);
                                    list_home = adapter.readFile();
                                    String path = list_bookmark.get(position).getPath();

                                    File file = new File(path);
                                    if (file.exists()) {
                                        Log.d("ZZZZZ", "Path: " + file.getAbsolutePath());
                                        if (file.delete()) {
                                            int posHome = 0;
                                            for (int i = 0; i < list_home.size(); i++){
                                                AllFileDTO fileHome = list_home.get(i);
                                                if (fileHome.getPath().equals(path)){
                                                    posHome = i;
                                                    list_home.remove(posHome);
                                                    adapter.saveFile(list_home);
                                                    break;
                                                }
                                            }
                                            list_bookmark.remove(position);
                                            FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                                            loadFile();
                                            Toast.makeText(context, context.getString(R.string.toast_delete), Toast.LENGTH_SHORT).show();
                                        } else {
                                            context.deleteFile(file.getName());
                                            Toast.makeText(context, context.getString(R.string.toast_failed), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.toast_exists), Toast.LENGTH_SHORT).show();
                                    }

                                    dialog.dismiss();
                                }
                            });
                            btn_cancel.setOnClickListener(new OnSingleClickListener() {
                                @Override
                                public void onSingleClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        holder.bookmark_file.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                adapter = new HomeADAPTER(context,list_home);
                list_home = adapter.readFile();
                String path = list_bookmark.get(position).getPath();

                int posHome = 0;
                for (int i = 0; i < list_home.size(); i++){
                    AllFileDTO fileHome = list_home.get(i);
                    if (fileHome.getPath().equals(path)){
                        posHome = i;
                        fileHome.setBookmark(0);
                        list_home.set(posHome,fileHome);
                        adapter.saveFile(list_home);
                        break;
                    }
                }

                list_bookmark.remove(position);
                FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                loadFile();
                Toast.makeText(context, context.getString(R.string.toast_bookmark), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void loadFile(){
        FileDATABASE.getInstance(context).fileDAO().getListFile();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return list_bookmark.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    list_bookmark = list_file_old;
                } else {
                    ArrayList<AllFileDTO> list_new = new ArrayList<>();
                    for (AllFileDTO file : list_file_old) {
                        if (file.getTen().toLowerCase().contains(strSearch.toLowerCase())) {
                            list_new.add(file);
                        }
                    }
                    list_bookmark = list_new;
                }
                FilterResults results = new FilterResults();
                results.values = list_bookmark;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list_bookmark = (ArrayList<AllFileDTO>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_icon_file;
        TextView tv_ten_file;
        TextView tv_ngay;
        ImageView bookmark_file;
        ImageButton menu_custom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon_file = itemView.findViewById(R.id.img_icon);
            tv_ten_file = itemView.findViewById(R.id.tv_ten_file);
            tv_ngay = itemView.findViewById(R.id.tv_ngay);
            bookmark_file = itemView.findViewById(R.id.bookmark_file);
            menu_custom = itemView.findViewById(R.id.menu_custom);
        }
    }
}
