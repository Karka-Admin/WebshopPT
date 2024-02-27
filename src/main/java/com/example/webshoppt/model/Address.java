package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Address {
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String secondaryStreetAddress;
    private String city;
    private String postalCode;
}
