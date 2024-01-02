package com.clothly.ecommerce.data.ui.search;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.AddFavouriteResponse;
import com.clothly.ecommerce.data.data.helper.response.ProductGridResponse;

public interface SearchMvpView extends MvpView {
    void onSearchSuccess(ProductGridResponse response);

    void onSearchError(String errorMessage);

    void onFavSuccess(AddFavouriteResponse response);
    void onFavError(String errorMessage);

    void onRemoveFavSuccess(AddFavouriteResponse response);
}
