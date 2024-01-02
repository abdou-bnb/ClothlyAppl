package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOrderList {

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("quantity")
    @Expose
    public String quantity;

    @SerializedName("product_title")
    @Expose
    public String productTitle;

    @SerializedName("product_image")
    @Expose
    public String imageUri;

    @SerializedName("attribute")
    @Expose
    public String attribute;
}
