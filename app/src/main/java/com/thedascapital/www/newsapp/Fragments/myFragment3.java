package com.thedascapital.www.newsapp.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link myFragment3.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link myFragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class myFragment3 extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

   // private OnFragmentInteractionListener mListener;

    ImageView imageView3;
    TextView textView3;
    ProgressBar progressBar;
    WebView webView;
    TextView loadUrl;
    LinearLayout linearLayoutContent;

    public myFragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment myFragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static myFragment3 newInstance(String param1, String param2) {
        myFragment3 fragment = new myFragment3();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

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
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
