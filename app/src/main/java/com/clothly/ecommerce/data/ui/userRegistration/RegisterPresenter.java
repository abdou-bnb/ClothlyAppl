package com.clothly.ecommerce.data.ui.userRegistration;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
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

public class RegisterPresenter extends BasePresenter<RegisterMvpView> {


    /**
     * checking name validation
     */
    public boolean nameValidity(String text) {
        return text.length() >= Constants.DefaultValue.MINIMUM_LENGTH_TEXT
                && text.length() <= Constants.DefaultValue.MAXIMUM_LENGTH;
    }

    /**
     * checking email validation
     */
    public boolean emailValidity(String text) {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    /**
     * checking password validation
     */
    public boolean passwordValidity(String text) {
        return text.length() >= Constants.DefaultValue.MINIMUM_LENGTH_PASS
                && text.length() <= Constants.DefaultValue.MAXIMUM_LENGTH;
    }

    /**
     * checking password and confirm password validation
     */
    public boolean isConfirmPasswordMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    /**
     * this api is used  to sign up with email
     * @param name
     * @param email
     * @param password
     * @param context
     */
    public void userRegistration(final String name
            , final String email
            , final String password
            , final Context context) {

        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.SIGNUP_URL;
        RequestQueue queue = Volley.newRequestQueue(context);

        if(NetworkHelper.hasNetworkAccess(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("RR", response);
                    UserRegistrationResponse user = new Gson().fromJson(response, UserRegistrationResponse.class);

                    getMvpView().onSignUpSuccess(user);
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getMvpView().onSignUpError(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_token", Constants.ServerUrl.API_TOKEN);
                    params.put("type", "" + Constants.RegistrationType.MANUAL_SIGN_UP);
                    params.put("social_id", "");
                    params.put("username", name);
                    params.put("email", email);
                    params.put("password", password);
                    return params;
                }
            };
            stringRequest.setRetryPolicy(new RetryPolicy() {
                @Override
                public int getCurrentTimeout() {
                    return 50000;
                }
                @Override
                public int getCurrentRetryCount() {
                    return 50000;
                }
                @Override
                public void retry(VolleyError error) throws VolleyError {

                }
            });
            queue.getCache().clear();
            queue.add(stringRequest);
        }else{
            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
        }
    }
}
