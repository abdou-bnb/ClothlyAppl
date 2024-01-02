package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailsImageModel {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("item_id")
    @Expose
    public int itemId;

    @SerializedName("review_id")
    @Expose
    public int reviewId;

    @SerializedName("image_name")
    @Expose
    public String imageUri;

    @SerializedName("resolution")
    @Expose
    public String imageResolution;

    @SerializedName("admin_id")
    @Expose
    public int adminId;

}
