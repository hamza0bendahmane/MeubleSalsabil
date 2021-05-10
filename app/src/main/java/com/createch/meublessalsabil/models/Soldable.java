package com.createch.meublessalsabil.models;

public class Soldable {


    String description;
    String color;
    double price;
    int quantity;
    String material;

    public Soldable(String description, String color, int quantity, String material, double price) {
        this.description = description;
        this.color = color;
        this.price = price;
        this.quantity = quantity;
        this.material = material;
    }

    public Soldable() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

}
