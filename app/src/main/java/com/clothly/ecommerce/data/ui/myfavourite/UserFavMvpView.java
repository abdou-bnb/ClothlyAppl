package com.clothly.ecommerce.data.ui.myfavourite;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.ProductGridResponse;

public interface UserFavMvpView extends MvpView {
    void onGettingFavouriteSuccess(ProductGridResponse response);
    void onGettingFavouriteError(String errorMessage);
}
