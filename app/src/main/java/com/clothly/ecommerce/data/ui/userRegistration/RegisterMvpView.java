package com.clothly.ecommerce.data.ui.userRegistration;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;

public interface RegisterMvpView extends MvpView {
    void onSignUpSuccess(UserRegistrationResponse user);
    void onSignUpError(String errorMessage);
}
