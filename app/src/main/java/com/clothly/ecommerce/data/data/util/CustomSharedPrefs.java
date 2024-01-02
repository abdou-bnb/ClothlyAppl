package com.clothly.ecommerce.data.data.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;

public class CustomSharedPrefs {
    public static void setLoggedInUser(Context context, UserRegistrationResponse user){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_USER, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(Constants.SharedPrefCredential.SHARED_PREF_LOGGEDIN_USER, json);
        editor.commit();
    }

    public static UserRegistrationResponse getLoggedInUser(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_USER, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = myPref.getString(Constants.SharedPrefCredential.SHARED_PREF_LOGGEDIN_USER,"");
        UserRegistrationResponse user = gson.fromJson(json, UserRegistrationResponse.class);
        return user;

    }

    public static String getLoggedInUserId(Context context){

        try {
            return getLoggedInUser(context).userRegistrationInfo.id;
        }catch ( NullPointerException e){
            return "";
        }
    }

    public static void setRegisteredUser(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_CURRENCY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.Preferences.USER_REGISTRATION, true);
        editor.commit();
    }

    public static boolean getUserStatus(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_CURRENCY, Context.MODE_PRIVATE);
        boolean aBoolean = myPref.getBoolean(Constants.Preferences.USER_REGISTRATION,false);
        return aBoolean;
    }
    public static void removeLoggedInUser(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_CURRENCY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.Preferences.USER_REGISTRATION, false);
        editor.commit();
    }


    public static void setCurrency(Context context, String currency){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_CURRENCY, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.SharedPrefCredential.SHARED_PREF_CURRENCY_IN, currency);
        editor.commit();
    }


    public static String getCurrency(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_CURRENCY, Context.MODE_PRIVATE);
        String currency = myPref.getString(Constants.SharedPrefCredential.SHARED_PREF_CURRENCY_IN,"");
        return currency;
    }

    public static void setCartEmpty(Context context, boolean emptyCart){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_EMPTY_CART, Context.MODE_PRIVATE).edit();
        editor.putBoolean(Constants.SharedPrefCredential.SHARED_PREF_EMPTY_CART_IN, emptyCart);
        editor.commit();
    }

    public static boolean getCartEmpty(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_EMPTY_CART, Context.MODE_PRIVATE);
        return myPref.getBoolean(Constants.SharedPrefCredential.SHARED_PREF_EMPTY_CART_IN,true);
    }


    public static void setProfileUrl(Context context, String profileUrl){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_PROFILEIMAGE, Context.MODE_PRIVATE).edit();
        editor.putString(Constants.SharedPrefCredential.SHARED_PREF_PROFILEIMAGE_IN, profileUrl);
        editor.commit();
    }

    public static String getProfileUrl(Context context){
        SharedPreferences myPref = context.getSharedPreferences(Constants.SharedPrefCredential.SHARED_PREF_PROFILEIMAGE, Context.MODE_PRIVATE);
        return myPref.getString(Constants.SharedPrefCredential.SHARED_PREF_PROFILEIMAGE_IN,"");
    }
}
