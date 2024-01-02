package com.clothly.ecommerce.data.data.helper.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.clothly.ecommerce.data.data.helper.database.TableNames;


@Entity(tableName = TableNames.CODES)
public class CustomProductInventory implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @SerializedName("inventory")
    @Expose
    public int inventory_id;
    public int color_id;
    public int size_id;

    @SerializedName("product")
    @Expose
    public int product_id;
    public int available_qty;
    public String color_name;
    public String color_code;
    public String size_name;
    public String productName;

    @SerializedName("price")
    @Expose
    public float price;
    public String imageUri;
    public float newPrice;

    @SerializedName("quantity")
    @Expose
    public int currentQuantity;
    public String userID;
    public String attributeTitle;


    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public int getColor_id() {
        return color_id;
    }

    public void setColor_id(int color_id) {
        this.color_id = color_id;
    }

    public int getSize_id() {
        return size_id;
    }

    public void setSize_id(int size_id) {
        this.size_id = size_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getAvailable_qty() {
        return available_qty;
    }

    public void setAvailable_qty(int available_qty) {
        this.available_qty = available_qty;
    }

    public String getColor_name() {
        return color_name;
    }

    public void setColor_name(String color_name) {
        this.color_name = color_name;
    }

    public String getColor_code() {
        return color_code;
    }

    public void setColor_code(String color_code) {
        this.color_code = color_code;
    }

    public String getSize_name() {
        return size_name;
    }

    public void setSize_name(String size_name) {
        this.size_name = size_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.inventory_id);
        dest.writeInt(this.color_id);
        dest.writeInt(this.size_id);
        dest.writeInt(this.product_id);
        dest.writeInt(this.available_qty);
        dest.writeString(this.color_name);
        dest.writeString(this.color_code);
        dest.writeString(this.size_name);
    }

    public CustomProductInventory() {
    }

    protected CustomProductInventory(Parcel in) {
        this.inventory_id = in.readInt();
        this.color_id = in.readInt();
        this.size_id = in.readInt();
        this.product_id = in.readInt();
        this.available_qty = in.readInt();
        this.color_name = in.readString();
        this.color_code = in.readString();
        this.size_name = in.readString();
    }

    public static final Creator<CustomProductInventory> CREATOR = new Creator<CustomProductInventory>() {
        @Override
        public CustomProductInventory createFromParcel(Parcel source) {
            return new CustomProductInventory(source);
        }

        @Override
        public CustomProductInventory[] newArray(int size) {
            return new CustomProductInventory[size];
        }
    };
}
