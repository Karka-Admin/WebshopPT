package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Liquid extends Product {
    protected int capacity;
    protected String composition;
    protected String type;

    public Liquid() {
        this.id = 0;
        this.quantity = 0;
        this.averageRating = 0;
        this.capacity = 0;
        this.price = 0.0F;
        this.name = null;
        this.brand = null;
        this.description = null;
        this.category = null;
        this.composition = null;
        this.type = null;
    }
    public Liquid(
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
            String type) {
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
    }
}
