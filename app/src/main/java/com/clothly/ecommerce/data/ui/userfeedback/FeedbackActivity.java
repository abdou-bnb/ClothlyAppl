package com.clothly.ecommerce.data.ui.userfeedback;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BaseActivity;
import com.clothly.ecommerce.data.data.helper.base.ItemClickListener;
import com.clothly.ecommerce.data.data.helper.base.ItemLongClickListener;
import com.clothly.ecommerce.data.data.helper.models.ReviewImage;
import com.clothly.ecommerce.data.data.helper.response.FeedBackResponse;
import com.clothly.ecommerce.data.data.helper.response.UserRegistrationResponse;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.Loader;
import com.clothly.ecommerce.data.data.util.PermissionUtil;
import com.clothly.ecommerce.data.data.util.SharedPref;
import com.clothly.ecommerce.data.data.util.UIHelper;
import com.clothly.ecommerce.data.databinding.ActivityFeedbackBinding;
import com.clothly.ecommerce.data.ui.productdetails.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends BaseActivity<FeedbackMvpView, FeedbackPresenter> implements FeedbackMvpView, ItemClickListener<ReviewImage>, ItemLongClickListener<ReviewImage> {

    private ActivityFeedbackBinding mBinding;
    private FeedbackAdapter mAdapter;
    private RecyclerView recyclerView;
    private List<ReviewImage> reviewImgList;
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    public static boolean isAddNeed;
    private List<String> mImagePathList;
    private String mItemId;
    private Loader mLoader;

    /**
     * Run Activity
     *
     * @param context mActivity
     * @param itemId  itemId
     */
    public static void runActivity(Context context, String itemId) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        intent.putExtra(Constants.IntentKey.ITEM_ID, itemId);
        runCurrentActivity(context, intent);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void startUI() {
        mBinding = (ActivityFeedbackBinding) getViewDataBinding();
        mLoader = new Loader(this);
        reviewImgList = new ArrayList<>();
        initAdapter();
        getIntentData();
    }

    /**
     * Get intent data of item id
     */
    private void getIntentData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mItemId = bundle.getString(Constants.IntentKey.ITEM_ID);
        }

    }

    /**
     * initializing adapter
     */
    private void initAdapter() {
        initToolbar();
        recyclerView = findViewById(R.id.recycler_view_image);
        ReviewImage reviewImage = new ReviewImage();
        reviewImage.imageUri = null;
        reviewImage.lastIndex = 10;
        reviewImgList.add(reviewImage);

        mAdapter = new FeedbackAdapter(reviewImgList, this);
        mAdapter.setClickListener(this);
        mAdapter.setImageItemLongClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mAdapter);

        setClickListener(mBinding.buttonSubmit);
        showUserInformation();
        mImagePathList = new ArrayList<>();
        changeTextForRating(mBinding.ratingBarAvgRating, mBinding.textViewDate);
    }


    /**
     * Rating count and change text
     *
     * @param textView change text
     */
    private void changeTextForRating(RatingBar ratingBar, TextView textView) {
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                if (rating <= 1.0) {
                    textView.setText(getResources().getString(R.string.rating_dialog_hated_it));
                } else if (rating <= 2.0) {
                    textView.setText(getResources().getString(R.string.rating_dialog_disliked_it));
                } else if (rating <= 3.0) {
                    textView.setText(getResources().getString(R.string.rating_dialog_it_okay));
                } else if (rating <= 4.0) {
                    textView.setText(getResources().getString(R.string.rating_dialog_liked_it));
                } else {
                    textView.setText(getResources().getString(R.string.rating_dialog_love_it));
                }
            }

        });


    }

    /**
     * Show user picture
     */
    private void showUserInformation() {
        UIHelper.setThumbImageUriInView(mBinding.imageViewProfile, SharedPref.getSharedPref(this).read(Constants.Preferences.USER_PROFILE_IMAGE));
        UserRegistrationResponse loggedInUser = CustomSharedPrefs.getLoggedInUser(this);
        mBinding.textViewName.setText(loggedInUser.userRegistrationInfo.username);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_submit:
                getDataToSubmitReview();
                break;
        }
    }

    /**
     * Get Data to submit review
     */
    private void getDataToSubmitReview() {
        if (mBinding.ratingBarAvgRating.getRating() > Constants.DefaultValue.RATING_ZERO) {
            String comment = mBinding.editTextComment.getText().toString().trim();
            float rating = mBinding.ratingBarAvgRating.getRating();
            mLoader.show();
            presenter.getReviewData(String.valueOf(rating), mImagePathList, "" + comment, mItemId, this);
        } else {
            Toast.makeText(this, getResources().getString(R.string.toast_please_add_rating), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void stopUI() {

    }

    @Override
    protected FeedbackPresenter initPresenter() {
        return new FeedbackPresenter();
    }

    @Override
    public void onItemClick(View view, ReviewImage item, int i) {
        if (PermissionUtil.on(this).request(PermissionUtil.REQUEST_CODE_PERMISSION_CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)) {
            presenter.selectImage(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_CODE_PERMISSION_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    presenter.selectImage(this);
                } else {
                    Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                presenter.onSelectFromGalleryResult(data, this);
            else if (requestCode == REQUEST_CAMERA)
                presenter.onCaptureImageResult(data, this);
        }
    }

    @Override
    public void onGettingBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            ReviewImage reviewImage = new ReviewImage();
            reviewImage.imageUri = bitmap;
            reviewImage.lastIndex = 1;
            mAdapter.addItemFirst(reviewImage);
        }
    }

    @Override
    public void onGettingImagePath(String imagePath) {
        if (mImagePathList != null) {
            mImagePathList.add(imagePath);
        }
    }

    @Override
    public void onGettingReviewSuccess(FeedBackResponse feedBackResponse) {
        mLoader.stopLoader();
        Toast.makeText(this, feedBackResponse.getMessage(), Toast.LENGTH_SHORT).show();
        ProductDetailsActivity.isFromReview = true;
        finish();
    }

    @Override
    public void onGettingReviewError(String error) {
        mLoader.stopLoader();
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemLongClick(View v, ReviewImage item) {

    }

    @Override
    public void onItemLongClick(View v, ReviewImage item, int position, int listSize) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder
                .setMessage("Do you want to remove?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialog, id) -> {
                    if (listSize == 5 && !isAddNeed) {
                        ReviewImage reviewImage = new ReviewImage();
                        reviewImage.imageUri = null;
                        reviewImage.lastIndex = 10;
                        mAdapter.addItemLast(reviewImage);
                        isAddNeed = true;
                    }
                    if (mImagePathList != null) {
                        mImagePathList.remove(position);
                    }
                    mAdapter.removeItem(position);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void initToolbar() {
        TextView toobarTitle = findViewById(R.id.toolbar_title);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Customer Feedback");
        // toobarTitle.setText(this.getString(R.string.title_product_detail));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
