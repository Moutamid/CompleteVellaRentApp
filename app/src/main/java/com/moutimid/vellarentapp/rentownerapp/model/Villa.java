package com.moutimid.vellarentapp.rentownerapp.model;


import java.util.List;
import java.util.Map;

public class Villa {
    String available;
    private HouseRules houseRules;
    private Location location;
    private PropertyAmenities propertyAmenities;
    public String rules;
    private PropertyDetails propertyDetails;
    private int area;
    private int bathRoom;
    private int bedroom;
    private String description;
    private String name;
    private int roomType;
    private int bill;
    public int no_of_persons;
    boolean bills_included;
    String image, userImage, userName;
    String key;
    private double lat;
    private double lng;
    private String title;

    public String token;
    public String city_name;
    public String town_name;
    public String available_dates;
    public List<String> selectedImageUris;

    public double distance;
    public boolean verified;
    public String ownerID;

    public Villa() {
    }

    public Villa(double lat, double lng, String title) {
        this.lat = lat;
        this.lng = lng;
        this.title = title;
    }

    public HouseRules getHouseRules() {
        return houseRules;
    }

    public Location getLocation() {
        return location;
    }

    public PropertyAmenities getPropertyAmenities() {
        return propertyAmenities;
    }

    public PropertyDetails getPropertyDetails() {
        return propertyDetails;
    }

    public int getArea() {
        return area;
    }

    public int getBathRoom() {
        return bathRoom;
    }

    public int getBedroom() {
        return bedroom;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getRoomType() {
        return roomType;
    }

    public String getImage() {
        return image;
    }

    public int getBill() {
        return bill;
    }

    public boolean isBills_included() {
        return bills_included;
    }

    public String getUserImage() {

        return userImage;
    }

    public String getUserName() {
        return userName;
    }

    public String getKey() {
        return key;
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

    public void setHouseRules(HouseRules houseRules) {
        this.houseRules = houseRules;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setPropertyAmenities(PropertyAmenities propertyAmenities) {
        this.propertyAmenities = propertyAmenities;
    }

    public void setPropertyDetails(PropertyDetails propertyDetails) {
        this.propertyDetails = propertyDetails;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setName(String name) {
        this.name = name;
    }

    private Map<String, String> images;

    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}