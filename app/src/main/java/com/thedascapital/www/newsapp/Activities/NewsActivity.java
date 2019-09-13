package com.thedascapital.www.newsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.thedascapital.www.newsapp.Adapters.Adapter;
import com.thedascapital.www.newsapp.R;
import com.thedascapital.www.newsapp.Adapters.ViewPagerAdapter;
import com.thedascapital.www.newsapp.Models.Article;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    public static final String API_KEY = "7a6c69485aad4a06b4798d294fccd8ea";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = MainActivity.class.getSimpleName();
    Toolbar toolbar;
    ViewPager viewPager;
    ViewPagerAdapter viewPagerAdapter;
    TabLayout tabLayout;
    ConstraintLayout delayLayout;
    private RelativeLayout errorLayout;
    private Button connectionRetryBtn;
    private TextView errorTitle, errorMessage;
    private ImageView errorImage;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            delayLayout.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        delayLayout = findViewById(R.id.delayLayout);
        handler.postDelayed(runnable, 2000); //2 seconds for splash screen

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.pager);
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        errorLayout = findViewById(R.id.errorLayout);
        connectionRetryBtn = findViewById(R.id.connectionRetryBtn);
        errorMessage = findViewById(R.id.errorMessage);
        errorTitle = findViewById(R.id.errorTitle);
        errorImage = findViewById(R.id.errorImage);

//        recyclerView = findViewById(R.id.recyclerView);
//        layoutManager = new LinearLayoutManager(NewsActivity.this);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setNestedScrollingEnabled(false);
//
//        LoadJson();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();

                // Google sign out
                GoogleSignIn.getClient(
                        getApplicationContext(),
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
                ).signOut();

                Intent a = new Intent(NewsActivity.this, MainActivity.class);
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



//    public void LoadJson(){
//        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
//
//        String country = DataUtils.getCountry();
//
//        Call<News> call;
//        //call = apiInterface.getNews("in","MODI", API_KEY);
//        call = apiInterface.getNewsOnlyIn("in", API_KEY);
//
//        call.enqueue(new Callback<News>() {
//            @Override
//            public void onResponse(Call<News> call, Response<News> response) {
//                if (response.body() != null){
//
//                    if (!articles.isEmpty()){
//                        articles.clear();
//                    }
//
//                    articles = response.body().getArticles();
//                    adapter = new Adapter(articles, NewsActivity.this);
//                    recyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                    initListerner();
//
//                }else {
//                    Toast.makeText(NewsActivity.this,"No Result", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<News> call, Throwable t) {
//                Toast.makeText(NewsActivity.this,"No Result", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void initListerner(){
//        adapter.setOnItemClickListerner(new Adapter.OnItemClickListerner() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(NewsActivity.this,"Ganesha "+articles.get(position), Toast.LENGTH_SHORT).show();
////                Intent ii = new Intent(NewsActivity.this, Main2Activity.class);
////                Article article = articles.get(position);
////                ii.putExtra("url", article.getUrl());
////                ii.putExtra("url", article.getUrl());
////                ii.putExtra("url", article.getUrl());
////                ii.putStringArrayListExtra("list", (ArrayList<String>) test);
////                startActivity(ii);
//            }
//        });
//    }

}
