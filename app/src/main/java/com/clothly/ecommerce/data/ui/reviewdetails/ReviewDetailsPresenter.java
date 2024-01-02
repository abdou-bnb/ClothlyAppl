package com.clothly.ecommerce.data.ui.reviewdetails;

import android.content.Context;

import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.response.FeedBackResponse;
import com.clothly.ecommerce.data.data.provider.reposervices.ApiService;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.NetworkHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewDetailsPresenter extends BasePresenter<ReviewDetailsMvpView> {


    /**
     * Show review data from server
     * @param context mActivity
     * @param itemId itemId
     */
    public void showReviewFromServer(Context context,String itemId){

        if (NetworkHelper.hasNetworkAccess(context)) {
            ApiService getRestInfoApi = RetrofitClient.getApiService();
            Call<FeedBackResponse> call=getRestInfoApi.showReview(Constants.ServerUrl.API_TOKEN,itemId);
            call.enqueue(new Callback<FeedBackResponse>() {
                @Override
                public void onResponse(Call<FeedBackResponse> call, Response<FeedBackResponse> response) {
                    if (response.isSuccessful() && response.body() != null){
                        FeedBackResponse feedBackResponse=response.body();
                        getMvpView().onGettingShowReviewSuccess(feedBackResponse.getFeedBackModel());
                    }
                }

                @Override
                public void onFailure(Call<FeedBackResponse> call, Throwable t) {
                        getMvpView().onGettingShowReviewError(t.getMessage());
                }
            });
        }
    }

}
