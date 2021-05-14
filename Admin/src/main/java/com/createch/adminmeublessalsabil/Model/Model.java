package com.createch.adminmeublessalsabil.Model;


import java.util.ArrayList;

public class Model  {
     ArrayList<Item> items ;
    String description;
     String name;
    ArrayList<String> images ;
    ArrayList<String> materials ;
    ArrayList<String> contents ;
    ArrayList<String> colors ;
    int quantity;
    Double price ;

    public Model() {

    }

    public Model(ArrayList<Item> items, String description, String name, ArrayList<String> images,
                 ArrayList<String> colors, int quantity, Double price) {
        this.items = items;
        this.description = description;
        this.name = name;
        this.images = images;
        this.colors = colors;
        this.quantity = quantity;
        this.price = price;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getMaterials() {
        return materials;
    }

    public void setMaterials(ArrayList<String> materials) {
        this.materials = materials;
    }

    public ArrayList<String> getContents() {
        return contents;
    }

    public void setContents(ArrayList<String> contents) {
        this.contents = contents;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList<String> colors) {
        this.colors = colors;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
