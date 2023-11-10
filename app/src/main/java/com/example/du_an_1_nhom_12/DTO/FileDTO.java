package com.example.du_an_1_nhom_12.DTO;

import java.io.Serializable;

public class FileDTO implements Serializable {
    int hinh;
    String ten;
    String ngay;
    // thêm thuộc tính bookmark để nhận lưu lựa chọn
    int bookMarl;

    public FileDTO(int hinh, String ten, String ngay, int bookMarl) {
        this.hinh = hinh;
        this.ten = ten;
        this.ngay = ngay;
        this.bookMarl = bookMarl;
    }


    public FileDTO() {
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

    public int getBookMarl() {
        return bookMarl;
    }

    public void setBookMarl(int bookMarl) {
        this.bookMarl = bookMarl;
    }
}
