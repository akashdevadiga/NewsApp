package com.thedascapital.www.newsapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.thedascapital.www.newsapp.Adapters.Adapter;
import com.thedascapital.www.newsapp.Models.News;
import com.thedascapital.www.newsapp.R;
import com.thedascapital.www.newsapp.Adapters.ViewPagerAdapter;
import com.thedascapital.www.newsapp.Models.Article;
import com.thedascapital.www.newsapp.Utils.DataUtils;
import com.thedascapital.www.newsapp.Utils.PrefUtils;
import com.thedascapital.www.newsapp.apiutils.ApiClient;
import com.thedascapital.www.newsapp.apiutils.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    ProgressBar searchProgressBar;
    NestedScrollView nestedScrollView5;
    RelativeLayout searchingLayout;

    private RelativeLayout searchResultLayout;

    SearchView searchView;

    boolean doubleBackToExitPressedOnce = false;

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

        searchResultLayout = findViewById(R.id.searchResultLayout);
        searchingLayout = findViewById(R.id.searchingLayout);
        searchProgressBar = findViewById(R.id.searchProgressBar);
        nestedScrollView5 = findViewById(R.id.nestedScrollView5);

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

        recyclerView = findViewById(R.id.recyclerView5);
        layoutManager = new LinearLayoutManager(NewsActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        String NOTIFICATION_CHANEL_ID = "MyNotification";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANEL_ID, "MyNotification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Successful";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(NewsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_menu, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search latest news.....");


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)){
                    //listView clear;
                    viewPager.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    searchResultLayout.setVisibility(View.VISIBLE);
                    hideSoftKeyboard();
                    LoadJson(query);
                }else {
                    //add data
                    Toast.makeText(getApplicationContext(),query, Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }




        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean newViewFocus)
            {
                if (!newViewFocus)
                {
                    //Collapse the action item.
                    //searchItem.collapseActionView();
                    //Clear the filter/search query.
                    searchResultLayout.setVisibility(View.GONE);
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);

                    searchingLayout.setVisibility(View.VISIBLE);
                    nestedScrollView5.setVisibility(View.GONE);
                }
            }
        });


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
            case R.id.action_search:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }



    public void LoadJson(String q){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        //String country = DataUtils.getCountry();

        Call<News> call;
        //call = apiInterface.getNews("in","MODI", API_KEY);
        call = apiInterface.getEverything(q, API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null){
                    searchingLayout.setVisibility(View.GONE);
                    nestedScrollView5.setVisibility(View.VISIBLE);
                    if (!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new Adapter(articles, NewsActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    initListerner();

                }else {
                    Toast.makeText(NewsActivity.this,"No Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(NewsActivity.this,"No Result", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initListerner(){
        adapter.setOnItemClickListerner(new Adapter.OnItemClickListerner() {
            @Override
            public void onItemClick(View view, int position) {
                Gson gson = new Gson();
                String vData = gson.toJson(articles);
                PrefUtils.storeArticles(getApplicationContext(), vData);
                Intent ii = new Intent(getApplicationContext(), NewsDetailsActivity.class);
                ii.putExtra("position", position);

                startActivity(ii);
            }
        });
    }

    @Override
    public void onBackPressed() {
        hideSoftKeyboard();
        if (!searchView.isIconified()) {
            searchView.setIconified(true); //for remove searching
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    private void hideSoftKeyboard(){
        View view = NewsActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) NewsActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow (view.getWindowToken (), 0);
        }
    }

}
