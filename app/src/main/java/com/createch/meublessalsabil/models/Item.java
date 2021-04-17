package com.createch.meublessalsabil.models;

public class Item extends Soldable {

    Double itemWidth;
    Double itemLength;
    String itemColor;
    ItemCategory itemCategory;
    boolean isSoldeAlone; //can be solde apart from the whole product (item collection)

    public Double getItemWidth() {
        return itemWidth;
    }

    public void setItemWidth(Double itemWidth) {
        this.itemWidth = itemWidth;
    }

    public Double getItemLength() {
        return itemLength;
    }

    public void setItemLength(Double itemLength) {
        this.itemLength = itemLength;
    }

    public String getItemColor() {
        return itemColor;
    }

    public void setItemColor(String itemColor) {
        this.itemColor = itemColor;
    }

    public ItemCategory getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(ItemCategory itemCategory) {
        this.itemCategory = itemCategory;
    }

    public boolean isSoldeAlone() {
        return isSoldeAlone;
    }

    public void setSoldeAlone(boolean soldeAlone) {
        isSoldeAlone = soldeAlone;
    }
}
