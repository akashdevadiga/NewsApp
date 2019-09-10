package com.thedascapital.www.newsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        FirstFragment firstFragment = new FirstFragment();
        position = position + 1;
        Bundle bundle = new Bundle();
        bundle.putInt("val", position);
        bundle.putString("Message", "Fragment: "+position);
        firstFragment.setArguments(bundle);
        return firstFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        position = position + 1;
        if(position == 1) return "Business";
        else if(position == 2) return "Sports";
        else return "Science";
    }
}
