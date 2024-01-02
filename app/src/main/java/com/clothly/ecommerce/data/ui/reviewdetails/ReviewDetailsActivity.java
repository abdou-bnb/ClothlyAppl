package com.clothly.ecommerce.data.ui.reviewdetails;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.helper.models.FeedBackModel;
import com.clothly.ecommerce.data.data.helper.models.ReviewImage;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.databinding.ActivityReviewDetailsBinding;
import com.clothly.ecommerce.data.ui.productdetails.ProductDetailsActivity;
import com.clothly.ecommerce.data.ui.userfeedback.FeedbackActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

public class ReviewDetailsActivity extends BaseActivity<ReviewDetailsMvpView, ReviewDetailsPresenter> implements ReviewDetailsMvpView {
    private RecyclerView recyclerViewReview;
    private ReviewDetailsAdapter mAdapter;
    private ActivityReviewDetailsBinding mBinding;
    String itemId;
    private boolean isRegistered;
    int rvSize, isOrdered;
    private Loader mLoader;
    public static boolean isAlreadyReviewed;


    /**
     * Run Activity
     *
     * @param context mActivity
     * @param itemId  itemId
     */
    public static void runActivity(Context context, String itemId, int ordered) {
        Intent intent = new Intent(context, ReviewDetailsActivity.class);
        intent.putExtra(Constants.IntentKey.ITEM_ID, itemId);
        intent.putExtra(Constants.IntentKey.IS_ORDERED, ordered);
        runCurrentActivity(context, intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_review_details;
    }

    @Override
    protected void startUI() {
        mBinding = (ActivityReviewDetailsBinding) getViewDataBinding();
        initToolbar();

        initRecyclerView();
        isAlreadyReviewed=false;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemId = bundle.getString(Constants.IntentKey.ITEM_ID);
            isOrdered = bundle.getInt(Constants.IntentKey.IS_ORDERED);
        }

        setClickListener(mBinding.fabAddReview);
        mLoader = new Loader(this);
        isRegistered = CustomSharedPrefs.getUserStatus(this);
        getIntentDataAndHitToServer();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ProductDetailsActivity.isFromReview)
            getIntentDataAndHitToServer();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_add_review:
                if (isOrdered == 1) {
                    if (!isAlreadyReviewed) {
                        FeedbackActivity.runActivity(this, itemId);
                    } else {
                        Toast.makeText(this, getString(R.string.review_given), Toast.LENGTH_LONG).show();
                    }
                }else {
                   // Toast.makeText(this, getString(R.string.order_first), Toast.LENGTH_LONG).show();
                    new AlertDialog.Builder(this)
                            .setMessage(getString(R.string.need_order))
                            .setIcon(R.drawable.ic_logout)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    /*Intent intentLogin = new Intent(ReviewDetailsActivity.this, ProductDetailsActivity.class);
                                    intentLogin.putExtra(Constants.SharedPrefCredential.PRODUCT_DETAIL_INTENT, itemId);
                                    startActivity(intentLogin);*/
                                    finish();
                                }
                            })
                            .setNegativeButton(android.R.string.no, null).show();
                }

                break;
        }
    }

    /**
     * Get intent data of item id
     */
    private void getIntentDataAndHitToServer() {
            mLoader.show();
            presenter.showReviewFromServer(this, itemId);
    }


    private void initToolbar() {
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toobarTitle.setText(getString(R.string.title_review_details));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void initRecyclerView() {
        recyclerViewReview = findViewById(R.id.recycler_view_review_details);
        mAdapter = new ReviewDetailsAdapter(this);
        recyclerViewReview.setAdapter(mAdapter);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void stopUI() {
    }

    @Override
    protected ReviewDetailsPresenter initPresenter() {
        return new ReviewDetailsPresenter();
    }


    @Override
    public void onGettingShowReviewSuccess(FeedBackModel feedBackModel) {
        if(feedBackModel != null) {
            mAdapter.clearList();
            mAdapter.addItem(feedBackModel.getReviewImageList());
            rvSize = mAdapter.getItemCount();
            if (rvSize <= 0) {
                mBinding.layoutNoData.setVisibility(View.VISIBLE);
            } else {
                mBinding.layoutNoData.setVisibility(View.GONE);
            }
            setData(feedBackModel);
        }
        mLoader.stopLoader();
    }

    /**
     * Set data in the activity xml
     *
     * @param feedBackModel feedBackModel
     */
    private void setData(FeedBackModel feedBackModel) {
        if (feedBackModel != null) {
            mBinding.textViewTotalRating.setText(feedBackModel.getTotalRatingCount() + " Ratings");
            mBinding.textViewReviewBg.setText(String.valueOf(feedBackModel.getAvgRating()));
            mBinding.ratingBarAvgRating.setRating(feedBackModel.getAvgRating());

            setDataInFiveRatingTextView(feedBackModel);
        }
    }

    /**
     * Set data in five rating text view
     *
     * @param feedBackModel feedBackModel
     */
    private void setDataInFiveRatingTextView(FeedBackModel feedBackModel) {
        List<Integer> ratingCount = new ArrayList<>();
        for (ReviewImage reviewImage : feedBackModel.getReviewImageList()) {
            Integer rating = Integer.parseInt(reviewImage.getRating());
            ratingCount.add(rating);
        }
        TreeSet<Integer> treeSet = new TreeSet<Integer>(ratingCount);
        for (Integer rating : treeSet) {
            switch (rating) {
                case 1:
                    mBinding.textViewRatingCount5.setText(String.valueOf(Collections.frequency(ratingCount, rating)));
                    break;

                case 2:
                    mBinding.textViewRatingCount4.setText(String.valueOf(Collections.frequency(ratingCount, rating)));
                    break;

                case 3:
                    mBinding.textViewRatingCount3.setText(String.valueOf(Collections.frequency(ratingCount, rating)));
                    break;

                case 4:
                    mBinding.textViewRatingCount2.setText(String.valueOf(Collections.frequency(ratingCount, rating)));
                    break;

                case 5:
                    mBinding.textViewRatingCount1.setText(String.valueOf(Collections.frequency(ratingCount, rating)));
                    break;

            }
        }
    }


    @Override
    public void onGettingShowReviewError(String error) {
        mBinding.layoutNoData.setVisibility(View.VISIBLE);
        mLoader.stopLoader();
    }
}
