package com.example.webshoppt.model;

public class Address {
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String secondaryStreetAddress;
    private String city;
    private String postalCode;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getSecondaryStreetAddress() {
        return secondaryStreetAddress;
    }

    public void setSecondaryStreetAddress(String secondaryStreetAddress) {
        this.secondaryStreetAddress = secondaryStreetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
