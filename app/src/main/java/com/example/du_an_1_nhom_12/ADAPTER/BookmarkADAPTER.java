package com.example.du_an_1_nhom_12.ADAPTER;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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
import java.util.ArrayList;

public class BookmarkADAPTER extends RecyclerView.Adapter<BookmarkADAPTER.ViewHolder> implements Filterable {
    Context context;
    ArrayList<AllFileDTO> list_bookmark, list_home, list_file_old;
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
                int[] values = new int[2];
                holder.menu_custom.getLocationInWindow(values);
                int positionOfIcon = values[1];// lay toa do truc Y
                DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
                int height = displayMetrics.heightPixels * 2 / 3;
                if (positionOfIcon > height) {
                    popupWindow.showAsDropDown(view, -22, -(holder.menu_custom.getHeight() * 7), Gravity.BOTTOM | Gravity.END);
                } else {
                    popupWindow.showAsDropDown(view, -22, 0, Gravity.TOP | Gravity.END);
                }
            }

            public void setPopupWindow() {
                LayoutInflater inflater = (LayoutInflater) context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.menu_popup, null);
                LinearLayout lnRename = view.findViewById(R.id.layout_rename);
                LinearLayout lnBookmark = view.findViewById(R.id.layout_bookmark);
                LinearLayout lnShare = view.findViewById(R.id.layout_menu_share);
                LinearLayout lnDelete = view.findViewById(R.id.layout_delete);
                TextView tvRename,tvBookmark,tvShare,tvDelete;
                tvRename  = view.findViewById(R.id.tv_rename);
                tvDelete  = view.findViewById(R.id.tv_delete);
                tvBookmark  = view.findViewById(R.id.tv_bookmark);
                tvShare  = view.findViewById(R.id.tv_share);
                tvRename.setText(context.getString(R.string.rename_popup));
                tvDelete.setText(context.getString(R.string.delete_popup));
                tvShare.setText(context.getString(R.string.tv_share));
                tvBookmark.setText(context.getString(R.string.bookmark_popup));
                popupWindow = new PopupWindow(view, (int) context.getResources().getDimension(com.intuit.sdp.R.dimen._130sdp), ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setElevation(10);

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

                        String name = allFileDTO.getTen();
                        if (name.indexOf(".") > 0) {
                            name = name.substring(0, name.lastIndexOf("."));
                        }
                        ed_ten.setText(name);

                        btn_agree.setOnClickListener(new OnSingleClickListener() {
                            @Override
                            public void onSingleClick(View view) {
                                adapter = new HomeADAPTER(context, list_home);
                                list_home = adapter.readFile();
                                String path = list_bookmark.get(position).getPath();

                                File oldFile = new File(path);
                                Log.d("ZZZZZ", "Path old: " + path);
                                File newFile = new File("/storage/emulated/0/" + ed_ten.getText().toString() + ".pdf");
                                Log.d("ZZZZZ", "Path new: " + "/storage/emulated/0/" + ed_ten.getText().toString() + ".pdf");

                                if (oldFile.exists()) {
                                    if (ed_ten.getText().toString().isEmpty()) {
                                        Toast.makeText(context, "Please enter name file", Toast.LENGTH_SHORT).show();
                                    } else {
                                        boolean success = oldFile.renameTo(new File("/storage/emulated/0/" + ed_ten.getText().toString() + ".pdf"));
                                        if (success) {
                                            int posHome = 0;
                                            for (int i = 0; i < list_home.size(); i++) {
                                                AllFileDTO fileHome = list_home.get(i);
                                                if (fileHome.getPath().equals(path)) {
                                                    posHome = i;
                                                    fileHome.setTen(ed_ten.getText().toString() + ".pdf");
                                                    fileHome.setPath("/storage/emulated/0/" + ed_ten.getText().toString() + ".pdf");
                                                    list_home.set(posHome, fileHome);
                                                    adapter.saveFile(list_home);
                                                    adapter.notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                            allFileDTO.setTen(ed_ten.getText().toString() + ".pdf");
                                            allFileDTO.setPath("/storage/emulated/0/" + ed_ten.getText().toString() + ".pdf");
                                            FileDATABASE.getInstance(context).fileDAO().updateFile(allFileDTO);
                                            loadFile();
                                            dialog.dismiss();
                                            Toast.makeText(context, context.getString(R.string.toast_rename), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, context.getString(R.string.toast_failed), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                } else {
                                    Toast.makeText(context, context.getString(R.string.toast_exists), Toast.LENGTH_SHORT).show();
                                }
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

                        adapter = new HomeADAPTER(context, list_home);
                        list_home = adapter.readFile();
                        String path = list_bookmark.get(position).getPath();

                        int posHome = 0;
                        for (int i = 0; i < list_home.size(); i++) {
                            AllFileDTO fileHome = list_home.get(i);
                            if (fileHome.getPath().equals(path)) {
                                posHome = i;
                                fileHome.setBookmark(0);
                                list_home.set(posHome, fileHome);
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
                                adapter = new HomeADAPTER(context, list_home);
                                list_home = adapter.readFile();
                                String path = list_bookmark.get(position).getPath();

                                File file = new File(path);
                                if (file.exists()) {
                                    Log.d("ZZZZZ", "Path: " + file.getAbsolutePath());
                                    if (file.delete()) {
                                        int posHome = 0;
                                        for (int i = 0; i < list_home.size(); i++) {
                                            AllFileDTO fileHome = list_home.get(i);
                                            if (fileHome.getPath().equals(path)) {
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
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("application/pdf");
                        File pdfFile = new File(allFileDTO.getPath());
                        Uri pdfUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", pdfFile);
                        intent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Để cấp quyền đọc cho ứng dụng khác
                        context.startActivity(Intent.createChooser(intent, "Chia sẻ tài liệu PDF"));

                    }
                });
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
                                    adapter = new HomeADAPTER(context, list_home);
                                    list_home = adapter.readFile();
                                    String path = list_bookmark.get(position).getPath();

                                    File oldFile = new File(path);
                                    Log.d("ZZZZZ", "Path old: " + path);
                                    File newFile = new File("/storage/emulated/0/" + ed_ten.getText().toString());
                                    Log.d("ZZZZZ", "Path new: " + "/storage/emulated/0/" + ed_ten.getText().toString());

                                    if (oldFile.exists()) {
                                        boolean success = oldFile.renameTo(new File("/storage/emulated/0/" + ed_ten.getText().toString()));
                                        if (success) {
                                            int posHome = 0;
                                            for (int i = 0; i < list_home.size(); i++) {
                                                AllFileDTO fileHome = list_home.get(i);
                                                if (fileHome.getPath().equals(path)) {
                                                    posHome = i;
                                                    fileHome.setTen(ed_ten.getText().toString());
                                                    fileHome.setPath("/storage/emulated/0/" + ed_ten.getText().toString());
                                                    list_home.set(posHome, fileHome);
                                                    adapter.saveFile(list_home);
                                                    adapter.notifyDataSetChanged();
                                                    break;
                                                }
                                            }
                                            allFileDTO.setTen(ed_ten.getText().toString());
                                            allFileDTO.setPath("/storage/emulated/0/" + ed_ten.getText().toString());
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
                            adapter = new HomeADAPTER(context, list_home);
                            list_home = adapter.readFile();
                            String path = list_bookmark.get(position).getPath();

                            int posHome = 0;
                            for (int i = 0; i < list_home.size(); i++) {
                                AllFileDTO fileHome = list_home.get(i);
                                if (fileHome.getPath().equals(path)) {
                                    posHome = i;
                                    fileHome.setBookmark(0);
                                    list_home.set(posHome, fileHome);
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
                                    adapter = new HomeADAPTER(context, list_home);
                                    list_home = adapter.readFile();
                                    String path = list_bookmark.get(position).getPath();

                                    File file = new File(path);
                                    if (file.exists()) {
                                        Log.d("ZZZZZ", "Path: " + file.getAbsolutePath());
                                        if (file.delete()) {
                                            int posHome = 0;
                                            for (int i = 0; i < list_home.size(); i++) {
                                                AllFileDTO fileHome = list_home.get(i);
                                                if (fileHome.getPath().equals(path)) {
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
                adapter = new HomeADAPTER(context, list_home);
                list_home = adapter.readFile();
                String path = list_bookmark.get(position).getPath();

                int posHome = 0;
                for (int i = 0; i < list_home.size(); i++) {
                    AllFileDTO fileHome = list_home.get(i);
                    if (fileHome.getPath().equals(path)) {
                        posHome = i;
                        fileHome.setBookmark(0);
                        list_home.set(posHome, fileHome);
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

    public void loadFile() {
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
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
