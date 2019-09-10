package com.thedascapital.www.newsapp;

import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.thedascapital.www.newsapp.apiutils.Article;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter3 extends FragmentPagerAdapter {

    private List<Article> articles4 = new ArrayList<Article>();
    int positionValue;

    public ViewPagerAdapter3(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPagerAdapter3(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public ViewPagerAdapter3(FragmentManager fm, List<Article> articles, int positionVal) {
        super(fm);
        articles4 = articles;
        positionValue = positionVal;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        myFragment3 myFragment3 = new myFragment3();
        position = position + 1;
        Bundle bundle = new Bundle();
        bundle.putInt("val", position);
        bundle.putString("Message", "Fragment: "+position);
        Article aa = articles4.get(position);
        bundle.putSerializable("test", (Serializable) aa);
        myFragment3.setArguments(bundle);
        return myFragment3;
    }

    @Override
    public int getCount() {
        return articles4.size()-1;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        position = position + 1;
//        if(position == 1) return "Business";
//        else if(position == 2) return "Sports";
//        else return "Science";
//    }
}
