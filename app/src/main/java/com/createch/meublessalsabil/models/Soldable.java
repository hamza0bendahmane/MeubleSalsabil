package com.createch.meublessalsabil.models;

public class Soldable {


    String description;
    String color;
    int quantity;
    String material;
    String product_id;

    public Soldable(String description, String color, int quantity, String material,
                    String product_id) {
        this.description = description;
        this.color = color;
        this.quantity = quantity;
        this.material = material;
        this.product_id = product_id;
    }

    public Soldable() {
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


    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }


}
