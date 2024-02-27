package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Manager extends User {
    private boolean isAdmin;
    private List<Cart> managedPurchases;
}
