package com.clothly.ecommerce.data.data.helper.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.clothly.ecommerce.data.data.helper.models.FeedBackModel;

public class FeedBackResponse {
    @SerializedName("status_code")
    @Expose
    private int statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private  FeedBackModel mFeedBackModel;

    public FeedBackModel getFeedBackModel() {
        return mFeedBackModel;
    }

    public void setFeedBackModel(FeedBackModel feedBackModel) {
        mFeedBackModel = feedBackModel;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
