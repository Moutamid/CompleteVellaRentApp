package com.moutimid.vellarentapp.vellarentappadmin.model;

public class PropertyAmenities {
    private boolean airConditioning;
    private boolean dryer;
    private boolean equippedKitchen;
    private boolean furnished;
    private boolean garden;
    private boolean heating;
    private boolean parking;
    private boolean tv;
    private boolean washingMachine;
    private boolean wifi;

    // Default constructor for Firebase
    public PropertyAmenities() {
    }

    public boolean isAirConditioning() {
        return airConditioning;
    }

    public boolean isDryer() {
        return dryer;
    }

    public boolean isEquippedKitchen() {
        return equippedKitchen;
    }

    public boolean isFurnished() {
        return furnished;
    }

    public boolean isGarden() {
        return garden;
    }

    public boolean isHeating() {
        return heating;
    }

    public boolean isParking() {
        return parking;
    }

    public boolean isTv() {
        return tv;
    }

    public boolean isWashingMachine() {
        return washingMachine;
    }

    public boolean isWifi() {
        return wifi;
    }
}
