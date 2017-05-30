package com.example.norgaard.barty.Models;

@org.parceler.Parcel
public class Location {

    public Double latitude;
    public Double longitude;

    public Location() {
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