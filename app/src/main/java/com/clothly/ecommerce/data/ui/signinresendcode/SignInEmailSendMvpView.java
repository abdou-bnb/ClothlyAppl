package com.clothly.ecommerce.data.ui.signinresendcode;

import com.clothly.ecommerce.data.data.helper.base.MvpView;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;

public interface SignInEmailSendMvpView extends MvpView {
   void onGetCodeSuccess(UserRegistrationResponse user);
   void onGetCodeError(String errorMessage);
}
