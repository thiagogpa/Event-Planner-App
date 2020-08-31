package com.lain.event_planner.classes;

import android.os.Parcel;
import android.os.Parcelable;

//Class holds all the guest of an event
public class Guests implements Parcelable {
    private int qtyMen = 0, qtyWomen= 0, qtyChildren= 0, qtyTotal= 0;

    //Constructor to be used when object is sent through an intent
    protected Guests(Parcel in) {
        qtyMen = in.readInt();
        qtyWomen = in.readInt();
        qtyChildren = in.readInt();
        qtyTotal = in.readInt();
    }

    public Guests() {
    }


    public int getQtyMen() {
        return qtyMen;
    }

    public void setQtyMen(int qtyMen) {
        this.qtyMen = qtyMen;
    }

    public int getQtyWomen() {
        return qtyWomen;
    }

    public void setQtyWomen(int qtyWomen) {
        this.qtyWomen = qtyWomen;
    }

    public int getQtyChildren() {
        return qtyChildren;
    }

    public void setQtyChildren(int qtyChildren) {
        this.qtyChildren = qtyChildren;
    }

    public int getQtyTotal() {
        return qtyTotal;
    }

    public void setQtyTotal(int qtyTotal) {
        this.qtyTotal = qtyTotal;
    }

    public int getTotal(){
        return (qtyMen + qtyWomen + qtyChildren);
    }


    //Methods create automatically to turn object into parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(qtyMen);
        parcel.writeInt(qtyWomen);
        parcel.writeInt(qtyChildren);
        parcel.writeInt(qtyTotal);
    }

    public static final Creator<Guests> CREATOR = new Creator<Guests>() {
        @Override
        public Guests createFromParcel(Parcel in) {
            return new Guests(in);
        }

        @Override
        public Guests[] newArray(int size) {
            return new Guests[size];
        }
    };


}
