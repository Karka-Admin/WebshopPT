package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    protected int id;
    protected int quantity;
    protected float averageRating;
    protected float price;
    protected String name;
    protected String brand;
    protected String description;
    protected String category;

    public Product() {
        this.id = 0;
        this.quantity = 0;
        this.averageRating = 0.0F;
        this.price = 0.0F;
        this.name = null;
        this.brand = null;
        this.description = null;
        this.category = null;
    }

    public Product(
            int id,
            int quantity,
            float averageRating,
            float price,
            String name,
            String brand,
            String description,
            String category) {
        this.id = id;
        this.quantity = quantity;
        this.averageRating = averageRating;
        this.price = price;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.category = category;
    }

    @Override
    public String toString() {
        return Integer.toString(id) + "\t|\t" + name + " | " + Float.toString(price) + " | "
                + Integer.toString(quantity);
    }
}
