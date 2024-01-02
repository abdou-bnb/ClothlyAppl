package com.clothly.ecommerce.data.ui.addcart;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.CustomMenuBaseActivity;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.database.DatabaseUtil;
import com.clothly.ecommerce.data.data.helper.models.CustomProductInventory;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.SharedPref;
import com.clothly.ecommerce.data.ui.productdetails.ProductDetailsActivity;
import com.clothly.ecommerce.data.ui.shippingaddress.ShippingAddressActivity;
import com.clothly.ecommerce.data.ui.userLogin.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends CustomMenuBaseActivity<CartMvpView, CartPresenter> implements CartMvpView, ItemClickListener<CustomProductInventory> {
    private Toolbar toolbar;
    private List<CustomProductInventory> inventoryList;
    private CartAdapter mAdapter;
    private RecyclerView recyclerView;
    private float totalPrice = 0.0f;
    private TextView textViewPrice;
    private Button buttonCheckOut;
    public static int CLICK_CART_DELETE = 4646;
    public static CartActivity cartActivity;
    private LinearLayout linearLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart;
    }

    @Override
    protected void startUI() {
        mAdapter = new CartAdapter(new ArrayList<>(), this);
        recyclerView = findViewById(R.id.recycler_view_cart);
        textViewPrice = findViewById(R.id.tv_total_price);
        buttonCheckOut = findViewById(R.id.btn_cart_checkout);
        linearLayout = findViewById(R.id.layout_no_data);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        mAdapter.setItemClickListener(this);
        recyclerView.setAdapter(mAdapter);
        cartActivity = this;

        initToolbar();
        AsyncTask.execute(() -> {
            inventoryList = DatabaseUtil.on().getAllCodes();
            if (inventoryList!=null&&inventoryList.size()>0){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.setVisibility(View.GONE);
                    }
                });
                mAdapter.addItem(inventoryList);
            }else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayout.setVisibility(View.VISIBLE);
                    }
                });

            }

            calculatePrice(inventoryList);
        });

        buttonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inventoryList.size() > 0) {
                    checkRegistration();
                } else {
                    //UIHelper.openSignInPopUp(CartActivity.this);
                    Toast.makeText(CartActivity.this, "Please add a productId", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * action upon registration state
     */
    private void checkRegistration() {
        if (!CustomSharedPrefs.getUserStatus(this)) {
            Intent intent = new Intent(CartActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            String totalPriceStr = String.valueOf(totalPrice);
            SharedPref.getSharedPref(this).write(Constants.IntentKey.TOTAL_AMOUNT, totalPrice);
            if (totalPrice > 0) {
                Intent intent = new Intent(CartActivity.this, ShippingAddressActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(CartActivity.this, "Please add a productId", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * to calculate total price based on all product in database
     *
     * @param inventoryList : list of CustomProductInventory
     */
    private void calculatePrice(List<CustomProductInventory> inventoryList) {
        if (DatabaseUtil.on().getItemCount() > 0) {
            for (CustomProductInventory productInventory : inventoryList) {
                totalPrice = totalPrice + (productInventory.price * productInventory.currentQuantity);
            }
            runOnUiThread(() -> {
                textViewPrice.setText(CustomSharedPrefs.getCurrency(this) + "" + totalPrice);
            });
        }
    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.title_cart));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void stopUI() {
        if (cartActivity != null)
            cartActivity = null;
    }

    @Override
    protected CartPresenter initPresenter() {
        return new CartPresenter();
    }

    @Override
    public void onItemClick(View view, CustomProductInventory item, int i) {
        totalPrice = 0.0f;
        calculatePrice(mAdapter.getItems());

        if (i == CLICK_CART_DELETE) {
            updateMenu();
            AsyncTask.execute(() -> {
                if (DatabaseUtil.on().getItemCount() <= 0) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewPrice.setText(CustomSharedPrefs.getCurrency(CartActivity.this) + "" + totalPrice);
                            linearLayout.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });



        }

    }




    @Override
    public void onBackPressed() {
        ProductDetailsActivity.isFromReview = false;
        super.onBackPressed();
    }
}
