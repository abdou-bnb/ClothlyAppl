package com.clothly.ecommerce.data.ui.emailverification;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;

public interface EmailVerificationMvpView extends MvpView {
    void onEmailVerificationSuccess(UserRegistrationResponse user);

    void onEmailVeridicationError(String errorMessage);

    void onResendCodeSuccess(UserRegistrationResponse user);

    void onResendCodeError(String errorMessage);
}
