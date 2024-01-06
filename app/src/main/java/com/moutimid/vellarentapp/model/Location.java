package com.moutimid.vellarentapp.model;
public class Location {
    private double lat;
    private double lng;
    private String title;

    // Default constructor for Firebase
    public Location() {
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getTitle() {
        return title;
    }
}
