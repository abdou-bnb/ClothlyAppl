package com.clothly.ecommerce.data.data.helper.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.clothly.ecommerce.data.data.helper.models.CategoryModel;

import java.util.List;

public class AllCategoryResponse {
    @SerializedName("status_code")
    @Expose
    public int statusCode;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("data")
    @Expose
    public List<CategoryModel> dataList;
}
