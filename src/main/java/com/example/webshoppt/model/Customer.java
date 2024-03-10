package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class Customer extends User {
    private Address shippingAddress;
    private Address billingAddress;
    private LocalDate birthDate;
    private List<Cart> purchases;
    private Card card;
    private Cart personalCart;
}
