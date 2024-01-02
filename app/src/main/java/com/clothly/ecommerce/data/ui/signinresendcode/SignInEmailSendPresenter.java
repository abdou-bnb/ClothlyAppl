package com.clothly.ecommerce.data.ui.signinresendcode;

import android.content.Context;
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

public class SignInEmailSendPresenter extends BasePresenter<SignInEmailSendMvpView> {

    /**
     * checking email validation
     */
    public boolean emailValidity(String text) {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }

    /**
     * resend code to email for getting pin code
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
                    getMvpView().onGetCodeSuccess(user);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getMvpView().onGetCodeError(error.getMessage());
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
