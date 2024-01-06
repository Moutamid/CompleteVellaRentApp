package com.moutimid.vellarentapp.rentownerapp.model;
public class HouseRules {
    private boolean petFriendly;
    private boolean smokerFriendly;

    // Default constructor for Firebase
    public HouseRules() {
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public boolean isSmokerFriendly() {
        return smokerFriendly;
    }
}
