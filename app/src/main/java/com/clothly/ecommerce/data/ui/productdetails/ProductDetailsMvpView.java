package com.clothly.ecommerce.data.ui.productdetails;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.AddFavouriteResponse;
import com.clothly.ecommerce.data.data.helper.response.ProductDetailsResponse;

public interface ProductDetailsMvpView extends MvpView {
    /**
     * this interface is used to create callback to pass server response
     */


    void onProductDetailsSuccess(ProductDetailsResponse detailsResponse);

    void onProductDetailsError(String errorMessage);

    void onFavSuccess(AddFavouriteResponse response);

    void onFavError(String errorMessage);

    void onRemoveFavSuccess(AddFavouriteResponse response);
}
