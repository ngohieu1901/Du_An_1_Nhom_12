package com.example.du_an_1_nhom_12.DTO;

import android.net.Uri;

public class ImageDTO {
    Uri imageUri;
    boolean checked;

    public ImageDTO(Uri imageUri, boolean checked) {
        this.imageUri = imageUri;
        this.checked = checked;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
