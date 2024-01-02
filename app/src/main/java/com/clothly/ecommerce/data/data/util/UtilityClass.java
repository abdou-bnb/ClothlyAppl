package com.clothly.ecommerce.data.data.util;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.clothly.ecommerce.data.data.helper.models.AddressModel;
import com.clothly.ecommerce.data.data.helper.models.InventoryServerModel;
import com.clothly.ecommerce.data.data.helper.response.UserAddressResponse;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.ui.main.MainActivity;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UtilityClass {

    public static final String TOOLBAR_TITLE = "toolbar_title";

    public static void textViewScaleIconLeft(Context context, View v, int imageId, double opacity) {

        int alpha = (int) (255 * opacity);

        EditText conatiner = (EditText) v;
        int imageResource = imageId;
        Drawable drawable = ContextCompat.getDrawable(context, imageResource);
        drawable.setAlpha(alpha);
        int pixelDrawableSize = (int) Math.round(conatiner.getLineHeight() * 1); // Or the percentage you like (0.8, 0.9, etc.)
        drawable.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize); // setBounds(int left, int top, int right, int bottom), in this case, drawable is a square image
        conatiner.setCompoundDrawables(
                drawable,//left
                null, //top
                null, //right
                null //bottom
        );
    }

    public static void buttonScaleIconLeft(Context context, View v, int imageId, double opacity) {
        int alpha = (int) (255 * opacity);

        Button conatiner = (Button) v;
        int imageResource = imageId;
        Drawable drawable = ContextCompat.getDrawable(context, imageResource);
        drawable.setAlpha(alpha);
        int pixelDrawableSize = (int) Math.round(conatiner.getLineHeight() * 1); // Or the percentage you like (0.8, 0.9, etc.)
        drawable.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize); // setBounds(int left, int top, int right, int bottom), in this case, drawable is a square image
        conatiner.setCompoundDrawables(
                drawable,//left
                null, //top
                null, //right
                null //bottom
        );
    }

    public static void buttonScaleIconRight(Context context, View v, int imageId, double opacity, double size) {
        int alpha = (int) (255 * opacity);

        Button conatiner = (Button) v;
        int imageResource = imageId;
        Drawable drawable = ContextCompat.getDrawable(context, imageResource);
        drawable.setAlpha(alpha);
        int pixelDrawableSize = (int) Math.round(conatiner.getLineHeight() * size); // Or the percentage you like (0.8, 0.9, etc.)
        drawable.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize); // setBounds(int left, int top, int right, int bottom), in this case, drawable is a square image
        conatiner.setCompoundDrawables(
                null,//left
                null, //top
                drawable, //right
                null //bottom
        );
    }


    public static String getNumberFormat(Context context, double price) {
        String price2;
        if (price % 1 == 0) {
            DecimalFormat df = new DecimalFormat("#0.0");
            df.setRoundingMode(RoundingMode.DOWN);
            price2 = df.format(price);

        } else {
            DecimalFormat df = new DecimalFormat("#0.00");
            price2 = String.valueOf(df.format(price));
        }
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        //String currency = String.valueOf(nf.format(23.2).charAt(0));
        String currency = CustomSharedPrefs.getCurrency(context);
        return String.valueOf(currency + price2);
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static Date stringToDate(String dateASstring) throws ParseException {
        //String string = "January 2, 2010";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date date = format.parse(dateASstring);
        return date;
    }

    /*public static void removeFavProduct(int productId, Context mActivity){
        DataSource dataSource = new DataSource(mActivity);
        dataSource.removeProductById(productId);

    }*/



    public static String objectToString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static String objectToStrings(List<InventoryServerModel> obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
    public static UserAddressResponse stringToAddress(String jsonProduct) {
        Gson gson = new Gson();
        return gson.fromJson(jsonProduct, UserAddressResponse.class);
    }

    public static <T> Object stringToObject(String jsonProduct, Class<T> obj) {
        Gson gson = new Gson();
        Type collectionType = new TypeToken<T>(){}.getType();
        return gson.fromJson(jsonProduct, collectionType);
    }

    public static String userToJson(UserRegistrationResponse user) {
        Gson gson = new Gson();
        return gson.toJson(user);
    }

    public static UserRegistrationResponse jsonToUser(String jsonUser) {
        Gson gson = new Gson();
        return gson.fromJson(jsonUser, UserRegistrationResponse.class);
    }

    public static String capFirstLetter(String value) {

        return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
    }

    public static int dpToPx(float dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void cartEmptyRedirect(Context context) {
        if (CustomSharedPrefs.getCartEmpty(context)) {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        }
    }

    public static void signOutFB(Context context) {
        LoginManager.getInstance().logOut();
        CustomSharedPrefs.removeLoggedInUser(context);
    }

    public static void signOutGoogle(GoogleApiClient gApiClient, Context context) {
        Auth.GoogleSignInApi.signOut(gApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Log.d("emailGoogl", "Loggedout");
            }
        });
        CustomSharedPrefs.removeLoggedInUser(context);
    }

    public static void signOutEmail(Context context) {
        CustomSharedPrefs.setLoggedInUser(context, null);
        CustomSharedPrefs.removeLoggedInUser(context);
    }

    public static String getDateStringFromDateValue(String dateValue, String dateFormat) {
        String dateString = "";
        try {
            String pattern = "yyyy-MM-dd hh:mm:ss";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            Date date = simpleDateFormat.parse(dateValue);


            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(dateFormat);
            dateString = simpleDateFormat2.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return dateString;//DateFormat.format(dateFormat, getTimestampInMili(dateValue)).toString();
    }

    public static AddressModel stringToAddressModel(String jsonProduct) {
        Gson gson = new Gson();
        return gson.fromJson(jsonProduct, AddressModel.class);
    }

}
