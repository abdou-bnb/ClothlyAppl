package com.clothly.ecommerce.data.data.helper.models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewImage {

    public Bitmap imageUri;
    public int lastIndex;


    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("item_id")
    @Expose
    private int itemId;

    @SerializedName("user_id")
    @Expose
    private int userId;

    @SerializedName("rating")
    @Expose
    private String rating;

    @SerializedName("review")
    @Expose
    private String review;


    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("user_name")
    @Expose
    private String userName;

    @SerializedName("image_name")
    @Expose
    private String imageName;

    @SerializedName("review_images")
    @Expose
    private List<ShowReviewImage> mShowReviewImageList;

    public List<ShowReviewImage> getShowReviewImageList() {
        return mShowReviewImageList;
    }

    public void setShowReviewImageList(List<ShowReviewImage> showReviewImageList) {
        mShowReviewImageList = showReviewImageList;
    }

    public Bitmap getImageUri() {
        return imageUri;
    }

    public void setImageUri(Bitmap imageUri) {
        this.imageUri = imageUri;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getImageName() {
        return imageName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
