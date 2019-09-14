package com.thedascapital.www.newsapp.apiutils;

import com.thedascapital.www.newsapp.Models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getNews(@Query("country") String country, @Query("q") String q, @Query("apiKey") String type);

    @GET("top-headlines")
    Call<News> getNewsOnlyIn(@Query("country") String country, @Query("apiKey") String type);

    @GET("top-headlines")
    Call<News> getNewsCC(@Query("country") String country, @Query("category") String category, @Query("apiKey") String type);

    @GET("everything")
    Call<News> getEverything(@Query("q") String q, @Query("apiKey") String type);

}
