package com.appsnipp.loginsamples.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.appsnipp.loginsamples.HomeActivity.CHECK_STATUS_LOGIN;
import static com.appsnipp.loginsamples.HomeActivity.USER_EMAIL_KEY;
import static com.appsnipp.loginsamples.HomeActivity.USER_PASSWORD_KEY;

public class SharedPrefs {
    private static final String PREFS_NAME = "share_prefs";
    private static SharedPrefs mInstance;
    private SharedPreferences mSharedPreferences;

    private SharedPrefs() {
        mSharedPreferences = GlobalApplication.self().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPrefs getInstance() {
        if (mInstance == null) {
            mInstance = new SharedPrefs();
        }
        return mInstance;
    }

    public <T> void put(String key, T data) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putFloat(key, (Float) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else {
            editor.putString(key, GlobalApplication.self().getGSon().toJson(data));
        }
        editor.apply();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> anonymousClass) {
        if (anonymousClass == String.class) {
            return (T) mSharedPreferences.getString(key, "");
        } else if (anonymousClass == Boolean.class) {
            return (T) Boolean.valueOf(mSharedPreferences.getBoolean(key, false));
        } else if (anonymousClass == Float.class) {
            return (T) Float.valueOf(mSharedPreferences.getFloat(key, 0));
        } else if (anonymousClass == Integer.class) {
            return (T) Integer.valueOf(mSharedPreferences.getInt(key, 0));
        } else if (anonymousClass == Long.class) {
            return (T) Long.valueOf(mSharedPreferences.getLong(key, 0));
        } else {
            return (T) GlobalApplication.self().getGSon().fromJson(mSharedPreferences.getString(key, ""), anonymousClass);
        }
    }

    public void clear() {
      //  mSharedPreferences.edit().clear().apply();
        SharedPrefs.getInstance().put(USER_EMAIL_KEY, "");
        SharedPrefs.getInstance().put(USER_PASSWORD_KEY, "");
        SharedPrefs.getInstance().put(CHECK_STATUS_LOGIN, "0");

    }
}
