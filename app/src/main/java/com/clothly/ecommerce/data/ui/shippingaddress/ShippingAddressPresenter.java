package com.clothly.ecommerce.data.ui.shippingaddress;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.models.AddressModel;
import com.clothly.ecommerce.data.data.helper.response.AvailableInventoryResponse;
import com.clothly.ecommerce.data.data.helper.response.UserAddressResponse;
import com.clothly.ecommerce.data.data.helper.response.UserMultipleAddressResponse;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.NetworkHelper;
import com.clothly.ecommerce.data.data.util.SharedPref;
import com.clothly.ecommerce.data.data.util.UtilityClass;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShippingAddressPresenter extends BasePresenter<ShippingAddressMvpView> {
    /**
     * updating address to server
     * @param context
     * @param address1
     * @param address2
     * @param city
     * @param zip
     * @param state
     * @param country
     */

    public void updateAddress(final Context context
            , final String address1
            , final String address2
            , final String city
            , final String zip
            , final String state
            , final String country) {
        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.USER_ADDRESS;
        RequestQueue queue = Volley.newRequestQueue(context);

        if (NetworkHelper.hasNetworkAccess(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, response -> {
                UserAddressResponse addressResponse;
                Type address = new TypeToken<UserAddressResponse>() {
                }.getType();
                addressResponse = new Gson().fromJson(response, address);
                getMvpView().onGetAvailableAddressSuccess(addressResponse);
             //   saveData(addressResponse, mActivity);
                Toast.makeText(context, addressResponse.message, Toast.LENGTH_LONG).show();
            }, error -> getMvpView().onGetAvailableAddressError(error.getMessage())) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("api_token", Constants.ServerUrl.API_TOKEN);
                    params.put("user_id", CustomSharedPrefs.getLoggedInUserId(context));
                    params.put("line_1", address1);
                    params.put("line_2", address2);
                    params.put("city", city);
                    params.put("zip", zip);
                    params.put("state", state);
                    params.put("country", country);

                    return params;

                }
            };

            queue.getCache().clear();
            queue.add(stringRequest);
        } else {
            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * getting token for braintree payment
     * @param context
     */
    public void getClientTokenFromServer(Context context) {

        String sendBox = SharedPref.getSharedPref(context).read(Constants.Preferences.ENVIRONMENT);
        String merchant = SharedPref.getSharedPref(context).read(Constants.Preferences.MERCHANT_ID);
        String publicID = SharedPref.getSharedPref(context).read(Constants.Preferences.PUBLIC_KEY);
        String privateID = SharedPref.getSharedPref(context).read(Constants.Preferences.PRIVATE_KEY);
        RequestParams params = new RequestParams();
        params.put("environment", sendBox);
        params.put("merchantId", merchant);
        params.put("publicKey", publicID);
        params.put("privateKey", privateID);
        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please Wait......");
        dialog.show();

        String JSON_URL = Constants.ServerUrl.ROOT_URL + Constants.ServerUrl.BRAIN_TREE;
        RequestQueue queue = Volley.newRequestQueue(context);

        if (NetworkHelper.hasNetworkAccess(context)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL, response -> {
                if(response != null && !response.equals("")) {
                    dialog.dismiss();
                    getMvpView().onBrainTreeClientTokenSuccess(response);
                }
            }, error -> {
                dialog.dismiss();
                getMvpView().onBrainTreeClientTokenError(error.getMessage());
            }){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("environment", sendBox);
                    params.put("merchantId", merchant);
                    params.put("publicKey", publicID);
                    params.put("privateKey", privateID);
                    return params;

                }
            };
            queue.getCache().clear();
            queue.add(stringRequest);
        } else {
            Toast.makeText(context, "Please check internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * saving address to shared pref
     * @param addressResponse
     * @param context
     */
    public void saveData(UserAddressResponse addressResponse, Context context) {
        if (addressResponse != null) {
            String address = UtilityClass.objectToString(addressResponse);
            SharedPref.getSharedPref(context).write(Constants.Preferences.USER_ADDRESS, address);
        }
    }

    /**
     * getting available inventory of product
     * @param context
     * @param inventories
     */
    public void getAvailableInventory(Context context, String inventories) {
        if (NetworkHelper.hasNetworkAccess(context)) {
            RetrofitClient.getApiService().getAvailableInventory(Constants.ServerUrl.API_TOKEN, inventories)
                    .enqueue(new Callback<AvailableInventoryResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<AvailableInventoryResponse> call, @NonNull Response<AvailableInventoryResponse> response) {
                            if (response.body() != null) {
                                getMvpView().onAvailableInventorySuccess(response.body());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<AvailableInventoryResponse> call, @NonNull Throwable t) {
                            getMvpView().onAvailableInventoryError(t.getMessage());
                        }
                    });

        }
    }

    /**
     * saving address to shared pref
     *
     * @param addressModel
     * @param context
     */
    public void saveAddressData(AddressModel addressModel, Context context) {
        if (addressModel != null) {
            String address = UtilityClass.objectToString(addressModel);
            SharedPref.getSharedPref(context).write(Constants.Preferences.USER_ADDRESS, address);
        }
    }

    /**
     * this api is used to get all address of user from server
     *
     * @param context
     * @param userId
     */
    public void getAllAddress(Context context, String userId) {
        if (NetworkHelper.hasNetworkAccess(context)) {
            RetrofitClient.getApiService().getAllAddress(Constants.ServerUrl.API_TOKEN, userId).enqueue(new Callback<UserMultipleAddressResponse>() {
                @Override
                public void onResponse(@NonNull Call<UserMultipleAddressResponse> call, @NonNull Response<UserMultipleAddressResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            getMvpView().onGettingAllAddressSuccess(response.body());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UserMultipleAddressResponse> call, @NonNull Throwable t) {
                    getMvpView().onGetAvailableAddressError(t.getMessage());
                }
            });
        } else {
            getMvpView().onGetAvailableAddressError("Please check internet connection!");
        }
    }


}
