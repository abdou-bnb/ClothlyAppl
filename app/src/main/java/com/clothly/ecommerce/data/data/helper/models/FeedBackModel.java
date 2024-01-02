package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FeedBackModel {

    @SerializedName("avg_rating")
    @Expose
    public float avgRating;

    @SerializedName("rating_count")
    @Expose
    public int totalRatingCount;

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public int getTotalRatingCount() {
        return totalRatingCount;
    }

    public void setTotalRatingCount(int totalRatingCount) {
        this.totalRatingCount = totalRatingCount;
    }

    @SerializedName("reviews")
    @Expose
    List<ReviewImage> mReviewImageList;


    public List<ReviewImage> getReviewImageList() {
        return mReviewImageList;
    }

    public void setReviewImageList(List<ReviewImage> reviewImageList) {
        mReviewImageList = reviewImageList;
    }
}
