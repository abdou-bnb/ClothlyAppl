package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("line_1")
    @Expose
    public String addressLine1;

    @SerializedName("line_2")
    @Expose
    public String addressLine2;

    @SerializedName("zip")
    @Expose
    public String zipCode;

    @SerializedName("state")
    @Expose
    public String state;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("user")
    @Expose
    public String userID;

    @SerializedName("city")
    @Expose
    public String city;

}
