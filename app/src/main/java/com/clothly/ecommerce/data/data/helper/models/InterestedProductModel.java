package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InterestedProductModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("image_name")
    @Expose
    public String imageUri;

    @SerializedName("image_resolution")
    @Expose
    public String imageResolution;

    @SerializedName("prev_price")
    @Expose
    public float prevPrice;

    @SerializedName("current_price")
    @Expose
    public float currentPrice;
}
