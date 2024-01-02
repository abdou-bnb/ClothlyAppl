
package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeBanner {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("admin_id")
    @Expose
    private String adminId;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("resolution")
    @Expose
    private String resolution;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("category_1")
    @Expose
    private Category1 category1;
    @SerializedName("category_2")
    @Expose
    private Category2 category2;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Category1 getCategory1() {
        return category1;
    }

    public void setCategory1(Category1 category1) {
        this.category1 = category1;
    }

    public Category2 getCategory2() {
        return category2;
    }

    public void setCategory2(Category2 category2) {
        this.category2 = category2;
    }

}
