package com.clothly.ecommerce.data.data.helper.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderMain implements Parcelable {
    private String heading;
    private String preHeading;
    @SerializedName("id")
    @Expose
    private String id;

    public SliderMain(String id, String tag, String imageName, String resolution) {
        this.id = id;
        this.tag = tag;
        this.imageName = imageName;
        this.resolution = resolution;
    }

    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("resolution")
    @Expose
    private String resolution;
    @SerializedName("admin_id")
    @Expose
    private String adminId;


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

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public void setPreHeading(String preHeading) {
        this.preHeading = preHeading;
    }

    public String getId() {
        return id;
    }

    public String getHeading() {
        return heading;
    }

    public String getPreHeading() {
        return preHeading;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.heading);
        dest.writeString(this.preHeading);
    }

    public SliderMain() {
    }

    protected SliderMain(Parcel in) {
        this.id = in.readString();
        this.heading = in.readString();
        this.preHeading = in.readString();
    }

    public static final Creator<SliderMain> CREATOR = new Creator<SliderMain>() {
        @Override
        public SliderMain createFromParcel(Parcel source) {
            return new SliderMain(source);
        }

        @Override
        public SliderMain[] newArray(int size) {
            return new SliderMain[size];
        }
    };
}