package com.example.webshoppt.model;

import java.util.List;

public class Order extends Cart {
    private int id;
    private Manager assignedManager;
    private List<Cart> orderedCart;
}
