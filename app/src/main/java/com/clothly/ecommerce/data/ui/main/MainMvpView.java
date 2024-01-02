package com.clothly.ecommerce.data.ui.main;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.MainPageResponse;

public interface MainMvpView extends MvpView {

    void onGettingHomePageDataSuccess(MainPageResponse mainPageResponse);
    void onGettingHomePageDataError(String error);

}
