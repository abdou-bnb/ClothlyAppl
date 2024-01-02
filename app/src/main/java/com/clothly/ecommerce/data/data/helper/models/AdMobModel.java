package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdMobModel {
    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("banner_status")
    @Expose
    public String bannerStatus;

    @SerializedName("banner_id")
    @Expose
    public String bannerId;

    @SerializedName("banner_unit_id")
    @Expose
    public String bannerUnitId;

    @SerializedName("interstitial_status")
    @Expose
    public String interstitialStatus;

    @SerializedName("interstitial_id")
    @Expose
    public String interstitialId;

    @SerializedName("interstitial_unit_id")
    @Expose
    public String interstitialUnitId;

    @SerializedName("video_status")
    @Expose
    public String videoStatus;

    @SerializedName("video_id")
    @Expose
    public String videoId;

    @SerializedName("video_unit_id")
    @Expose
    public String videoUnitId;

}
