package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecentReviewModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("item_id")
    @Expose
    public int itemId;

    @SerializedName("user_id")
    @Expose
    public String userId;

    @SerializedName("admin_id")
    @Expose
    public int adminId;

    @SerializedName("rating")
    @Expose
    public float rating;

    @SerializedName("review")
    @Expose
    public String reviewMessage;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("image_name")
    @Expose
    public String imageUri;

    @SerializedName("time")
    @Expose
    public String time;

}
