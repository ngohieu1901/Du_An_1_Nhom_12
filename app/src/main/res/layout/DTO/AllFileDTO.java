package com.example.project_1.DTO;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "tb_file")
public class AllFileDTO implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;
    int hinh;
    String ten;
    String ngay;
    String path;
    int bookmark;

    public AllFileDTO(int hinh, String ten, String ngay,String path, int bookmark) {
        this.hinh = hinh;
        this.ten = ten;
        this.ngay = ngay;
        this.path = path;
        this.bookmark = bookmark;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHinh() {
        return hinh;
    }

    public void setHinh(int hinh) {
        this.hinh = hinh;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getBookmark() {
        return bookmark;
    }

    public void setBookmark(int bookmark) {
        this.bookmark = bookmark;
    }
}
