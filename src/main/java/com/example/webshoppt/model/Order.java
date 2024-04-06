package com.example.webshoppt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends Cart {
    private int id;
    private int cartId;
    private int assignedManagerId;
    private OrderStatus orderStatus;

    @Override
    public String toString() {
        return id + " | " + cartId + " | " + assignedManagerId + " | " + orderStatus;
    }
}
