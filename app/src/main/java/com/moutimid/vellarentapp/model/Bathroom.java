package com.moutimid.vellarentapp.model;

public class Bathroom {
    private int fullBathroom;
    private int steamShower;
    private int toilet;

    // Default constructor for Firebase
    public Bathroom() {
    }

    public int getFullBathroom() {
        return fullBathroom;
    }

    public int getSteamShower() {
        return steamShower;
    }

    public int getToilet() {
        return toilet;
    }
}
