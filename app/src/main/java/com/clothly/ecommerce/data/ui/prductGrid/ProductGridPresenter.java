package com.clothly.ecommerce.data.ui.prductGrid;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.clothly.ecommerce.data.data.helper.response.AddFavouriteResponse;
import com.clothly.ecommerce.data.data.helper.response.ProductGridResponse;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.NetworkHelper;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class ProductGridPresenter extends BasePresenter<ProductGridMvpView> {

    /**
     * getting product list by category from server
     *
     * @param categoryId
     * @param context
     * @param userId
     * @param page
     */
    public void getProductList(final String categoryId,
                               final Context context,
                               final String userId,
                               final String page) {
        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.PRODUCT_GRID_URL;
        RequestQueue queue = Volley.newRequestQueue(context);
        if (NetworkHelper.hasNetworkAccess(context)) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, response -> {
                if (response != null) {
                    Type listType = new TypeToken<ProductGridResponse>() {
                    }.getType();
                    ProductGridResponse productGridResponse = new Gson().fromJson(response, listType);
                    if (productGridResponse !=null){
                        getMvpView().onProductListSuccess(productGridResponse);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    getMvpView().onProductListError(error.getMessage());
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_token", Constants.ServerUrl.API_TOKEN);
                    params.put("category_id", categoryId);
                    params.put("user_id", userId);
                    params.put("page", page);
                    return params;
                }
            };

            queue.getCache().clear();
            queue.add(stringRequest);
        } else {
            Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * add favourite to product
     *
     * @param context
     * @param itemId
     * @param userId
     */
    public void getAddFavouriteResponse(Context context, String itemId, String userId) {
        if (NetworkHelper.hasNetworkAccess(context)) {
            RetrofitClient.getApiService().addFavourite(Constants.ServerUrl.API_TOKEN, itemId, userId).enqueue(new Callback<AddFavouriteResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddFavouriteResponse> call, @NonNull retrofit2.Response<AddFavouriteResponse> response) {
                    if (response.body() != null) {
                        getMvpView().onFavSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AddFavouriteResponse> call, @NonNull Throwable t) {
                    getMvpView().onFavError(t.getMessage());
                }
            });
        } else {
            Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * remove favourite
     *
     * @param context
     * @param itemId
     * @param userId
     */
    public void getRemoveFavouriteResponse(Context context, String itemId, String userId) {
        if (NetworkHelper.hasNetworkAccess(context)) {
            RetrofitClient.getApiService().removeFavourite(Constants.ServerUrl.API_TOKEN, itemId, userId).enqueue(new Callback<AddFavouriteResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddFavouriteResponse> call, @NonNull retrofit2.Response<AddFavouriteResponse> response) {
                    if (response.body() != null) {
                        getMvpView().onRemoveFavSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AddFavouriteResponse> call, @NonNull Throwable t) {
                    getMvpView().onFavError(t.getMessage());
                }
            });
        } else {
            Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }


}
