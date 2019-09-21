package com.thedascapital.www.newsapp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.thedascapital.www.newsapp.Utils.DataUtils;
import com.thedascapital.www.newsapp.R;
import com.thedascapital.www.newsapp.Models.Article;

import java.util.Objects;

public class myFragment3 extends Fragment {

    ImageView imageView3;
    TextView textView3;
    ProgressBar progressBar;
    WebView webView;
    TextView loadUrl;
    LinearLayout linearLayoutContent;

    public static boolean isWebView = false;

    public myFragment3() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_fragment3, container, false);

        linearLayoutContent = view.findViewById(R.id.linearLayoutContent);
        imageView3 = view.findViewById(R.id.imageView3);
        textView3 = view.findViewById(R.id.textView3);
        progressBar = view.findViewById(R.id.progressLoad);
        webView = view.findViewById(R.id.webView);
        loadUrl = view.findViewById(R.id.loadUrl);

        int val = getArguments().getInt("val");
        final Article article = (Article) getArguments().getSerializable("test");
        bindData(article);

        loadUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isWebView = true;

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setLoadWithOverviewMode(true);
                webSettings.setDomStorageEnabled(true);

                webView.setWebViewClient(new WebViewController());

                linearLayoutContent.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl(article.getUrl());
            }
        });


        return view;

    }



    private void bindData(Article article) {

        textView3.setText(article.getContent());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(DataUtils.getRandomDrawbleColor());
        requestOptions.error(DataUtils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(getActivity())
                .load(article.getUrlToImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView3);

    }

    public class WebViewController extends WebViewClient {
        private ProgressDialog webviewProgressDialog;

        public WebViewController() {
            //this.webviewProgressDialog = webviewProgressDialog;
            webviewProgressDialog =  ProgressDialog.show(getActivity(), "Please wait", "Loading...");

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (webviewProgressDialog.isShowing()) {
                webviewProgressDialog.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (webviewProgressDialog.isShowing()) {
                webviewProgressDialog.dismiss();
            }
            final AlertDialog alertDialog = new AlertDialog.Builder(Objects.requireNonNull(getActivity())).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Can't load the site");
            alertDialog.setCancelable(false);
            alertDialog.setButton(Dialog.BUTTON_POSITIVE,"OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    isWebView = false;
                    webView.setVisibility(View.GONE);
                    linearLayoutContent.setVisibility(View.VISIBLE);
                }
            });

            alertDialog.show();
        }
    }

}
