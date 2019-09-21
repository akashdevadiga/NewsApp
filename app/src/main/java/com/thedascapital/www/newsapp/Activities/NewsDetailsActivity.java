package com.thedascapital.www.newsapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.ToxicBakery.viewpager.transforms.RotateUpTransformer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thedascapital.www.newsapp.R;
import com.thedascapital.www.newsapp.Adapters.ViewPagerAdapter3;
import com.thedascapital.www.newsapp.Models.Article;
import com.thedascapital.www.newsapp.Utils.PrefUtils;

import java.util.ArrayList;
import java.util.List;

import static com.thedascapital.www.newsapp.Fragments.myFragment3.isWebView;

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

        // set the card transformer and set reverseDrawingOrder to true, so the fragments are drawn from the right to the left
        viewPager3.setPageTransformer(true, new CardTransformer(0.7f));// Animation.

        //viewPager3.setPageTransformer(true, new RotateUpTransformer()); //using library

    }

    @Override
    public void onBackPressed() {
            //findViewById(R.id.webView).getVisibility() == View.VISIBLE
        if (isWebView) {
            isWebView = false;
            findViewById(R.id.webView).setVisibility(View.GONE);
            findViewById(R.id.linearLayoutContent).setVisibility(View.VISIBLE);
            //viewPager3.setCurrentItem(viewPager3.getCurrentItem(),false);
        }else{
            super.onBackPressed();
        }

    }

    public class CardTransformer implements ViewPager.PageTransformer {

        private final float scalingStart;

        public CardTransformer(float scalingStart) {
            super();
            this.scalingStart = 1 - scalingStart;
        }

        @Override
        public void transformPage(View page, float position) {

            if (position >= 0) {
                final int w = page.getWidth();
                float scaleFactor = 1 - scalingStart * position;

                page.setAlpha(1 - position);
                page.setScaleX(scaleFactor);
                page.setScaleY(scaleFactor);
                page.setTranslationX(w * (1 - position) - w);
            }
        }
    }

}
