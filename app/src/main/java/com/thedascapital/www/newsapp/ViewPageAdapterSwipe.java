package com.thedascapital.www.newsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ViewPageAdapterSwipe extends FragmentStatePagerAdapter {
    public ViewPageAdapterSwipe(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPageAdapterSwipe(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        ChildFragment childFragment = new ChildFragment();
        position = position + 1;
        Bundle bundle = new Bundle();
        bundle.putInt("val", position);
        bundle.putString("Message", "Fragment: "+position);
        childFragment.setArguments(bundle);
        return childFragment;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
