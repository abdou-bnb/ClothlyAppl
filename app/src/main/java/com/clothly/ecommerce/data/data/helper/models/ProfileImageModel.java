package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImageModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("image_name")
    @Expose
    public String imageUrl;

    @SerializedName("image_resolution")
    @Expose
    public String imageResolution;
}
