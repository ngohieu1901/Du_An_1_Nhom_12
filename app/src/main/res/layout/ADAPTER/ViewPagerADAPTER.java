package com.example.project_1.ADAPTER;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.project_1.FRAGMENT.ScreenSaver1;
import com.example.project_1.FRAGMENT.ScreenSaver2;
import com.example.project_1.FRAGMENT.ScreenSaver3;

public class ViewPagerADAPTER extends FragmentStatePagerAdapter {
    public ViewPagerADAPTER(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return  new ScreenSaver1();
            case 1:
                return  new ScreenSaver2();
            case 2:
                return  new ScreenSaver3();
            default:
                return new ScreenSaver1();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
