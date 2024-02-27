package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Card {
    private String firstName;
    private String lastName;
    private String cardNumber;
    private String cvc;
    private String cardType;
    private LocalDate expirationDate;
}
