package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InventoryModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("product")
    @Expose
    public int productId;

    @SerializedName("quantity")
    @Expose
    public int quantity;

    @SerializedName("attributes")
    @Expose
    public String attribute;
}
