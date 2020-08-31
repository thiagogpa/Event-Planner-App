package com.lain.event_planner.classes;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.NumberFormat;

//Class holds the info of a Product
public class Product implements Parcelable {

    private int id_;
    private String name;
    private Double price;
    private Integer quantity;
    private String category;
    private boolean selected;
    private String image_url;

    public Product(int id_, String name, Double price, Integer quantity, String category, boolean selected, String image_url) {
        this.id_ = id_;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.selected = selected;
        this.image_url = image_url;
    }

    protected Product(Parcel in) {
        id_ = in.readInt();
        name = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readDouble();
        }
        if (in.readByte() == 0) {
            quantity = null;
        } else {
            quantity = in.readInt();
        }
        category = in.readString();
        selected = in.readByte() != 0;
        image_url = in.readString();
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public double getTotalPrice(){
        return this.price * this.quantity;
    }

    public String getTotalPriceFormatted(){
        return NumberFormat.getCurrencyInstance().format(this.getTotalPrice()).replace(NumberFormat.getCurrencyInstance().getCurrency().getSymbol(), "$");
    }

    public int getId_() {
        return id_;
    }

    public void setId_(int id_) {
        this.id_ = id_;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    @NonNull
    @Override
    public String toString() {
        return "id " + this.id_ + " name " + this.name + " price " + this.price + " quantity " + this.quantity + " category " + this.category + " is selected " + this.selected + " url " + this.image_url;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_);
        dest.writeString(name);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(price);
        }
        if (quantity == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(quantity);
        }
        dest.writeString(category);
        dest.writeByte((byte) (selected ? 1 : 0));
        dest.writeString(image_url);
    }
}