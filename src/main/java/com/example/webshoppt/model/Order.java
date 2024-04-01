package com.example.webshoppt.model;

import java.util.List;

public class Order extends Cart {
    private int id;
    private boolean fullfilled;
    private Manager assignedManager;
    private List<Cart> orderedCart;
}
