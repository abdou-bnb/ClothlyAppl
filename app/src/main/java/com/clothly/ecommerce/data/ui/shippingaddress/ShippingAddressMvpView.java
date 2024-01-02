package com.clothly.ecommerce.data.ui.shippingaddress;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.AvailableInventoryResponse;
import com.clothly.ecommerce.data.data.helper.response.UserAddressResponse;
import com.clothly.ecommerce.data.data.helper.response.UserMultipleAddressResponse;

public interface ShippingAddressMvpView extends MvpView {
    void onGetAvailableAddressSuccess(UserAddressResponse addressResponse);
    void onGetAvailableAddressError(String errorMessage);
    void onGettingAllAddressSuccess(UserMultipleAddressResponse response);

    void onAvailableInventorySuccess(AvailableInventoryResponse response);
    void onAvailableInventoryError(String errorMessage);

    void onBrainTreeClientTokenSuccess(String tokenResponse);
    void onBrainTreeClientTokenError(String errorMessage);

}
