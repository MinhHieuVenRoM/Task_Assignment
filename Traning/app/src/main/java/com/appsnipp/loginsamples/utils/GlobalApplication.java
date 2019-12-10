package com.appsnipp.loginsamples.utils;

import android.app.Application;

import com.google.gson.Gson;

public class GlobalApplication extends Application {
    private static GlobalApplication mSelf;
    private Gson mGSon;

    public static GlobalApplication self() {
        return mSelf;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSelf = this;
        mGSon = new Gson();
    }

    public Gson getGSon() {
        return mGSon;
    }
}
