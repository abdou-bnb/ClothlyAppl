package com.clothly.ecommerce.data.ui.userLogin;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;

public interface LoginMvpView extends MvpView {
    void onEmailSignUpSuccess(UserRegistrationResponse user);
    void onEmainSignUpError(String errorMessage);

    void onSocialSignUpSuccess(UserRegistrationResponse user);
    void onSocialSignUpError(String errorMessage);
}
