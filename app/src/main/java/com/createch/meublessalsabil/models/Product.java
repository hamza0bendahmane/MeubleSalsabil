package com.createch.meublessalsabil.models;

import java.util.List;

public class Product extends Soldable {

    List<Item> itemCollection;
    //other attributes to be added


    public List<Item> getItemCollection() {
        return itemCollection;
    }

    public void setItemCollection(List<Item> itemCollection) {
        this.itemCollection = itemCollection;
    }
}
