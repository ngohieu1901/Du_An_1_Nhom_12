package com.example.project_1.DTO;

public class LanguageDTO {
    int hinh;
    String ten;
    boolean isChecked;

    public LanguageDTO(int hinh, String ten, boolean isChecked) {
        this.hinh = hinh;
        this.ten = ten;
        this.isChecked = isChecked;
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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
