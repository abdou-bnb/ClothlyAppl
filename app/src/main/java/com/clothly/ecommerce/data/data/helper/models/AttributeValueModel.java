package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AttributeValueModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("attribute")
    @Expose
    public int attribute;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("attribute_title")
    @Expose
    public String rootTitle;
}
