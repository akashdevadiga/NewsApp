package com.thedascapital.www.newsapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
    public PrefUtils() {
    }
    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("APP_PREF", Context.MODE_PRIVATE);
    }

    public static void storeArticles(Context context, String ar) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString("ARTICLES",ar);
        editor.apply();
    }

    public static String getArticles(Context context) {
        return getSharedPreferences(context).getString("ARTICLES","");
    }

}
