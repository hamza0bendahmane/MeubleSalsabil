package com.createch.adminmeublessalsabil.Model;


import java.util.ArrayList;

public class Item extends Soldable {

    double length;
    double width;
    double height;
    ArrayList<String> colors;
    double quantity;
    ArrayList<String> materials;
    String category;
    double price;
    String image;
    String name;


    public Item() {
    }

    public Item(double lenght, double height, double width, String category, ArrayList<String> colors, double quantity, ArrayList<String> materials,
                double price, String image, String name) {
        this.length = lenght;
        this.width = width;
        this.colors = colors;
        this.height = height;
        this.quantity = quantity;
        this.materials = materials;
        this.category = category;
        this.price = price;
        this.image = image;
        this.name = name;


    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ArrayList<String> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<String> materials) {
        this.materials = materials;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        category = category;
    }
}
