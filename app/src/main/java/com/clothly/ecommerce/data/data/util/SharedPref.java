package com.clothly.ecommerce.data.data.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

public class SharedPref {
    private static SharedPref sSharedPref;
    private static String TAG = "shared";
    private SharedPreferences preferences;

    private SharedPref(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

    }

    public static synchronized SharedPref getSharedPref(@NonNull Context context) {

        Log.d(TAG, Thread.currentThread().getName());
        if (sSharedPref == null) {
            Log.d(TAG,Thread.currentThread().getName());

            synchronized (SharedPref.class) {
                Log.d(TAG,Thread.currentThread().getName());
                if (sSharedPref == null) {
                    Log.d(TAG,Thread.currentThread().getName());
                    sSharedPref = new SharedPref(context);
                }
            }

        }
        return sSharedPref;
    }

    public boolean write(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(key, value);

        return editor.commit();
    }

    public boolean write(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(key, value);

        return editor.commit();
    }

    public boolean write(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(key, value);

        return editor.commit();
    }
    public boolean write(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putFloat(key, value);

        return editor.commit();
    }

    public boolean write(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(key, value);

        return editor.commit();
    }

    public String read(String key) {
        return preferences.getString(key, "");
    }

    public long readLong(String key) {
        return preferences.getLong(key, 0);
    }

    public int readInt(String key) {
        return preferences.getInt(key, 0);
    }

    public float readFloat(String key){
        return preferences.getFloat(key, 0);
    }

    public boolean readBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    public boolean readBooleanDefaultTrue(String key) {
        return preferences.getBoolean(key, true);
    }

    public boolean contains(String key) {
        return preferences.contains(key);
    }
}
