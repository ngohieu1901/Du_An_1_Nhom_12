package com.example.project_1.SUPPORT;

import com.amazic.ads.util.AdsApplication;

import java.util.List;

public class MyApplication extends AdsApplication {
    @Override
    public boolean enableAdsResume() {
        return true;
    }

    @Override
    public List<String> getListTestDeviceId() {
        return null;
    }

    @Override
    public String getResumeAdId() {
        return "resume_id";
    }

    @Override
    public Boolean buildDebug() {
        return null;
    }
}
