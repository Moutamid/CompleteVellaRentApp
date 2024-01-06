package com.moutimid.vellarentapp.rentownerapp.model;

public class PropertyDetails {
    private Bathroom bathroom;
    private Bedroom bedroom;

    // Default constructor for Firebase
    public PropertyDetails() {
    }

    public Bathroom getBathroom() {
        return bathroom;
    }

    public Bedroom getBedroom() {
        return bedroom;
    }
}
