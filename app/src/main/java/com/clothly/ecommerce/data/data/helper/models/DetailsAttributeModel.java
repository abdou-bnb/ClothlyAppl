package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailsAttributeModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("value")
    @Expose
    public List<AttributeValueModel> attributeList;

}
