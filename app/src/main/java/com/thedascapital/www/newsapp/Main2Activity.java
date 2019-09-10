package com.thedascapital.www.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Main2Activity extends AppCompatActivity {

    VerticalViewPager verticalViewPager;
    ViewPageAdapterSwipe viewPageAdapterSwipe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        verticalViewPager = findViewById(R.id.viewpager);
        viewPageAdapterSwipe = new ViewPageAdapterSwipe(getSupportFragmentManager());

        verticalViewPager.setAdapter(viewPageAdapterSwipe);
    }
}
