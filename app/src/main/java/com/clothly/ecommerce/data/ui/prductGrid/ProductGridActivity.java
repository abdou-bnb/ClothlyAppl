package com.clothly.ecommerce.data.ui.prductGrid;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.CustomMenuBaseActivity;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.models.ProductModel;
import com.clothly.ecommerce.data.data.helper.response.AddFavouriteResponse;
import com.clothly.ecommerce.data.data.helper.response.ProductGridResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.UIHelper;
import com.clothly.ecommerce.data.data.util.UtilityClass;

import java.net.HttpURLConnection;
import java.util.ArrayList;


public class ProductGridActivity extends CustomMenuBaseActivity<ProductGridMvpView, ProductGridPresenter> implements ProductGridMvpView, ItemClickListener<ProductModel> {

    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;

    private Loader mLoader;
    RecyclerView recyclerViewProduct;
    private String userID = "";
    private String categoryId = "";
    private int pageNumber = 1;
    private ProductRecylerViewAdapter mAdapter;
    private ProductGridResponse mProductResponse;
    private ProgressBar progressBar;
    private boolean hasMore, isFirstTime;
    private LinearLayout linearLayout, linearLayout2;
    private ProductModel productModel;
    private View views;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_grid;
    }

    @Override
    protected void startUI() {
        recyclerViewProduct = findViewById(R.id.rv_product_grid);
        progressBar = findViewById(R.id.progress_bar);
        linearLayout = findViewById(R.id.layout_total);
        linearLayout2 = findViewById(R.id.layout_no_data);
        initToolbar();
        Intent intent = getIntent();
        if (CustomSharedPrefs.getUserStatus(this)) {
            userID = CustomSharedPrefs.getLoggedInUserId(this);
        }
        if (intent != null) {
            categoryId = "" + intent.getStringExtra(Constants.SharedPrefCredential.INTENT_CATEGORY_ID);
            Log.d("catId",categoryId.toString());
        }

        mAdapter = new ProductRecylerViewAdapter(new ArrayList<>(), this, false);
        mAdapter.setItemClickedListener(this);

        mLoader = new Loader(this);
        mLoader.show();
        presenter.getProductList(categoryId, this, userID, "" + pageNumber);
        settingRecylerView();
        loadMore();


    }

    @Override
    protected void onResume() {
        super.onResume();


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
        toobarTitle.setText(this.getString(R.string.category));
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
                                presenter.getProductList(categoryId, this, userID, "" + pageNumber);
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    /**
     * hiding and showing loader based on counter
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


    @Override
    protected ProductGridPresenter initPresenter() {
        return new ProductGridPresenter();
    }

    /**
     * setting recycler with adapter
     */
    private void settingRecylerView() {
        recyclerViewProduct.setHasFixedSize(false);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewProduct.setNestedScrollingEnabled(false);
        recyclerViewProduct.setAdapter(mAdapter);
        hasMore = true;
    }

    @Override
    public void onItemClick(View view, ProductModel item, int i) {
        if (item != null) {
            if (CustomSharedPrefs.getUserStatus(this)) {
                if (view.getId() == R.id.btn_favourite) {
                    productModel = item;
                    views = view;
                    if (item.isFavourite != 1) {
                        presenter.getAddFavouriteResponse(this, "" + item.id, CustomSharedPrefs.getLoggedInUserId(this));
                    } else {
                        presenter.getRemoveFavouriteResponse(this, "" + item.id, CustomSharedPrefs.getLoggedInUserId(this));
                    }
                }
            }
        } else {
            UIHelper.openSignInPopUp(this);
        }
    }


    @Override
    public void onFavSuccess(AddFavouriteResponse response) {
        if (response != null) {
            if (response.statusCode == HttpURLConnection.HTTP_OK) {
                if (productModel != null) {
                    productModel.isFavourite = 1;
                }
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show();
                ((LikeButton) views).setLiked(true);
            } else {
                ((LikeButton) views).setLiked(false);
            }
        }
    }

    @Override
    public void onFavError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRemoveFavSuccess(AddFavouriteResponse response) {
        if (response != null) {
            if (response.statusCode == HttpURLConnection.HTTP_OK) {
                if (productModel != null) {
                    productModel.isFavourite = 2;
                }
                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show();
                ((LikeButton) views).setLiked(false);
            } else ((LikeButton) views).setLiked(true);
        }
    }

    @Override
    public void onProductListSuccess(ProductGridResponse products) {
        mProductResponse = products;
        if (mProductResponse.dataModel != null && mProductResponse.statusCode == HttpURLConnection.HTTP_OK) {
                mAdapter.addItem(mProductResponse.dataModel);
                linearLayout2.setVisibility(View.GONE);
                isFirstTime = true;

        } else {
            if (!isFirstTime)
                linearLayout2.setVisibility(View.VISIBLE);
            // Toast.makeText(this, mProductResponse.message, Toast.LENGTH_SHORT).show();
            hasMore = false;
        }
        progressBar.setVisibility(View.GONE);
        mLoader.stopLoader();
    }


    @Override
    public void onProductListError(String errorMessage) {
        mLoader.stopLoader();
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
