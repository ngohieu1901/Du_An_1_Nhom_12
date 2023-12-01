package com.example.du_an_1_nhom_12.ADAPTER;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.du_an_1_nhom_12.Activity.PdfViewActivity;
import com.example.du_an_1_nhom_12.DATABASE.FileDATABASE;
import com.example.du_an_1_nhom_12.DTO.AllFileDTO;
import com.example.du_an_1_nhom_12.R;
import com.example.du_an_1_nhom_12.SUPPORT.OnSingleClickListener;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class HomeADAPTER extends RecyclerView.Adapter<HomeADAPTER.ViewHolder> implements Filterable {
    Context context;
    ArrayList<AllFileDTO> list_home, list_file_old, list_bookmark;
    public boolean isActivityOpen = false;
    PopupWindow popupWindow;

    public HomeADAPTER(Context context, ArrayList<AllFileDTO> list) {
        this.context = context;
        this.list_home = list;
        this.list_file_old = list;
    }

    public void setData(ArrayList<AllFileDTO> list){
        this.list_home = list;
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
        AllFileDTO allFileDTO = list_home.get(position);
        holder.img_icon_file.setImageResource(allFileDTO.getHinh());
        holder.tv_ten_file.setText(allFileDTO.getTen());
        holder.tv_ngay.setText(allFileDTO.getNgay());
//        if (allFileDTO.getTen().endsWith(".txt")){
//            allFileDTO.setHinh(R.drawable.txt_icon);
//        }else if (allFileDTO.getTen().endsWith(".docx")){
//            allFileDTO.setHinh(R.drawable.word_icon);
//        }else if (allFileDTO.getTen().endsWith(".xlsx")){
//            allFileDTO.setHinh(R.drawable.excel_icon);
//        }else if (allFileDTO.getTen().endsWith(".pptx")){
//            allFileDTO.setHinh(R.drawable.pdf_icon);
//        }
        holder.itemView.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if(!isActivityOpen){
                    isActivityOpen = true;
                    String path = list_home.get(position).getPath();
                    String name = list_home.get(position).getTen();
                    Intent intent = new Intent(context, PdfViewActivity.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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

        if (allFileDTO.getBookmark() == 0) {
            holder.bookmark_file.setImageResource(R.drawable.star);
        } else {
            holder.bookmark_file.setImageResource(R.drawable.ic_star_gold);
        }


        holder.menu_custom.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // showPopupMenu(holder.menu_custom);
                setPopupWindow();
                popupWindow.showAsDropDown(view,-50,0);
            }
            public void setPopupWindow(){
                LayoutInflater inflater = (LayoutInflater)
                        context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.menu_popup, null);
                LinearLayout lnRename = view.findViewById(R.id.layout_rename);
                LinearLayout lnBookmark = view.findViewById(R.id.layout_bookmark);
                LinearLayout lnShare = view.findViewById(R.id.layout_menu_share);
                LinearLayout lnDelete = view.findViewById(R.id.layout_delete);
//RENAMEEEEEEEEEEEEEEEEE
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
                                String path = allFileDTO.getPath();

                                File oldFile = new File(path);
                                Log.d("ZZZZZ","Path old: "+path);
                                File newFile = new File("/storage/emulated/0/"+ed_ten.getText().toString());
                                Log.d("ZZZZZ","Path new: "+ "/storage/emulated/0/"+ed_ten.getText().toString());

                                if (oldFile.exists()) {
                                    boolean success = oldFile.renameTo(new File("/storage/emulated/0/"+ed_ten.getText().toString()));
                                    if (success) {
                                        allFileDTO.setTen(ed_ten.getText().toString());
                                        allFileDTO.setPath("/storage/emulated/0/"+ed_ten.getText().toString());
                                        list_home.set(position, allFileDTO);
                                        notifyItemChanged(position);
                                        saveFile(list_home);

                                        AllFileDTO fileDTO = null;
                                        list_bookmark = (ArrayList<AllFileDTO>) FileDATABASE.getInstance(context).fileDAO().getListFile();
                                        for (AllFileDTO file : list_bookmark) {
                                            if (file.getPath().equals(path)) {
                                                fileDTO = file;
                                                break;
                                            }
                                        }
                                        if (fileDTO != null) {
                                            fileDTO.setTen(ed_ten.getText().toString());
                                            fileDTO.setPath("/storage/emulated/0/"+ed_ten.getText().toString());
                                            FileDATABASE.getInstance(context).fileDAO().updateFile(fileDTO);
                                        }
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
//BOOKMARKKKKKKKKKKKKKKK
                lnBookmark.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View view) {
                        popupWindow.dismiss();

                        int so = allFileDTO.getBookmark();
                        if (so == 0) {
                            allFileDTO.setBookmark(1);
                            list_home.set(position, allFileDTO);
                            saveFile(list_home);

                            int hinh = list_home.get(position).getHinh();
                            String ten = list_home.get(position).getTen();
                            String ngay = list_home.get(position).getNgay();
                            int bookmark = list_home.get(position).getBookmark();
                            String path = list_home.get(position).getPath();
                            AllFileDTO fileDTO = new AllFileDTO(hinh, ten, ngay, path, bookmark);
                            FileDATABASE.getInstance(context).fileDAO().insertFile(fileDTO);
                            loadFile();
                            holder.bookmark_file.setImageResource(R.drawable.ic_star_gold);
                        } else {
                            allFileDTO.setBookmark(0);
                            list_home.set(position, allFileDTO);
                            saveFile(list_home);

                            String path = list_home.get(position).getPath();
                            list_bookmark = (ArrayList<AllFileDTO>) FileDATABASE.getInstance(context).fileDAO().getListFile();
                            FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                            holder.bookmark_file.setImageResource(R.drawable.star);
                            notifyDataSetChanged();
                        }

                    }
                });
//DELETEEEEEEEEEEEEE
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
                                String path = allFileDTO.getPath();
                                File file = new File(path);
                                if (file.exists()) {
                                    Log.d("ZZZZZ", "Path: " + file.getAbsolutePath());
                                    if (file.delete()) {
                                        list_home.remove(position);
                                        saveFile(list_home);

                                        list_bookmark = (ArrayList<AllFileDTO>) FileDATABASE.getInstance(context).fileDAO().getListFile();
                                        FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                                        notifyDataSetChanged();
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
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("application/pdf");
                        File pdfFile = new File(allFileDTO.getPath());
                        Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", pdfFile);
                        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Để cấp quyền đọc cho ứng dụng khác
                        context.startActivity(Intent.createChooser(intent, "Chia sẻ tài liệu PDF"));

                    }
                });
                popupWindow = new PopupWindow(view,500,LinearLayout.LayoutParams.WRAP_CONTENT,true);
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
                            btn_agree.setOnClickListener(new OnSingleClickListener() {
                                @Override
                                public void onSingleClick(View view) {
                                    String path = allFileDTO.getPath();

                                    File oldFile = new File(path);
                                    Log.d("ZZZZZ","Path old: "+path);
                                    File newFile = new File("/storage/emulated/0/"+ed_ten.getText().toString());
                                    Log.d("ZZZZZ","Path new: "+ "/storage/emulated/0/"+ed_ten.getText().toString());

                                    if (oldFile.exists()) {
                                        boolean success = oldFile.renameTo(new File("/storage/emulated/0/"+ed_ten.getText().toString()));
                                        if (success) {
                                            allFileDTO.setTen(ed_ten.getText().toString());
                                            allFileDTO.setPath("/storage/emulated/0/"+ed_ten.getText().toString());
                                            list_home.set(position, allFileDTO);
                                            notifyItemChanged(position);
                                            saveFile(list_home);

                                            AllFileDTO fileDTO = null;
                                            list_bookmark = (ArrayList<AllFileDTO>) FileDATABASE.getInstance(context).fileDAO().getListFile();
                                            for (AllFileDTO file : list_bookmark) {
                                                if (file.getPath().equals(path)) {
                                                    fileDTO = file;
                                                    break;
                                                }
                                            }
                                            if (fileDTO != null) {
                                                fileDTO.setTen(ed_ten.getText().toString());
                                                fileDTO.setPath("/storage/emulated/0/"+ed_ten.getText().toString());
                                                FileDATABASE.getInstance(context).fileDAO().updateFile(fileDTO);
                                            }
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
//BOOKMARK MENU
                        } else if (item.getItemId() == R.id.bookmark) {
                            int so = allFileDTO.getBookmark();

                            if (so == 0) {
                                allFileDTO.setBookmark(1);
                                list_home.set(position, allFileDTO);
                                saveFile(list_home);

                                int hinh = list_home.get(position).getHinh();
                                String ten = list_home.get(position).getTen();
                                String ngay = list_home.get(position).getNgay();
                                int bookmark = list_home.get(position).getBookmark();
                                String path = list_home.get(position).getPath();
                                AllFileDTO fileDTO = new AllFileDTO(hinh, ten, ngay, path, bookmark);
                                FileDATABASE.getInstance(context).fileDAO().insertFile(fileDTO);
                                loadFile();
                                holder.bookmark_file.setImageResource(R.drawable.ic_star_gold);
                            } else {
                                allFileDTO.setBookmark(0);
                                list_home.set(position, allFileDTO);
                                saveFile(list_home);

                                String path = list_home.get(position).getPath();
                                list_bookmark = (ArrayList<AllFileDTO>) FileDATABASE.getInstance(context).fileDAO().getListFile();
                                FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                                holder.bookmark_file.setImageResource(R.drawable.star);
                                notifyDataSetChanged();
                            }

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
                            btn_delete.setOnClickListener(new OnSingleClickListener() {
                                @Override
                                public void onSingleClick(View view) {
                                    String path = allFileDTO.getPath();
                                    File file = new File(path);
                                    if (file.exists()) {
                                        Log.d("ZZZZZ", "Path: " + file.getAbsolutePath());
                                        if (file.delete()) {
                                            list_home.remove(position);
                                            saveFile(list_home);

                                            list_bookmark = (ArrayList<AllFileDTO>) FileDATABASE.getInstance(context).fileDAO().getListFile();
                                            FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                                            notifyDataSetChanged();
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
                int so = allFileDTO.getBookmark();
                xuLyChonHOME(allFileDTO, so);
                Toast.makeText(context, context.getString(R.string.toast_bookmark), Toast.LENGTH_SHORT).show();
            }

            public void addFile() {
                int hinh = list_home.get(position).getHinh();
                String ten = list_home.get(position).getTen();
                String ngay = list_home.get(position).getNgay();
                int bookmark = list_home.get(position).getBookmark();
                String path = list_home.get(position).getPath();

                AllFileDTO fileDTO = new AllFileDTO(hinh, ten, ngay, path, bookmark);
                FileDATABASE.getInstance(context).fileDAO().insertFile(fileDTO);
            }

            public void xuLyChonHOME(AllFileDTO dto, int so) {
                if (so == 0) {
                    dto.setBookmark(1);
                    list_home.set(position, dto);
                    saveFile(list_home);

                    addFile();
                    loadFile();
                    holder.bookmark_file.setImageResource(R.drawable.ic_star_gold);
                } else {
                    dto.setBookmark(0);
                    list_home.set(position, dto);
                    saveFile(list_home);

                    String path = list_home.get(position).getPath();
                    list_bookmark = (ArrayList<AllFileDTO>) FileDATABASE.getInstance(context).fileDAO().getListFile();
                    FileDATABASE.getInstance(context).fileDAO().deleteByPath(path);
                    holder.bookmark_file.setImageResource(R.drawable.star);
                    notifyDataSetChanged();
                }
            }
        });
    }

    public void loadFile() {
        FileDATABASE.getInstance(context).fileDAO().getListFile();
        notifyDataSetChanged();
    }

    public void saveFile(ArrayList<AllFileDTO> list) {
        try {
            FileOutputStream fileOutputStream = ((Activity) context).openFileOutput("KEY_NAME", ((Activity) context).MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(list);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "Save: ", e);
        }
    }

    public ArrayList readFile() {
        ArrayList<AllFileDTO> list = new ArrayList<>();
        try {
            FileInputStream fileInputStream = ((Activity) context).openFileInput("KEY_NAME");
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
    public int getItemCount() {
        return list_home.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    list_home = list_file_old;
                } else {
                    ArrayList<AllFileDTO> list_new = new ArrayList<>();
                    for (AllFileDTO file : list_file_old) {
                        if (file.getTen().toLowerCase().contains(strSearch.toLowerCase())) {
                            list_new.add(file);
                        }
                    }
                    list_home = list_new;
                }
                FilterResults results = new FilterResults();
                results.values = list_home;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list_home = (ArrayList<AllFileDTO>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_icon_file;
        TextView tv_ten_file;
        TextView tv_ngay;
        ImageView bookmark_file;
        ImageButton menu_custom;
        LinearLayout container;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon_file = itemView.findViewById(R.id.img_icon);
            tv_ten_file = itemView.findViewById(R.id.tv_ten_file);
            tv_ngay = itemView.findViewById(R.id.tv_ngay);
            bookmark_file = itemView.findViewById(R.id.bookmark_file);
            menu_custom = itemView.findViewById(R.id.menu_custom);
            container = itemView.findViewById(R.id.container);
            tv_ten_file.setSelected(true);
        }
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
    }

}
