package com.clothly.ecommerce.data.ui.updatepassword;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;

public interface UpdatePasswordMvpView extends MvpView {
    void onPasswordUpdateSuccess(UserRegistrationResponse user);
    void onPasswordUpdateError(String errorMessage);
}
