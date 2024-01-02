package com.clothly.ecommerce.data.ui.ordercomplete;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.OrderListResponse;

public interface OrderCompleteMvpView extends MvpView {
    void onGettingOrderInfoSuccess(OrderListResponse response);
    void onGettingOrderInfoError(String errorMessage);
}
