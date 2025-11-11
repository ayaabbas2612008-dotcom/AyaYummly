package com.example.ayayummly.classes;

public class Recipe {
    private String cook;
    private String name;
    private String description;
    private double price;


    public Recipe() {
    }

    public Recipe(String cook, String name, String description, double price) {
        this.cook = cook;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getCook() {
        return cook;
    }

    public void setCook(String cook) {
        this.cook = cook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

