package com.clothly.ecommerce.data.ui.ordercomplete;

import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.helper.response.OrderListResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.databinding.ActivityOrderCompleteBinding;


import java.net.HttpURLConnection;
import java.util.ArrayList;

public class OrderCompleteActivity extends BaseActivity<OrderCompleteMvpView, OrderCompletePresenter> implements OrderCompleteMvpView {

    private ActivityOrderCompleteBinding mBinding;
    private OrderMainAdapter mAdapter;
    private Loader mLoader;
    private OrderListResponse orderListResponse;
    private int pageNumber = 1;
    private boolean hasMore;
    private String userID;
    private boolean isFirstLoad;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_complete;
    }

    @Override
    protected void startUI() {
        mBinding = (ActivityOrderCompleteBinding) getViewDataBinding();
        initToolbar();
        mLoader = new Loader(this);
        userID = CustomSharedPrefs.getLoggedInUserId(this);

        initRecycler();
        mLoader.show();
        presenter.getOrderedList(this, userID, "" + pageNumber);
        loadMore();
    }

    /**
     * this api is used for pagination purpose
     */
    private void loadMore() {
        mBinding.recyclerViewMainOrder.getViewTreeObserver().
                addOnScrollChangedListener(() ->
                {
                    if (mBinding.recyclerViewMainOrder.getChildAt(0).getBottom()
                            == (mBinding.recyclerViewMainOrder.getHeight() + mBinding.recyclerViewMainOrder.getScrollY())) {
                        if (hasMore) {
                            if (orderListResponse != null && orderListResponse.orderModels != null && orderListResponse.statusCode == HttpURLConnection.HTTP_OK) {
                                pageNumber = pageNumber + 1;
                                callCounter();
                                presenter.getOrderedList(this, userID, "" + pageNumber);
                            }
                        } else {
                            mBinding.progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * start a counter for stooping loader
     */
    private void callCounter() {
        new CountDownTimer(Constants.DefaultValue.DELAY_INTERVAL, 1) {
            public void onTick(long millisUntilFinished) {
                mBinding.progressBar.setVisibility(View.VISIBLE);
            }

            public void onFinish() {
                mBinding.progressBar.setVisibility(View.GONE);
            }
        }.start();
    }

    /**
     * initializing recycler view
     */
    private void initRecycler() {
        mAdapter = new OrderMainAdapter(new ArrayList<>(), this);
        mBinding.recyclerViewMainOrder.setAdapter(mAdapter);
        mBinding.recyclerViewMainOrder.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));
        hasMore = true;
    }

    /**
     * toolbar initialization
     */
    private void initToolbar() {
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(getString(R.string.order_complete));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected OrderCompletePresenter initPresenter() {
        return new OrderCompletePresenter();
    }

    @Override
    public void onGettingOrderInfoSuccess(OrderListResponse response) {
        if (response != null) {
            orderListResponse = response;
            if (response.statusCode == HttpURLConnection.HTTP_OK) {
                isFirstLoad = true;
                mBinding.textViewEmpty.setVisibility(View.INVISIBLE);
               // mBinding.textViewEmpty.setVisibility(View.INVISIBLE);
                mBinding.layoutNoData.setVisibility(View.GONE);
                mAdapter.addItem(orderListResponse.orderModels);
            } else {
                hasMore = false;
                if(!isFirstLoad){
                   // mBinding.textViewEmpty.setVisibility(View.VISIBLE);
                    mBinding.layoutNoData.setVisibility(View.VISIBLE);
                }
            }
            mBinding.progressBar.setVisibility(View.GONE);
            mLoader.stopLoader();
        }

    }

    @Override
    public void onGettingOrderInfoError(String errorMessage) {
        mLoader.stopLoader();
        mBinding.layoutNoData.setVisibility(View.VISIBLE);
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
