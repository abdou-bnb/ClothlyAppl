package com.clothly.ecommerce.data.ui.offerproduct;

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

public class OfferProductPresenter extends BasePresenter<OfferProductMvpView> {
    public void getOfferProduct(Context context, String tag, String pageNumber) {
        RetrofitClient.getApiService().getOfferProductList(Constants.ServerUrl.API_TOKEN,
                tag, pageNumber).enqueue(new Callback<ProductGridResponse>() {
            @Override
            public void onResponse(@NonNull  Call<ProductGridResponse> call,@NonNull Response<ProductGridResponse> response) {
                if(response != null){
                    if (response.body() != null) {
                        getMvpView().onOfferProductSuccess(response.body());
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<ProductGridResponse> call,@NonNull Throwable t) {
                getMvpView().onOfferProductError(t.getMessage());
            }
        });

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
                    if (response.isSuccessful() && response.body() != null) {
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
