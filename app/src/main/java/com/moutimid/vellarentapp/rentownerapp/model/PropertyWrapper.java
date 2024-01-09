package com.moutimid.vellarentapp.rentownerapp.model;

public class PropertyWrapper {
    private Property property;
    private VillaAmenities propertyAmenities;
    private Rules houseRules;

    public PropertyWrapper(Property property, VillaAmenities propertyAmenities, Rules houseRules) {
        this.property = property;
        this.propertyAmenities = propertyAmenities;
        this.houseRules = houseRules;
    }

    public Property getProperty() {
        return property;
    }

    public VillaAmenities getPropertyAmenities() {
        return propertyAmenities;
    }

    public Rules getHouseRules() {
        return houseRules;
    }
}
