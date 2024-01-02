package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailsDataModel {
    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("image_name")
    @Expose
    public String imageUri;

    @SerializedName("image_resolution")
    @Expose
    public String imageResolution;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("prev_price")
    @Expose
    public float prevPrice;

    @SerializedName("current_price")
    @Expose
    public float currentPrice;

    @SerializedName("avg_rating")
    @Expose
    public float avgRating;

    @SerializedName("rating_count")
    @Expose
    public int ratingCount;

    @SerializedName("favourite_count")
    @Expose
    public int favouriteCount;

    @SerializedName("favourited")
    @Expose
    public int isFav;

    @SerializedName("ordered")
    @Expose
    public int ordered;

    @SerializedName("review_images")
    @Expose
    public List<ProductDetailsImageModel> reviewImages;

    @SerializedName("recent_review")
    @Expose
    public  RecentReviewModel  recentReview;

    @SerializedName("images")
    @Expose
    public List<ProductDetailsImageModel> imageList;

    @SerializedName("attribute")
    @Expose
    public List<DetailsAttributeModel> attribute;

    @SerializedName("inventory")
    @Expose
    public List<InventoryModel> inventory;

    @SerializedName("interested_product")
    @Expose
    public List<InterestedProductModel> interestedProduct;

}
