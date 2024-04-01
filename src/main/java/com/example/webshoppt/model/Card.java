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
    private LocalDate expirationDate;

    public Card() {
        this.firstName = null;
        this.lastName = null;
        this.cardNumber = null;
        this.cvc = null;
        this.expirationDate = null;
    }

    public Card(String firstName, String lastName, String cardNumber, String cvc, LocalDate expirationDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.cardNumber = cardNumber;
        this.cvc = cvc;
        this.expirationDate = expirationDate;
    }
}
