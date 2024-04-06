package com.example.webshoppt.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
    protected List<Comment> comments;

    public Product() {
        this.id = 0;
        this.quantity = 0;
        this.averageRating = 0.0F;
        this.price = 0.0F;
        this.name = null;
        this.brand = null;
        this.description = null;
        this.category = null;
        this.comments = new ArrayList<>();
    }

    public Product(
            int id,
            int quantity,
            float averageRating,
            float price,
            String name,
            String brand,
            String description,
            String category,
            List<Comment> comments) {
        this.id = id;
        this.quantity = quantity;
        this.averageRating = averageRating;
        this.price = price;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.category = category;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return Integer.toString(id) + "\t|\t" + name + " | " + Float.toString(price) + " | "
                + Integer.toString(quantity);
    }
}
