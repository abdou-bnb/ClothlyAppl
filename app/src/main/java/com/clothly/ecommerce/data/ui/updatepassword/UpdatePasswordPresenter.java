package com.clothly.ecommerce.data.ui.updatepassword;

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
import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.NetworkHelper;

import java.util.HashMap;
import java.util.Map;

public class UpdatePasswordPresenter extends BasePresenter<UpdatePasswordMvpView> {
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
     * hitting server to update password
     *
     * @param email
     * @param verifyCode
     * @param password
     * @param context
     */
    public void updatePassword(final String email, final String verifyCode, final String password, final Context context) {
        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.UPDATE_PASSWORD;
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        if (NetworkHelper.hasNetworkAccess(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    UserRegistrationResponse user = new Gson().fromJson(response, UserRegistrationResponse.class);
                    getMvpView().onPasswordUpdateSuccess(user);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getMvpView().onPasswordUpdateError(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("api_token", Constants.ServerUrl.API_TOKEN);
                    map.put("email", email);
                    map.put("verification_token", verifyCode);
                    map.put("password", password);
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
