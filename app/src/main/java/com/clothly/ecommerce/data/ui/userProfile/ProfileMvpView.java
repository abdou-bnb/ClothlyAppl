package com.clothly.ecommerce.data.ui.userProfile;


import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.UploadImageResponse;
import com.clothly.ecommerce.data.data.helper.response.UserAddressResponse;
import com.clothly.ecommerce.data.data.helper.response.UserMultipleAddressResponse;

public interface ProfileMvpView extends MvpView {
    void onSetAddressSuccess(UserAddressResponse address);

    void onRemoveAddressSuccess(UserAddressResponse address);

    void onSetAddressError(String errorMessage);

    void onGettingImageSuccess(UploadImageResponse response);

    void onGettingImageError(String errorMessage);

    void onGettingAllAddressSuccess(UserMultipleAddressResponse response);
}
