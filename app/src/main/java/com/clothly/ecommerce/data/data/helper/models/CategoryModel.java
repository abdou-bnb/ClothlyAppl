package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("status")
    @Expose
    public int status;

    @SerializedName("admin_id")
    @Expose
    public int adminId;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("image_name")
    @Expose
    public String imageUrl;

    @SerializedName("image_resolution")
    @Expose
    public String imageResulation;
}
