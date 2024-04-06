package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Cart {
    private int id;
    private int chatId;
    private List<Product> cartItems;
    private List<Comment> messages;
}
