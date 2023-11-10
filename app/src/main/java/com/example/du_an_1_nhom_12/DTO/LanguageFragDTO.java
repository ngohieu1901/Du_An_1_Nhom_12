package com.example.du_an_1_nhom_12.DTO;

public class LanguageFragDTO {
    private Boolean isSelected;
    String ten;

    public LanguageFragDTO(Boolean isSelected, String ten) {
        this.isSelected = isSelected;
        this.ten = ten;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
