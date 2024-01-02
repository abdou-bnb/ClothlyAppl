package com.clothly.ecommerce.data.ui.myfavourite;

import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.CustomMenuBaseActivity;
import com.clothly.ecommerce.data.data.helper.response.ProductGridResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.UtilityClass;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class UserFavActivity extends CustomMenuBaseActivity<UserFavMvpView, UserFavPresenter> implements UserFavMvpView {

    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;
    private int pageNumber = 1;
    private ProductGridResponse mProductResponse;
    private boolean hasMore;
    private ProgressBar progressBar;
    private LinearLayout linearLayout,linearLayoutNoDataFound;

    RecyclerView recyclerViewProduct;
    private Loader mLoader;
    private FavProductRecylerViewAdapter mAdapter;
    private boolean isFirstTime;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_fav;
    }

    @Override
    protected void startUI() {
        initToolbar();
        mLoader = new Loader(this);
        progressBar = findViewById(R.id.progress_bar);
        linearLayout = findViewById(R.id.layout_linear);
        mAdapter = new FavProductRecylerViewAdapter(new ArrayList<>(), this);
        recyclerViewProduct = findViewById(R.id.rv_user_fav_product_grid);
        linearLayoutNoDataFound = findViewById(R.id.layout_no_data);
        settingRecylerView();
        mLoader.show();
        presenter.getAllFavouriteResponse(this, "" + pageNumber, CustomSharedPrefs.getLoggedInUserId(this));
        loadMore();
    }

    @Override
    protected void stopUI() {
    }

    /**
     * setting pagination
     */
    private void loadMore() {
        linearLayout.getViewTreeObserver().
                addOnScrollChangedListener(() ->
                {
                    if (linearLayout.getChildAt(0).getBottom()
                            == (linearLayout.getHeight() + linearLayout.getScrollY())) {
                        if (hasMore) {
                            if (mProductResponse != null && mProductResponse.dataModel != null && mProductResponse.statusCode == HttpURLConnection.HTTP_OK) {
                                pageNumber = pageNumber + 1;
                                callCounter();
                                presenter.getAllFavouriteResponse(this, "" + pageNumber, CustomSharedPrefs.getLoggedInUserId(this));
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * this api is used for hiding and showing loader
     */
    private void callCounter() {
        new CountDownTimer(Constants.DefaultValue.DELAY_INTERVAL, 1) {
            public void onTick(long millisUntilFinished) {
                progressBar.setVisibility(View.VISIBLE);
            }

            public void onFinish() {
                progressBar.setVisibility(View.GONE);
            }
        }.start();
    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setVisibility(View.VISIBLE);
        toobarTitle.setText(this.getString(R.string.my_favorites));
        String toolbarTitle = getIntent().getStringExtra(UtilityClass.TOOLBAR_TITLE);
        if (toolbarTitle != null) {
            toobarTitle.setText(toolbarTitle);
            toolbarLogo.setVisibility(View.GONE);
            toobarTitle.setVisibility(View.VISIBLE);

        }
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    protected UserFavPresenter initPresenter() {
        return new UserFavPresenter();
    }


    /**
     * setting recycler view with adapter
     */
    private void settingRecylerView() {
        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(mAdapter);
    }


    @Override
    public void onGettingFavouriteSuccess(ProductGridResponse response) {
        if (response!= null) {
            mProductResponse = response;
            if (mProductResponse.dataModel != null && mProductResponse.statusCode == HttpURLConnection.HTTP_OK) {
                mAdapter.addItem(mProductResponse.dataModel);
                isFirstTime = true;
                linearLayoutNoDataFound.setVisibility(View.GONE);
            } else {
               if(!isFirstTime){
                   linearLayoutNoDataFound.setVisibility(View.VISIBLE);
               }
                hasMore = false;
            }
            progressBar.setVisibility(View.GONE);
            mLoader.stopLoader();
        }else {
            linearLayoutNoDataFound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onGettingFavouriteError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        mLoader.stopLoader();
        linearLayoutNoDataFound.setVisibility(View.VISIBLE);

    }
}
