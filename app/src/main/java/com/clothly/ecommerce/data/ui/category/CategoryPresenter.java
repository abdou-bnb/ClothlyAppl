package com.clothly.ecommerce.data.ui.category;

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
import com.clothly.ecommerce.data.data.helper.response.AllCategoryResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.NetworkHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CategoryPresenter extends BasePresenter<CategoryMvpView> {

    public void getCategories(Context context) {
        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.CATEGORY_URL;
        RequestQueue queue = Volley.newRequestQueue(context);
        if (NetworkHelper.hasNetworkAccess(context)) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response != null) {
                        Type listType = new TypeToken<AllCategoryResponse>() {
                        }.getType();

                        AllCategoryResponse categoryResponse = new Gson().fromJson(response, listType);
                        getMvpView().onCategoryListSuccess(categoryResponse);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getMvpView().onCategoryListError(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_token", Constants.ServerUrl.API_TOKEN);
                   // params.put("page", page);
                    return params;
                }
            };

            queue.getCache().clear();
            queue.add(stringRequest);
        } else {
            Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }
}
