package com.clothly.ecommerce.data.ui.shippingaddress;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.database.DatabaseUtil;
import com.clothly.ecommerce.data.data.helper.models.AddressModel;
import com.clothly.ecommerce.data.data.helper.models.CustomProductInventory;
import com.clothly.ecommerce.data.data.helper.models.InventoryModel;
import com.clothly.ecommerce.data.data.helper.response.AvailableInventoryResponse;
import com.clothly.ecommerce.data.data.helper.response.UserAddressResponse;
import com.clothly.ecommerce.data.data.helper.response.UserMultipleAddressResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.SharedPref;
import com.clothly.ecommerce.data.databinding.ActivityShippingAddressBinding;
import com.clothly.ecommerce.data.ui.checkout.CheckOutActivity;
import com.clothly.ecommerce.data.ui.productdetails.ProductDetailsActivity;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class ShippingAddressActivity extends BaseActivity<ShippingAddressMvpView, ShippingAddressPresenter> implements ShippingAddressMvpView , ItemClickListener<AddressModel> {

    private Toolbar toolbar;
    private ActivityShippingAddressBinding mBinding;
    private EditText etAddress1, etAddress2, etCity, etZip, etState, etCountry;
    private String address1 = "", address2 = "", city = "", zip = "", state = "", country = "";

    boolean isFieldEmpty;
    private FrameLayout btnCompleteAddress;
    private Loader mLoader;
    private Dialog dialog;
    private float totalPrice;
    StringBuilder inventoryIds = new StringBuilder(100);
    private List<CustomProductInventory> inventoryList;
    private List<InventoryModel> availableList;
    private boolean isAvailable;
    public String clientToken;
    public boolean isPaymentClicked;
    public int paymentMethod;
    public int clickedRadio = 0;
    private UserAddressResponse addressResponses;
    public static ShippingAddressActivity shippingAddressActivity;
    boolean isAddressInput;
    private ShippingAddressAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shipping_address;
    }

    @Override
    protected void startUI() {
        mBinding = DataBindingUtil.setContentView(this, getLayoutId());
        initToolbar();
        mLoader = new Loader(this);
        getListFromDataBase();
        totalPrice = SharedPref.getSharedPref(this).readFloat(Constants.IntentKey.TOTAL_AMOUNT);
        mBinding.textTotalCostTitle.setText(CustomSharedPrefs.getCurrency(this) + "" + totalPrice);
        setClickListener(mBinding.layoutAddress.radioCurrentAddress2, mBinding.buttonContinue, mBinding.layoutPayment.radioPaypal,
                mBinding.layoutPayment.radioCredit);
        isPaymentClicked = false;
        shippingAddressActivity = this;
        mAdapter = new ShippingAddressAdapter(new ArrayList<>(), this);
        mBinding.layoutAddress.recyclerViewAddress.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mBinding.layoutAddress.recyclerViewAddress.setAdapter(mAdapter);
        mAdapter.setItemClickListener(this);

        presenter.getAllAddress(this, ""+CustomSharedPrefs.getLoggedInUserId(this));
    }

    /**
     * getting list from database
     */
    private void getListFromDataBase() {
        AsyncTask.execute(() -> {
            inventoryList = DatabaseUtil.on().getAllCodes();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (inventoryList != null && inventoryList.size() > 0) {
                        for (CustomProductInventory inventory : inventoryList) {
                            inventoryIds.append(inventory.inventory_id + ",");
                        }
                        mLoader.show();
                        presenter.getAvailableInventory(ShippingAddressActivity.this, String.valueOf(inventoryIds));
                    }
                }
            });
        });
    }



    /**
     * init toolbar
     */
    private void initToolbar() {
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_shipping_add));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void stopUI() {
        if (shippingAddressActivity != null)
            shippingAddressActivity = null;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.radio_current_address_2:
                //add current address
                mBinding.layoutAddress.radioCurrentAddress2.setChecked(false);
                openAddressPopUp();

                break;

            case R.id.btn_complete_address:
                //validation address
                validateAddress();
                if (!isFieldEmpty) {
                    mLoader.show();
                    if (address1.equals("")) {
                        address1 = "";
                    } else if (address2.equals("")) {
                        address2 = "";
                    }
                    //update address to server
                    presenter.updateAddress(this, address1, address2, city, zip, state, country);
                }
                break;

            case R.id.radio_paypal:
                isPaymentClicked = true;
                paymentMethod = 1;
                mBinding.layoutPayment.radioCredit.setChecked(false);
                break;

            case R.id.radio_credit:
                isPaymentClicked = true;
                paymentMethod = 2;
                mBinding.layoutPayment.radioPaypal.setChecked(false);
                break;

            case R.id.button_continue:

                if (availableList != null && availableList.size() > 0) {
                    if (inventoryList.size() > 0) {
                        for (CustomProductInventory productInventory : inventoryList) {
                            int quantity = 0, proId = 0;
                            quantity = productInventory.currentQuantity;
                            proId = productInventory.inventory_id;

                            for (InventoryModel inventoryModel : availableList) {
                                if (inventoryModel.id == proId) {
                                    if (inventoryModel.quantity >= quantity) {
                                        isAvailable = true;
                                    } else {
                                        isAvailable = false;
                                    }
                                } else {
                                    isAvailable = false;
                                }
                            }
                        }
                    }
                }

                if (isAvailable) {
                    if (isPaymentClicked) {
                        //clickedRadio = 1 means address is given before
                        if (clickedRadio != 0) {
                            //address taken
                            mLoader.show();

                            presenter.getClientTokenFromServer(this);
                        } else {
                            Toast.makeText(this, "Please add address!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Please choose payment method!", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(this, "Your product is out of stock!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * checking validation of address field
     */
    private void validateAddress() {

        address1 = etAddress1.getText().toString().trim();
        address2 = etAddress2.getText().toString().trim();
        city = etCity.getText().toString().trim();
        zip = etZip.getText().toString().trim();
        state = etState.getText().toString().trim();
        country = etCountry.getText().toString().trim();

        if (address1.equals("")) {
            isFieldEmpty = true;
            etAddress1.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etAddress1.setBackgroundResource(R.drawable.edittext_round);
        }
        if (city.equals("")) {
            isFieldEmpty = true;
            etCity.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etCity.setBackgroundResource(R.drawable.edittext_round);
        }
        if (zip.equals("")) {
            isFieldEmpty = true;
            etZip.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etZip.setBackgroundResource(R.drawable.edittext_round);
        }
        if (state.equals("")) {
            isFieldEmpty = true;
            etState.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etState.setBackgroundResource(R.drawable.edittext_round);
        }
        if (country.equals("")) {
            isFieldEmpty = true;
            etCountry.setBackgroundResource(R.drawable.edittext_error);
        } else {
            isFieldEmpty = false;
            etCountry.setBackgroundResource(R.drawable.edittext_round);
        }
    }

    /**
     * open address pop up to update address
     */
    private void openAddressPopUp() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_alert_address, null);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);
        dialog = alertDialogBuilder.create();
        dialog.show();

        etAddress1 = dialog.findViewById(R.id.et_shipping_address_1);
        etAddress2 = dialog.findViewById(R.id.et_shipping_address_2);
        etCity = dialog.findViewById(R.id.et_shipping_city);
        etZip = dialog.findViewById(R.id.et_shipping_zip);
        etState = dialog.findViewById(R.id.et_shipping_state);
        etCountry = dialog.findViewById(R.id.et_shipping_country);

        btnCompleteAddress = dialog.findViewById(R.id.btn_complete_address);
        btnCompleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAddress();
                if (!isFieldEmpty) {
                    isAddressInput = true;
                    mLoader.show();
                    hideKeyboardFrom(ShippingAddressActivity.this,view);
                    presenter.updateAddress(ShippingAddressActivity.this, address1, address2, city, zip, state, country);
                } else {

                }
            }
        });
    }

    @Override
    protected ShippingAddressPresenter initPresenter() {
        return new ShippingAddressPresenter();
    }

    @Override
    public void onGetAvailableAddressSuccess(UserAddressResponse addressResponse) {
        if (dialog != null) dialog.dismiss();
        mLoader.stopLoader();
        if (addressResponse != null && addressResponse.statusCode == HttpURLConnection.HTTP_OK) {
            mAdapter.addItem(addressResponse.addressModel);
        }


    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }



    @Override
    public void onGetAvailableAddressError(String errorMessage) {
        if (dialog != null) dialog.dismiss();
        mLoader.stopLoader();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        clickedRadio = 0;
    }

    @Override
    public void onGettingAllAddressSuccess(UserMultipleAddressResponse response) {
        mLoader.stopLoader();
        if (response != null && response.statusCode == HttpURLConnection.HTTP_OK) {
            if (!response.addressModel.isEmpty()) {
                mAdapter.addListItem(response.addressModel);
            }
        }
    }

    @Override
    public void onAvailableInventorySuccess(AvailableInventoryResponse response) {
        mLoader.stopLoader();
        if (response.statusCode == HttpURLConnection.HTTP_OK) {
            availableList = response.inventoryModelList;
        }
    }

    @Override
    public void onAvailableInventoryError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        mLoader.stopLoader();
    }

    @Override
    public void onBrainTreeClientTokenSuccess(String tokenResponse) {
        if (tokenResponse != null) {
            clientToken = tokenResponse;
            mLoader.stopLoader();
            Intent intent = new Intent(this, CheckOutActivity.class);
            intent.putExtra(Constants.IntentKey.PAYMENT_RESPONSE, clientToken);
            intent.putExtra(Constants.IntentKey.PAYMENT_METHOD, paymentMethod);
            startActivity(intent);
            if (ProductDetailsActivity.productDetailsActivity != null)
                ProductDetailsActivity.productDetailsActivity.finish();
           // finish();
        }
        mLoader.stopLoader();
    }


    @Override
    public void onBrainTreeClientTokenError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        mLoader.stopLoader();
    }

    @Override
    public void onItemClick(View view, AddressModel item, int i) {
        if (item != null) {
            clickedRadio = 1;
            presenter.saveAddressData(item, this);
        }
    }
}
