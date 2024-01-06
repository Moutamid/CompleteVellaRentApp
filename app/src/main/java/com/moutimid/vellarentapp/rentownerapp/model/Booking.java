package com.moutimid.vellarentapp.rentownerapp.model;
// Import the necessary Firebase libraries

public class Booking {
    private String creditCardNumber;
    private String customerName;
    private int numberOfPersons;
    private String date;
    private String token;
    private String id;
    boolean verified;

    // Default constructor
    public Booking() {
        // Default constructor required for Firebase
    }

    // Parameterized constructor

    public Booking(String creditCardNumber, String customerName, int numberOfPersons, String date, String token, String id, boolean verified) {
        this.creditCardNumber = creditCardNumber;
        this.customerName = customerName;
        this.numberOfPersons = numberOfPersons;
        this.date = date;
        this.token = token;
        this.id = id;
        this.verified = verified;
    }

    // Getters and setters
    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(int numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}

