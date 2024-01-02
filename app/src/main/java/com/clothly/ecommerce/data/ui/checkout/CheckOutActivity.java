package com.clothly.ecommerce.data.ui.checkout;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.helper.database.DatabaseUtil;
import com.clothly.ecommerce.data.data.helper.models.AddressModel;
import com.clothly.ecommerce.data.data.helper.models.CustomProductInventory;
import com.clothly.ecommerce.data.data.helper.models.InventoryServerModel;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.SharedPref;
import com.clothly.ecommerce.data.data.util.UtilityClass;
import com.clothly.ecommerce.data.databinding.ActivityCheckOutBinding;
import com.clothly.ecommerce.data.ui.addcart.CartActivity;
import com.clothly.ecommerce.data.ui.ordercomplete.OrderCompleteActivity;
import com.clothly.ecommerce.data.ui.productdetails.ProductDetailsActivity;
import com.clothly.ecommerce.data.ui.shippingaddress.ShippingAddressActivity;

import java.util.ArrayList;
import java.util.List;

public class CheckOutActivity extends BaseActivity<CheckOutMvpView, CheckOutPresenter> implements CheckOutMvpView {
    private ActivityCheckOutBinding mBinding;
    private Toolbar toolbar;
    private int paymentMethod;
    private String paymentResponse;
    private static final int BRAINTREE_REQUEST_CODE = 4949;
    private float totalAmount;
    private Loader mLoader;
    private List<CustomProductInventory> inventoryList;
    private String paymentMethods = "";
    private List<InventoryServerModel> serverModels = new ArrayList<>();
    private String address1 = "";
    private String addressID;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_check_out;
    }

    @Override
    protected void startUI() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        mLoader = new Loader(this);
        presenter.getSettingCredential(this);
        initToolbar();
        getDataFromIntent();
        setValueToView();
    }

    private void setValueToView() {
        String address = SharedPref.getSharedPref(this).read(Constants.Preferences.USER_ADDRESS);
        AddressModel addressModel = UtilityClass.stringToAddressModel(address);
        if (addressModel != null) {
            addressID = "" + addressModel.id;
            address1 = addressModel.addressLine1 + addressModel.addressLine2
                    + ", City : " + addressModel.city + ", zip Code : " + addressModel.zipCode
                    + ", State : " + addressModel.state + ", Country :" + addressModel.country;
            mBinding.textViewAddress1.setText(address1);
        }
        if (paymentMethod != 0) {
            if (paymentMethod == 1) {
                mBinding.textViewPayment.setText(getString(R.string.payment_paypal));
            } else {
                mBinding.textViewPayment.setText(getString(R.string.creditordebit));
            }
        }

        String currency = CustomSharedPrefs.getCurrency(this);
        float amount = SharedPref.getSharedPref(this).readFloat(Constants.IntentKey.TOTAL_AMOUNT);
        int tax = SharedPref.getSharedPref(this).readInt(Constants.Preferences.TAX);
        float taxPrice = (tax * amount) / 100;

        mBinding.textViewSubtotalAmount.setText(currency + "" + amount);
        mBinding.textViewDiscount.setText("Tax(" + tax + "%)");
        mBinding.textViewDiscountAmount.setText(currency + "" + taxPrice);

        //  mBinding.textViewCostAmount.setText(currency + "" + shippingCost);

        totalAmount = amount + taxPrice;
        mBinding.textViewTotalAmount.setText(currency + "" + totalAmount);
        mBinding.textTotalCostTitle.setText(currency + "" + totalAmount);

        mBinding.buttonContinue.setOnClickListener(this);

    }

    private void getDataFromIntent() {
        paymentMethod = getIntent() == null ? 0 : getIntent().getIntExtra(Constants.IntentKey.PAYMENT_METHOD, 0);
        paymentResponse = getIntent() == null ? "" : getIntent().getStringExtra(Constants.IntentKey.PAYMENT_RESPONSE);

    }

    /**
     * init toolbar with title
     */
    private void initToolbar() {
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(getString(R.string.check_out));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void stopUI() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.button_continue) {
            onBraintreeSubmit();
        }
    }

    @Override
    protected CheckOutPresenter initPresenter() {
        return new CheckOutPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BRAINTREE_REQUEST_CODE) {
            if (RESULT_OK == resultCode) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                String paymentNonce = result.getPaymentMethodNonce().getNonce();

                String payamount = String.valueOf(totalAmount);

                mLoader.show();
                presenter.sendPaymentNonceToServer(this, paymentNonce, payamount);
            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);

            }
        }
    }

    /**
     * this api is for initializing {@link DropInRequest} to get callback in {@link #onActivityResult}
     */
    private void onBraintreeSubmit() {
        if (paymentResponse != null) {
            DropInRequest dropInRequest = new DropInRequest().clientToken(paymentResponse);
            startActivityForResult(dropInRequest.getIntent(this), BRAINTREE_REQUEST_CODE);
        }
    }

    @Override
    public void onPaymentNonceSuccess(String response) {
        if (response != null) {
            String[] separated = response.split("_");
            if (separated.length == 2)
                paymentMethods = UtilityClass.capFirstLetter(separated[0]) + " " + UtilityClass.capFirstLetter(separated[1]);
            else paymentMethods = response;


            AsyncTask.execute(() -> {
                inventoryList = DatabaseUtil.on().getAllCodes();
                for (CustomProductInventory inventory : inventoryList) {
                    InventoryServerModel model = new InventoryServerModel();
                    model.inventory = "" + inventory.inventory_id;
                    model.price = "" + inventory.price;
                    model.product = "" + inventory.product_id;
                    model.quantity = "" + inventory.currentQuantity;

                    serverModels.add(model);
                }
                String inventoryLists = UtilityClass.objectToStrings(serverModels);
                presenter.doPayment(CheckOutActivity.this, paymentMethods,
                        "" + totalAmount, inventoryLists, addressID, CustomSharedPrefs.getLoggedInUserId(CheckOutActivity.this));

            });
        }
        mLoader.stopLoader();

    }

    @Override
    public void onPaymentNonceError(String errorMessage) {
        mLoader.stopLoader();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void allPaymentSuccess(String response) {
        AsyncTask.execute(() -> {
            DatabaseUtil.on().deleteAll();

            runOnUiThread(() -> {
                mLoader.stopLoader();
                startActivity(new Intent(CheckOutActivity.this, OrderCompleteActivity.class));
                if (ProductDetailsActivity.productDetailsActivity != null)
                    ProductDetailsActivity.productDetailsActivity.finish();
                if (CartActivity.cartActivity != null)
                    CartActivity.cartActivity.finish();
                if (ShippingAddressActivity.shippingAddressActivity != null)
                    ShippingAddressActivity.shippingAddressActivity.finish();
                finish();
            });
        });

    }

    @Override
    public void allPaymentError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        mLoader.stopLoader();
    }

}
