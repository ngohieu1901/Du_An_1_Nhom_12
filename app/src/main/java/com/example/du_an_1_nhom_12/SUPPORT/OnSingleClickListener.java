package com.example.du_an_1_nhom_12.SUPPORT;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.view.View;


public abstract class OnSingleClickListener implements View.OnClickListener {
    private static final long MIN_CLICK_INTERVAL = 1000;
    private long lastClickTime;

    public boolean isLockTime() {
        return true;
    }

    public abstract void onSingleClick(View view);

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View view) {
        if (isLockTime()) {
            long currentClickTime = SystemClock.uptimeMillis();
            long elapsedTime = currentClickTime - lastClickTime;
            lastClickTime = currentClickTime;
            if (elapsedTime < MIN_CLICK_INTERVAL) return;
        }
        onSingleClick(view);
    }
}
