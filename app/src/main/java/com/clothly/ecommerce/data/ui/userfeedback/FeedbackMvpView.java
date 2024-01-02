package com.clothly.ecommerce.data.ui.userfeedback;

import android.graphics.Bitmap;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.FeedBackResponse;

public interface FeedbackMvpView extends MvpView {
    void onGettingBitmap(Bitmap bitmap);
    void onGettingImagePath(String imagePath);

    void onGettingReviewSuccess(FeedBackResponse feedBackResponse);

    void onGettingReviewError(String error);
}
