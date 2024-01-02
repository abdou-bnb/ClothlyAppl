package com.clothly.ecommerce.data.ui.checkout;

import com.clothly.ecommerce.data.data.helper.base.MvpView;

public interface CheckOutMvpView extends MvpView {
    void onPaymentNonceSuccess(String response);
    void  onPaymentNonceError(String errorMessage);

    void allPaymentSuccess(String response);
    void allPaymentError(String errorMessage);

}
