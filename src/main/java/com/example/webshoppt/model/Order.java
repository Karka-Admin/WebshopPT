package com.example.webshoppt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Order extends Cart {
    private int id;
    private int cartId;
    private int assignedManagerId;
    private LocalDate orderDate;
    private OrderStatus orderStatus;

    @Override
    public String toString() {
        return "ID: " + id + " | CART_ID: " + cartId + " | MGR_ID: " + assignedManagerId + " | DATE: "
                + orderDate + " | " + orderStatus;
    }
}
