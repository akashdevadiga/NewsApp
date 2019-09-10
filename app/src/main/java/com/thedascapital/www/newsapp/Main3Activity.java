package com.thedascapital.www.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thedascapital.www.newsapp.apiutils.Article;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main3Activity extends AppCompatActivity {

    ViewPager viewPager3;
    ViewPagerAdapter3 viewPagerAdapter3;
    int position4;
    private List<Article> articles3 = new ArrayList<Article>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        //articles3 = (ArrayList<Article>) getIntent().getExtras().getParcelable("test");
        //articles3 = (ArrayList<Article>) getIntent().getSerializableExtra("test");
        position4 = getIntent().getIntExtra("position",0);

        final ArrayList<String> ss= new ArrayList<>();
        final Gson gson = new Gson();
        try {
            String storedData = PrefUtils.getArticles(Main3Activity.this);
            java.lang.reflect.Type type = new TypeToken<List<Article>>(){}.getType();
            articles3 = gson.fromJson(storedData, type);
        } catch (Exception e){
            e.printStackTrace();
        }

       // articles3.get(1).

        viewPager3 = findViewById(R.id.pager3);
        viewPagerAdapter3 = new ViewPagerAdapter3(getSupportFragmentManager(), articles3, position4);
        viewPager3.setAdapter(viewPagerAdapter3);

    }
}
