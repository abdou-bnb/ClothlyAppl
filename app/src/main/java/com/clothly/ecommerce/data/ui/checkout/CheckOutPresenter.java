package com.clothly.ecommerce.data.ui.checkout;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.response.SettingsResponse;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.NetworkHelper;
import com.clothly.ecommerce.data.data.util.SharedPref;


import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class CheckOutPresenter extends BasePresenter<CheckOutMvpView> {

    /**
     * Getting payment nonce from server
     *
     * @param context
     * @param paymentNonce
     * @param Amounts
     */
    void sendPaymentNonceToServer(Context context, String paymentNonce, String Amounts) {

        String sandBox = SharedPref.getSharedPref(context).read(Constants.Preferences.ENVIRONMENT);
        String merchant = SharedPref.getSharedPref(context).read(Constants.Preferences.MERCHANT_ID);
        String publicID = SharedPref.getSharedPref(context).read(Constants.Preferences.PUBLIC_KEY);
        String privateID = SharedPref.getSharedPref(context).read(Constants.Preferences.PRIVATE_KEY);

        RetrofitClient.getApiService().paymentNonce(paymentNonce, Amounts, sandBox, merchant, publicID, privateID).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.body() != null)
                    getMvpView().onPaymentNonceSuccess(response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                getMvpView().onPaymentNonceError(t.getMessage());

            }
        });
    }

    /**
     * this api is used to confirm payment
     *
     * @param context
     * @param paymentMethod
     * @param totalPrice
     * @param inventoryList
     * @param address
     * @param userID
     */
    public void doPayment(final Context context, final String paymentMethod, final String totalPrice,
                          final String inventoryList, final String address, final String userID) {

        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.ORDER_URL;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                getMvpView().allPaymentSuccess(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                getMvpView().allPaymentError(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("api_token", Constants.ServerUrl.API_TOKEN);
                params.put("method", paymentMethod);
                params.put("address", address);
                params.put("user", userID);
                params.put("amount", totalPrice);
                params.put("ordered_products", inventoryList);

                return params;

            }
        };
        queue.getCache().clear();
        queue.add(stringRequest);
    }

    /**
     * getting setting credential
     *
     * @param context
     */
    public void getSettingCredential(final Context context) {
        if (NetworkHelper.hasNetworkAccess(context)) {

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
