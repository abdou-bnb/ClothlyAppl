
package com.clothly.ecommerce.data.data.helper.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HomeData {

    @SerializedName("slider_images")
    @Expose
    private List<SliderMain> sliderImages = null;
    @SerializedName("featured")
    @Expose
    private List<Featured> featured = null;
    @SerializedName("home_banners")
    @Expose
    private List<HomeBanner> homeBanners = null;

    public List<SliderMain> getSliderImages() {
        return sliderImages;
    }

    public void setSliderImages(List<SliderMain> sliderImages) {
        this.sliderImages = sliderImages;
    }

    public List<Featured> getFeatured() {
        return featured;
    }

    public void setFeatured(List<Featured> featured) {
        this.featured = featured;
    }

    public List<HomeBanner> getHomeBanners() {
        return homeBanners;
    }

    public void setHomeBanners(List<HomeBanner> homeBanners) {
        this.homeBanners = homeBanners;
    }

}
