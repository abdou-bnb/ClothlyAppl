package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("api_token")
    @Expose
    public String api_token;

    @SerializedName("currency_name")
    @Expose
    public String currencyName;

    @SerializedName("currency_font")
    @Expose
    public String currencyFont;

    @SerializedName("tax")
    @Expose
    public int tax;

    @SerializedName("payment")
    @Expose
    public PaymentCredentialModel paymentModel;


}
