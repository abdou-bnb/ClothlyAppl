package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserRegistrationInfo {
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("type")
    @Expose
    public String type;

    @SerializedName("admin_id")
    @Expose
    public String adminId;

    @SerializedName("verification_token")
    @Expose
    public String verifyToken;
}
