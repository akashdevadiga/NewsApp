package com.thedascapital.www.newsapp.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thedascapital.www.newsapp.Activities.NewsDetailsActivity;
import com.thedascapital.www.newsapp.Adapters.Adapter;

import com.thedascapital.www.newsapp.Utils.DataUtils;
import com.thedascapital.www.newsapp.R;
import com.thedascapital.www.newsapp.Utils.PrefUtils;
import com.thedascapital.www.newsapp.apiutils.ApiClient;
import com.thedascapital.www.newsapp.apiutils.ApiInterface;
import com.thedascapital.www.newsapp.Models.Article;
import com.thedascapital.www.newsapp.Models.News;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String API_KEY = "7a6c69485aad4a06b4798d294fccd8ea";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = FirstFragment.class.getSimpleName();
    private SwipeRefreshLayout refreshLayout;
    TextView newsHeadline;
    int val;
    private RelativeLayout errorLayout;
    private Button connectionRetryBtn;
    private TextView errorTitle, errorMessage;
    private ImageView errorImage;

    public FirstFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_first, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);

        newsHeadline = view.findViewById(R.id.newsHeadline);

        errorLayout = view.findViewById(R.id.errorLayout);
        connectionRetryBtn = view.findViewById(R.id.connectionRetryBtn);
        errorMessage = view.findViewById(R.id.errorMessage);
        errorTitle = view.findViewById(R.id.errorTitle);
        errorImage = view.findViewById(R.id.errorImage);

         val = getArguments().getInt("val");
        if (val == 1){
            onLoadingRefresh("business");
        }else if (val == 2){
            onLoadingRefresh("sports");
        } else {
            onLoadingRefresh("science");
        }


        return view;
    }

    public void LoadJson(String countryVal){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        errorLayout.setVisibility(View.GONE);

        refreshLayout.setRefreshing(true);
        newsHeadline.setText("Getting The Result");

        String country = DataUtils.getCountry();

        Call<News> call;
        //call = apiInterface.getNews("in","business", API_KEY);
        //call = apiInterface.getNewsOnlyIn(countryVal, API_KEY);
        call = apiInterface.getNewsCC("in",countryVal, API_KEY);

        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.body() != null){

                    if (!articles.isEmpty()){
                        articles.clear();
                    }

                    articles = response.body().getArticles();
                    adapter = new Adapter(articles, getContext());
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                    newsHeadline.setText("Top HeadLines");
                    refreshLayout.setRefreshing(false);
                    newsHeadline.setVisibility(View.VISIBLE);

                    initListerner();

                }else {
                    refreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(),"No Result", Toast.LENGTH_SHORT).show();
                    newsHeadline.setVisibility(View.INVISIBLE);

                    String errorCode;
                    switch (response.code()){
                        case 404:
                            errorCode = "No data found";
                            break;
                        case 500:
                            errorCode = "Server Error";
                            break;
                        default:
                            errorCode = "Unknown error";
                            break;
                    }

                    showErrorMessage(R.drawable.ic_satellite_signal,
                                        "No Result",
                                    "Please try again later\n"+ errorCode);

                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(getContext(),"No Result: "+t.getMessage(), Toast.LENGTH_SHORT).show();
                newsHeadline.setVisibility(View.INVISIBLE);

                showErrorMessage(R.drawable.ic_satellite_signal,
                        "No Result",
                        "Network failure, Please try again later \n"+t.getLocalizedMessage().toString());
            }
        });
    }

    private void showErrorMessage(int imageView, String title, String message) {
        errorLayout.setVisibility(View.VISIBLE);
        errorImage.setImageResource(imageView);
        errorTitle.setText(title);
        errorMessage.setText(message);

        connectionRetryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (val == 1){
                    onLoadingRefresh("business");
                }else if (val == 2){
                    onLoadingRefresh("sports");
                } else {
                    onLoadingRefresh("science");
                }
            }
        });

    }

    private void initListerner(){
        adapter.setOnItemClickListerner(new Adapter.OnItemClickListerner() {
            @Override
            public void onItemClick(View view, int position) {
               // Toast.makeText(getContext(),"Ganesha "+articles.get(position).getAuthor(), Toast.LENGTH_SHORT).show();
                Gson gson = new Gson();
                String vData = gson.toJson(articles);
                PrefUtils.storeArticles(getActivity(), vData);
                Intent ii = new Intent(getActivity(), NewsDetailsActivity.class);
                //Article article = articles.get(position);
                ii.putExtra("position", position);
                //Bundle bundle = new Bundle();
                //bundle.putParcelableArrayList("test",  articles);
                //ii.putExtras(bundle);

               // ii.putExtra("test", (Serializable) articles);

                startActivity(ii);
            }
        });
    }

    @Override
    public void onRefresh() {
        if (val == 1){
            onLoadingRefresh("business");
        }else if (val == 2){
            onLoadingRefresh("sports");
        } else {
            onLoadingRefresh("science");
        }
    }

    private void onLoadingRefresh(final String keyword){
        refreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        LoadJson(keyword);
                    }
                }
        );
    }

}
