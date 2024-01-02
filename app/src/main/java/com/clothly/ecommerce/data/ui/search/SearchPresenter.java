package com.clothly.ecommerce.data.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.response.AddFavouriteResponse;
import com.clothly.ecommerce.data.data.helper.response.ProductGridResponse;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.NetworkHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter extends BasePresenter<SearchMvpView> {
    /**
     * getting products by search text from server
     *
     * @param context
     * @param searchText
     * @param page
     * @param userID
     */

    public void makeSearch(Context context, String searchText, String page, String userID) {
        if (NetworkHelper.hasNetworkAccess(context)) {
            RetrofitClient.getApiService().searchResult(Constants.ServerUrl.API_TOKEN, page, userID, searchText).enqueue(new Callback<ProductGridResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProductGridResponse> call, @NonNull Response<ProductGridResponse> response) {
                    if (response.body() != null) {
                        getMvpView().onSearchSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProductGridResponse> call, @NonNull Throwable t) {
                    getMvpView().onSearchError(t.getMessage());
                }
            });
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
                public void onResponse(@NonNull Call<AddFavouriteResponse> call, @NonNull Response<AddFavouriteResponse> response) {
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
                public void onResponse(@NonNull Call<AddFavouriteResponse> call, @NonNull Response<AddFavouriteResponse> response) {
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
