package com.clothly.ecommerce.data.ui.emailverification;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.NetworkHelper;

import java.util.HashMap;
import java.util.Map;

public class EmailVerificationPresenter extends BasePresenter<EmailVerificationMvpView> {

    /**
     * checking email validation
     */
    public boolean emailValidity(String text) {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    /**
     * getting email varification response from server
     *
     * @param email
     * @param pinCode
     * @param context
     */
    public void doEmailVerification(final String email
            , final String pinCode
            , final Context context) {

        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.EMAIL_VERIFICATION;
        RequestQueue queue = Volley.newRequestQueue(context);

        if (NetworkHelper.hasNetworkAccess(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    UserRegistrationResponse user = new Gson().fromJson(response, UserRegistrationResponse.class);

                    getMvpView().onEmailVerificationSuccess(user);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getMvpView().onEmailVeridicationError(error.getMessage());
                    Log.i("SHAP", "" + error.networkResponse.statusCode);
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_token", Constants.ServerUrl.API_TOKEN);
                    params.put("email", email);
                    params.put("verification_token", pinCode);
                    return params;
                }
            };
            queue.getCache().clear();
            queue.add(stringRequest);
        } else {
            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * resend code to get pin number
     * @param email
     * @param context
     */
    public void resendCode(final String email, final Context context) {
        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.RESEND_CODE;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        if (NetworkHelper.hasNetworkAccess(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    UserRegistrationResponse user = new Gson().fromJson(response, UserRegistrationResponse.class);
                    getMvpView().onResendCodeSuccess(user);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getMvpView().onResendCodeError(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("api_token", Constants.ServerUrl.API_TOKEN);
                    map.put("email", email);
                    return map;
                }
            };
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
        }


    }
}
