package com.thedascapital.www.newsapp;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.thedascapital.www.newsapp.apiutils.ApiClient;
import com.thedascapital.www.newsapp.apiutils.ApiInterface;
import com.thedascapital.www.newsapp.apiutils.Article;
import com.thedascapital.www.newsapp.apiutils.News;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FirstFragment extends Fragment {

    public static final String API_KEY = "7a6c69485aad4a06b4798d294fccd8ea";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articles = new ArrayList<>();
    private Adapter adapter;
    private String TAG = FirstFragment.class.getSimpleName();

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
        int val = getArguments().getInt("val");
        if (val == 1){
            LoadJson("business");
        }else if (val == 2){
            LoadJson("sports");
        } else {
            LoadJson("science");
        }


        return view;
    }

    public void LoadJson(String countryVal){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        String country = Utils.getCountry();

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

                    initListerner();

                }else {
                    Toast.makeText(getContext(),"No Result", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(getContext(),"No Result", Toast.LENGTH_SHORT).show();
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
                Intent ii = new Intent(getActivity(), Main3Activity.class);
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
}
