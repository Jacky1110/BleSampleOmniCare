package com.asuslife.sampleapps.blesampleomnicare.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.asuslife.sampleapps.blesampleomnicare.BuildConfig;

public class SharedPreferencesHelper {

    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(BuildConfig.APPLICATION_ID + "_preferences", Context.MODE_PRIVATE);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public void putBoolean(String key, boolean value) {
        sharedPreferences.edit()
                .putBoolean(key, value)
                .apply();
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key,0);
    }

    public void putInt(String key, int value) {
        sharedPreferences.edit()
                .putInt(key, value)
                .apply();
    }

}
