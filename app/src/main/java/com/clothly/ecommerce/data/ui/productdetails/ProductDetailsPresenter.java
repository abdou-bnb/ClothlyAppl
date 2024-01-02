package com.clothly.ecommerce.data.ui.productdetails;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.database.DatabaseUtil;
import com.clothly.ecommerce.data.data.helper.models.AttributeValueModel;
import com.clothly.ecommerce.data.data.helper.models.AttributeWithView;
import com.clothly.ecommerce.data.data.helper.models.CustomProductInventory;
import com.clothly.ecommerce.data.data.helper.models.InventoryModel;
import com.clothly.ecommerce.data.data.helper.response.AddFavouriteResponse;
import com.clothly.ecommerce.data.data.helper.response.ProductDetailsResponse;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.NetworkHelper;
import com.clothly.ecommerce.data.data.util.SharedPref;
import com.clothly.ecommerce.data.ui.addcart.CartActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsPresenter extends BasePresenter<ProductDetailsMvpView> {
    private String userId = "";
    private boolean colorFlag, sizeFlag, isAvailableProduct, hasInventory;
    private int counter_1 = 0, counter_2 = 0, productCounter = 0, quality;
    private List<CustomProductInventory> prevCartList;
    private CustomProductInventory currentProductInventory;
    private List<String> combinationArray;
    private boolean isFirstCombination;
    StringBuilder inventoryKey;
    String color, size;
    List<String> attributeTitle;
    String gsonTitle;

    /**
     * this api is used to get product details response
     *
     * @param productId
     * @param context
     */
    public void getProductDetailsResponse(final String productId, final Context context) {

        userId = CustomSharedPrefs.getLoggedInUserId(context);
        if (NetworkHelper.hasNetworkAccess(context)) {
            RetrofitClient.getApiService().getProductDetailsResponse(Constants.ServerUrl.API_TOKEN, productId, userId).enqueue(new Callback<ProductDetailsResponse>() {
                @Override
                public void onResponse(@NonNull Call<ProductDetailsResponse> call, @NonNull Response<ProductDetailsResponse> response) {
                    if (response.body() != null) {
                        getMvpView().onProductDetailsSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProductDetailsResponse> call, @NonNull Throwable t) {
                    getMvpView().onProductDetailsError(t.getMessage());
                }
            });
        } else {
            Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * this api is used to make product favourite.
     *
     * @param context
     * @param itemId
     * @param userId
     */
    public void getAddFavouriteResponse(Context context, String itemId, String userId) {
        if (NetworkHelper.hasNetworkAccess(context)) {
            RetrofitClient.getApiService().addFavourite(Constants.ServerUrl.API_TOKEN, itemId, userId).enqueue(new Callback<AddFavouriteResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddFavouriteResponse> call, @NonNull Response<AddFavouriteResponse> response) {
                    if (response.body() != null) {
                        getMvpView().onFavSuccess(response.body());
                    }

                }

                @Override
                public void onFailure(@NonNull Call<AddFavouriteResponse> call, @NonNull Throwable t) {
                    getMvpView().onFavError(t.getMessage());
                }
            });
        } else {
            Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * this api is used to remove favourite.
     *
     * @param context
     * @param itemId
     * @param userId
     */
    public void getRemoveFavouriteResponse(Context context, String itemId, String userId) {
        if (NetworkHelper.hasNetworkAccess(context)) {
            RetrofitClient.getApiService().removeFavourite(Constants.ServerUrl.API_TOKEN, itemId, userId).enqueue(new Callback<AddFavouriteResponse>() {
                @Override
                public void onResponse(@NonNull Call<AddFavouriteResponse> call, @NonNull Response<AddFavouriteResponse> response) {
                    if (response.body() != null) {
                        getMvpView().onRemoveFavSuccess(response.body());
                    }

                }

                @Override
                public void onFailure(@NonNull Call<AddFavouriteResponse> call, @NonNull Throwable t) {
                    getMvpView().onFavError(t.getMessage());
                }
            });
        } else {
            Toast.makeText(context, "Could not connect to internet.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addCart(int track, Activity mActivity,
                        List<AttributeValueModel> selectedAttribute,
                        List<AttributeWithView> mainIdList,
                        List<InventoryModel> inventoryModels,
                        CustomProductInventory cartInventory, boolean isListEmpty) {

        combinationArray = new ArrayList<>();
        attributeTitle = new ArrayList<>();
        isFirstCombination = false;
        if (!selectedAttribute.isEmpty() && !mainIdList.isEmpty()) {
            for (int i = 0; i < mainIdList.size(); i++) {
                for (int j = 0; j < selectedAttribute.size(); j++) {
                    if (selectedAttribute.get(j).attribute == mainIdList.get(i).id) {
                        if (isFirstCombination) {
                            combinationArray.add("," + mainIdList.get(i).id + "-" + selectedAttribute.get(j).id);

                        } else {
                            combinationArray.add(mainIdList.get(i).id + "-" + selectedAttribute.get(j).id);
                            isFirstCombination = true;
                        }
                       /* if (selectedAttribute.get(j).attribute == 28) {
                            color = selectedAttribute.get(j).title;
                        } else if (selectedAttribute.get(j).attribute == 29) {
                            size = selectedAttribute.get(j).title;
                        }*/
                        attributeTitle.add(selectedAttribute.get(j).rootTitle + ": " + selectedAttribute.get(j).title + " ");
                    }
                }
            }

            gsonTitle = new Gson().toJson(attributeTitle);
            inventoryKey = new StringBuilder();
            for (int i = 0; i < combinationArray.size(); i++) {
                inventoryKey.append(combinationArray.get(i));
            }
        }
        if (inventoryModels.size() > 0) {
            if (!isListEmpty) {
                for (InventoryModel inventory : inventoryModels) {
                    if (inventory.attribute.contentEquals(inventoryKey)) {
                        if (inventory.quantity > 0 && counter_1 <= inventory.quantity) {
                            ++counter_1;
                            isAvailableProduct = true;
                            cartInventory.available_qty = inventory.quantity;
                            cartInventory.product_id = inventory.productId;
                            cartInventory.inventory_id = inventory.id;

                            /* cartInventory.color_name = color;
                            cartInventory.size_name = size;*/
                            cartInventory.attributeTitle = gsonTitle;
                        } else {
                            isAvailableProduct = false;
                        }
                    }
                }
            } else {
                for (InventoryModel inventory : inventoryModels) {
                    if (inventory.quantity > 0) {
                        isAvailableProduct = true;
                        cartInventory.available_qty = inventory.quantity;
                        cartInventory.attributeTitle = gsonTitle;
                    /*    cartInventory.color_name = "null";
                        cartInventory.size_name = "null";*/
                        cartInventory.product_id = inventory.productId;
                        cartInventory.inventory_id = inventory.id;
                    } else isAvailableProduct = false;
                }
            }
        } else {
            Toast.makeText(mActivity, "There is no inventory!", Toast.LENGTH_SHORT).show();
        }

        currentProductInventory = cartInventory;
        checkPreviousInventory(currentProductInventory, mActivity, track);


    }

    /**
     * if user has previous cart product. need to check total inventory
     *
     * @param inventoryModels
     * @param mActivity
     * @param track
     */
    private void checkPreviousInventory(CustomProductInventory inventoryModels, Activity mActivity, int track) {
        productCounter = 0;
        AsyncTask.execute(() -> {
            prevCartList = DatabaseUtil.on().getAllCodes();
            for (CustomProductInventory customInventory : prevCartList) {
                if (inventoryModels.inventory_id == customInventory.inventory_id) {
                    productCounter = productCounter + customInventory.currentQuantity;
                }
            }
            //productCounter++;
            if (isAvailableProduct) {
                if (productCounter < inventoryModels.available_qty) {
                    insertToCart(currentProductInventory, mActivity, track);
                } else {
                    mActivity.runOnUiThread(() -> Toast.makeText(mActivity, "Inventory exceed. You can't add more!!", Toast.LENGTH_SHORT).show());
                }
            } else {
                mActivity.runOnUiThread(() -> Toast.makeText(mActivity, "Product is not available", Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * insert item to cart
     *
     * @param currentProductInventory
     * @param mActivity
     * @param track
     */
    private void insertToCart(CustomProductInventory currentProductInventory, Activity mActivity, int track) {
        if (isAvailableProduct) {
            AsyncTask.execute(() -> {
                DatabaseUtil.on().insertItem(currentProductInventory);
                if (track == 1) {
                    Intent intent = new Intent(mActivity, CartActivity.class);
                    mActivity.startActivity(intent);
                } else {
                    mActivity.runOnUiThread(() -> {
                        Toast.makeText(mActivity, "This Product added into cart!", Toast.LENGTH_SHORT).show();
                        ((ProductDetailsActivity) mActivity).updateMenu();
                    });
                }
            });
        } else {
            Toast.makeText(mActivity, "Product is not available", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * getting data from shared pref
     */
    public void getDataFromSharePref(Activity mActivity, ConstraintLayout layoutAdView) {

        String bannerStatus = SharedPref.getSharedPref(mActivity).read(Constants.Preferences.BANNER_STATUS);
        String bannerUnitID = SharedPref.getSharedPref(mActivity).read(Constants.Preferences.BANNER_UNIT_ID);
        String bannerAppID = SharedPref.getSharedPref(mActivity).read(Constants.Preferences.BANNER_APP_ID);
        if (bannerStatus.equals(Constants.Preferences.STATUS_ON)) {
            if (bannerUnitID != null && bannerAppID != null) {
                try {
                    ApplicationInfo applicationInfo = mActivity.getPackageManager().getApplicationInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA);
                    applicationInfo.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", bannerAppID);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                MobileAds.initialize(mActivity,
                        bannerAppID);
                AdView adView = new AdView(mActivity);
                adView.setAdSize(AdSize.SMART_BANNER);
                adView.setAdUnitId(bannerUnitID);
                layoutAdView.addView(adView);

                adPlay(adView, layoutAdView);
            }
        }
    }

    /**
     * play banner ad based on ad listener
     */
    private void adPlay(AdView adView, ConstraintLayout layoutAdview) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                layoutAdview.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });
    }
}
