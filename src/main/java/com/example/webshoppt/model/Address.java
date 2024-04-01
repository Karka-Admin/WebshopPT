package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Address {
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String secondaryStreetAddress;
    private String city;
    private String postalCode;
    private LocalDate birthDate;

    public Address() {
        this.firstName = null;
        this.lastName = null;
        this.streetAddress = null;
        this.secondaryStreetAddress = null;
        this.city = null;
        this.postalCode = null;
        this.birthDate = null;
    }

    public Address(
            String firstName,
            String lastName,
            String streetAddress,
            String secondaryStreetAddress,
            String city,
            String postalCode,
            LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.secondaryStreetAddress = secondaryStreetAddress;
        this.city = city;
        this.postalCode = postalCode;
        this.birthDate = birthDate;
    }
}
