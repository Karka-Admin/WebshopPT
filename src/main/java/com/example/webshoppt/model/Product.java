package com.example.webshoppt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public String toString() {
        return id + "\t|\t" + name + " | " + price + " | " + quantity;
    }
}
