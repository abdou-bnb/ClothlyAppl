package com.clothly.ecommerce.data.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.response.AdMobResponse;
import com.clothly.ecommerce.data.data.helper.response.MainPageResponse;
import com.clothly.ecommerce.data.data.helper.response.SettingsResponse;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.NetworkHelper;
import com.clothly.ecommerce.data.data.util.SharedPref;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;

public class MainPresenter extends BasePresenter<MainMvpView> {
    private boolean isNetworkAvailable;

    /**
     * Get Data from server
     */
    public void getHomeDataFromServer(Context context,String pageNumber ){
        String userID = CustomSharedPrefs.getLoggedInUserId(context);
        RetrofitClient.getApiService().
                getHomePageDataFromServer(Constants.ServerUrl.API_TOKEN,""+userID,""+pageNumber).enqueue(new Callback<MainPageResponse>() {
            @Override
            public void onResponse(Call<MainPageResponse> call, retrofit2.Response<MainPageResponse> response) {
                if (response.body()!=null){
                    getMvpView().onGettingHomePageDataSuccess(response.body());
                }
            }
            @Override
            public void onFailure(Call<MainPageResponse> call, Throwable t) {
                getMvpView().onGettingHomePageDataError(t.toString());
            }
        });


    }

    /**
     * getting admob credential from server
     *
     * @param context
     */
    public void getAdMobCredential(final Context context) {
        isNetworkAvailable = NetworkHelper.hasNetworkAccess(context);
        if (isNetworkAvailable) {

            String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.ADMOB_URL;
            RequestQueue queue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //getMvpView().onCategorySuccess(response);
                    AdMobResponse adMobResponse;
                    Type admob = new TypeToken<AdMobResponse>() {
                    }.getType();
                    adMobResponse = new Gson().fromJson(response, admob);
                    SharedPref.getSharedPref(context).write(Constants.Preferences.BANNER_STATUS, adMobResponse.adMobModel.bannerStatus);
                    SharedPref.getSharedPref(context).write(Constants.Preferences.BANNER_APP_ID, adMobResponse.adMobModel.bannerId);
                    SharedPref.getSharedPref(context).write(Constants.Preferences.BANNER_UNIT_ID, adMobResponse.adMobModel.bannerUnitId);
                    SharedPref.getSharedPref(context).write(Constants.Preferences.INTERSTITIAL_STATUS, adMobResponse.adMobModel.interstitialStatus);
                    SharedPref.getSharedPref(context).write(Constants.Preferences.INTERSTITIAL_APP_ID, adMobResponse.adMobModel.interstitialId);
                    SharedPref.getSharedPref(context).write(Constants.Preferences.INTERSTITIAL_UNIT_ID, adMobResponse.adMobModel.interstitialUnitId);
                }
            }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_token", Constants.ServerUrl.API_TOKEN);
                    return params;
                }
            };
            queue.getCache().clear();
            queue.add(stringRequest);

        } else Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
    }

    private InterstitialAd mInterstitialAd;

    /**
     * setting interstitial ad property
     *
     * @param activity
     */
    void setInterstitialApp(Activity activity)  {
        String status = SharedPref.getSharedPref(activity).read(Constants.Preferences.INTERSTITIAL_STATUS);
        String AppID = SharedPref.getSharedPref(activity).read(Constants.Preferences.INTERSTITIAL_APP_ID);
        if (status.equals(Constants.Preferences.STATUS_ON)) {
            if (AppID != null) {
                try {
                    ApplicationInfo applicationInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
                    applicationInfo.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", AppID);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                MobileAds.initialize(activity,
                        AppID);
                prepareAd(activity);
                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                } else {
                                    Log.d("TAG", " Interstitial not loaded");
                                }
                                prepareAd(activity);
                            }
                        });
                    }
                }, 10, 5, TimeUnit.MINUTES);
            }
        }
    }

    /**
     * init interstitial ad
     *
     * @param activity
     */
    private void prepareAd(Activity activity) {
        String unitID = SharedPref.getSharedPref(activity).read(Constants.Preferences.INTERSTITIAL_UNIT_ID);
        mInterstitialAd = new InterstitialAd(activity);

        mInterstitialAd.setAdUnitId(unitID);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
    }

    /**
     * getting setting credential
     *
     * @param context
     */
    public void getSettingCredential(final Context context) {
        isNetworkAvailable = NetworkHelper.hasNetworkAccess(context);
        if (isNetworkAvailable) {

            String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.SETTINGS_URL;
            RequestQueue queue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        SettingsResponse settingsResponse;
                        Type admob = new TypeToken<SettingsResponse>() {
                        }.getType();
                        settingsResponse = new Gson().fromJson(response, admob);
                        CustomSharedPrefs.setCurrency(context, settingsResponse.settingsModel.currencyFont);
                        SharedPref.getSharedPref(context).write(Constants.Preferences.TAX, settingsResponse.settingsModel.tax);
                        SharedPref.getSharedPref(context).write(Constants.Preferences.MERCHANT_ID, settingsResponse.settingsModel.paymentModel.merchantId);
                        SharedPref.getSharedPref(context).write(Constants.Preferences.ENVIRONMENT, settingsResponse.settingsModel.paymentModel.environment);
                        SharedPref.getSharedPref(context).write(Constants.Preferences.PUBLIC_KEY, settingsResponse.settingsModel.paymentModel.publicKey);
                        SharedPref.getSharedPref(context).write(Constants.Preferences.PRIVATE_KEY, settingsResponse.settingsModel.paymentModel.privateKey);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_token", Constants.ServerUrl.API_TOKEN);
                    return params;
                }
            };
            queue.getCache().clear();
            queue.add(stringRequest);

        } else Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
    }


}
