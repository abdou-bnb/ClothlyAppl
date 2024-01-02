package com.clothly.ecommerce.data.ui.category;

import android.os.CountDownTimer;
import android.support.v7.widget.LinearLayoutManager;
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
import com.clothly.ecommerce.data.data.helper.response.AllCategoryResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.Loader;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class CategoryActivity extends CustomMenuBaseActivity<CategoryMvpView, CategoryPresenter> implements CategoryMvpView {

    TextView toobarTitle;
    Toolbar toolbar;
    ImageView toolbarLogo;
    RecyclerView recyclerViewCategory;
    private Loader mLoader;
    private boolean hasMore;
    private ProgressBar progressBar;
    private AllCategoryResponse mCategoryResponse;
    private int pageNumber = 1;
    private CategoryRecylerViewGridAdapter mAdapter;
    private boolean isFirstTime;
    private LinearLayout linearLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_category;
    }

    @Override
    protected void startUI() {
        initToolbar();
        initViews();

        mLoader = new Loader(this);
        mLoader.show();
        mAdapter = new CategoryRecylerViewGridAdapter(new ArrayList<>(), this);
        settingRecylerView();
        presenter.getCategories(this);
        //loadMore();
    }

    private void initViews() {
        recyclerViewCategory = findViewById(R.id.rv_product_category_grid);
        progressBar = findViewById(R.id.progress_bar);
        linearLayout = findViewById(R.id.layout_no_data);

    }

    @Override
    protected void stopUI() {

    }

    private void loadMore() {
        recyclerViewCategory.getViewTreeObserver().addOnScrollChangedListener(() ->
        {
            if (recyclerViewCategory.getChildAt(0).getBottom()
                    == (recyclerViewCategory.getHeight() + recyclerViewCategory.getScrollY())) {
                if (hasMore) {
                    if (mCategoryResponse != null && mCategoryResponse.dataList != null && mCategoryResponse.statusCode == HttpURLConnection.HTTP_OK) {
                        pageNumber = pageNumber + 1;
                        callCounter();
                        presenter.getCategories(CategoryActivity.this);
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

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

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toobarTitle = findViewById(R.id.toolbar_title);
        toolbarLogo = findViewById(R.id.toolbar_logo);
        toolbar.setTitle("");
        toobarTitle.setText(this.getString(R.string.all_category));
        toobarTitle.setVisibility(View.VISIBLE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void settingRecylerView() {
        recyclerViewCategory.setHasFixedSize(false);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategory.setNestedScrollingEnabled(false);
        recyclerViewCategory.setAdapter(mAdapter);
        hasMore = true;
    }

    @Override
    protected CategoryPresenter initPresenter() {
        return new CategoryPresenter();
    }


    @Override
    public void onCategoryListSuccess(AllCategoryResponse categoryResponse) {
        mCategoryResponse = categoryResponse;
        if (mCategoryResponse.dataList != null && mCategoryResponse.statusCode == HttpURLConnection.HTTP_OK) {
            mAdapter.addItem(mCategoryResponse.dataList);
            isFirstTime = true;
            linearLayout.setVisibility(View.GONE);
        } else {
            hasMore = false;
            if (!isFirstTime) {
                linearLayout.setVisibility(View.VISIBLE);
            }
        }
        progressBar.setVisibility(View.GONE);
        mLoader.stopLoader();
    }

    @Override
    public void onCategoryListError(String message) {
        mLoader.stopLoader();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
