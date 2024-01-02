package com.clothly.ecommerce.data.ui.reviewdetails;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.models.FeedBackModel;

public interface ReviewDetailsMvpView extends MvpView {
    void onGettingShowReviewSuccess(FeedBackModel feedBackModel);

    void onGettingShowReviewError(String error);
}
