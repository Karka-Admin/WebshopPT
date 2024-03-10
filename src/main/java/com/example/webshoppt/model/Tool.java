package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Tool extends Product {
    private String type;

    public Tool(
            int id,
            int quantity,
            float averageRating,
            float price,
            String name,
            String brand,
            String description,
            String category,
            String type) {
        this.id = id;
        this.quantity = quantity;
        this.averageRating = averageRating;
        this.price = price;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.category = category;
        this.type = type;
    }
}
