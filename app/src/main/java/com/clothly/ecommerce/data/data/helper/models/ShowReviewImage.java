package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowReviewImage {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("item_id")
    @Expose
    private int itemId;

    @SerializedName("review_id")
    @Expose
    private int reviewId;

    @SerializedName("admin_id")
    @Expose
    private int adminId;

    @SerializedName("image_name")
    @Expose
    private String profileImageOfReviewer;

    @SerializedName("resolution")
    @Expose
    private String withHightOfImage;

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

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getProfileImageOfReviewer() {
        return profileImageOfReviewer;
    }

    public void setProfileImageOfReviewer(String profileImageOfReviewer) {
        this.profileImageOfReviewer = profileImageOfReviewer;
    }

    public String getWithHightOfImage() {
        return withHightOfImage;
    }

    public void setWithHightOfImage(String withHightOfImage) {
        this.withHightOfImage = withHightOfImage;
    }
}
