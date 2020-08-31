package com.lain.event_planner.classes;

import android.os.Parcel;
import android.os.Parcelable;

public class EventLocation implements Parcelable {
    private String formatted_address;
    private Double latitude, longitude;

    private String place_id, name;

    public EventLocation() {
    }

    public EventLocation(String place_id, String name) {
        this.place_id = place_id;
        this.name = name;
    }

    protected EventLocation(Parcel in) {
        formatted_address = in.readString();
        if (in.readByte() == 0) {
            latitude = null;
        } else {
            latitude = in.readDouble();
        }
        if (in.readByte() == 0) {
            longitude = null;
        } else {
            longitude = in.readDouble();
        }
        place_id = in.readString();
        name = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(formatted_address);
        if (latitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(latitude);
        }
        if (longitude == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(longitude);
        }
        dest.writeString(place_id);
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EventLocation> CREATOR = new Creator<EventLocation>() {
        @Override
        public EventLocation createFromParcel(Parcel in) {
            return new EventLocation(in);
        }

        @Override
        public EventLocation[] newArray(int size) {
            return new EventLocation[size];
        }
    };

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

}
