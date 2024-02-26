package com.example.webshoppt.model;

import java.time.LocalDate;
import java.util.List;

public class Customer extends User {
    private Address shippingAddress;
    private Address billingAddress;
    private LocalDate birthDate;
    private List<Cart> purchases;
    private Card card;
}
