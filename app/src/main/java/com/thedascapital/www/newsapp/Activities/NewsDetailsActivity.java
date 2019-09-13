package com.thedascapital.www.newsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thedascapital.www.newsapp.R;
import com.thedascapital.www.newsapp.Adapters.ViewPagerAdapter3;
import com.thedascapital.www.newsapp.Models.Article;
import com.thedascapital.www.newsapp.Utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

public class NewsDetailsActivity extends AppCompatActivity {

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
            String storedData = PrefUtils.getArticles(NewsDetailsActivity.this);
            java.lang.reflect.Type type = new TypeToken<List<Article>>(){}.getType();
            articles3 = gson.fromJson(storedData, type);
        } catch (Exception e){
            e.printStackTrace();
        }


        viewPager3 = findViewById(R.id.pager3);
        viewPagerAdapter3 = new ViewPagerAdapter3(getSupportFragmentManager(), articles3, position4);
        viewPager3.setAdapter(viewPagerAdapter3);
        viewPager3.setCurrentItem(position4);

    }

    @Override
    public void onBackPressed() {

        if (findViewById(R.id.webView).getVisibility() == View.VISIBLE) {
            findViewById(R.id.webView).setVisibility(View.GONE);
            findViewById(R.id.linearLayoutContent).setVisibility(View.VISIBLE);
            //viewPager3.setCurrentItem(viewPager3.getCurrentItem(),false);
        }else{
            super.onBackPressed();
        }

    }
}
