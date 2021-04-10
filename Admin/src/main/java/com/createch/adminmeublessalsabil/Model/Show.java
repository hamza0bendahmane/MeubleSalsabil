package com.createch.adminmeublessalsabil.Model;

import java.util.ArrayList;

public class Show {
    String description;
    ArrayList<String> photos;
    double price;

    public Show() {
    }

    public Show(String description, ArrayList<String> photos, double price) {
        this.description = description;
        this.photos = photos;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
