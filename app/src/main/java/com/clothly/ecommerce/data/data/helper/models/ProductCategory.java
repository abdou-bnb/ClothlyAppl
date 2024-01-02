package com.clothly.ecommerce.data.data.helper.models;

import android.os.Parcel;
import android.os.Parcelable;

public class ProductCategory implements Parcelable {

    private String id;
    private String title;
    private int sort;
    private String image_name;


    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getSort() {
        return sort;
    }

    public String getImage_name() {
        return image_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeInt(this.sort);
        dest.writeString(this.image_name);
    }

    public ProductCategory() {
    }

    protected ProductCategory(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.sort = in.readInt();
        this.image_name = in.readString();
    }

    public static final Creator<ProductCategory> CREATOR = new Creator<ProductCategory>() {
        @Override
        public ProductCategory createFromParcel(Parcel source) {
            return new ProductCategory(source);
        }

        @Override
        public ProductCategory[] newArray(int size) {
            return new ProductCategory[size];
        }
    };
}
