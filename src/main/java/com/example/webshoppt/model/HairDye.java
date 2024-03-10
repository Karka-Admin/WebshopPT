package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HairDye extends Liquid {
    private String color;

    public HairDye(
            int id,
            int quantity,
            float averageRating,
            float price,
            String name,
            String brand,
            String description,
            String category,
            int capacity,
            String composition,
            String type,
            String color) {
        this.id = id;
        this.quantity = quantity;
        this.averageRating = averageRating;
        this.capacity = capacity;
        this.price = price;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.category = category;
        this.composition = composition;
        this.type = type;
        this.color = color;
    }
}
