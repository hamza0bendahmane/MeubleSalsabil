package com.createch.meublessalsabil.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.Locale;

public class Application extends android.app.Application {

    public static final String ADMINE_PHONE = "0556765919";

    public static void changeLang(String lang, Activity activity, boolean refresh) {
        Locale local = new Locale(lang);
        Resources res = activity.getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        Configuration configuration = res.getConfiguration();
        configuration.locale = local;
        res.updateConfiguration(configuration, displayMetrics);
        activity.getSharedPreferences("language", MODE_PRIVATE).edit().putString("language", lang).apply();
        if (refresh) {
            Intent intent = activity.getIntent();
            activity.finish();
            activity.startActivity(intent);
        }
    }

    public static String getCurrentLang(Context cc) {
        String language = cc.getSharedPreferences("language", MODE_PRIVATE).getString("language", "ar");
        return language;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.fullyInitialize();
        AppEventsLogger.activateApp(this);
    }
}
