package com.createch.adminmeublessalsabil.Model;

import java.util.ArrayList;

public class Model extends Soldable {
    private ArrayList<Item> items;
    private String Description;

    public Model() {
    }

    public Model(ArrayList<Item> items, String description) {
        this.items = items;
        Description = description;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }


}
