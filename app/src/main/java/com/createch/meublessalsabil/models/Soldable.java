package com.createch.meublessalsabil.models;

import java.util.Date;

public class Soldable {

    String soldableID;
    String soldableName;
    Double soldablePrice;
    int soldableQte;
    String imageUrl;
    Date postDate;
    Category soldableCategory;
    String soldableDescription;

    public String getSoldableID() {
        return soldableID;
    }

    public void setSoldableID(String soldableID) {
        this.soldableID = soldableID;
    }

    public String getSoldableName() {
        return soldableName;
    }

    public void setSoldableName(String soldableName) {
        this.soldableName = soldableName;
    }

    public Double getSoldablePrice() {
        return soldablePrice;
    }

    public void setSoldablePrice(Double soldablePrice) {
        this.soldablePrice = soldablePrice;
    }

    public int getSoldableQte() {
        return soldableQte;
    }

    public void setSoldableQte(int soldableQte) {
        this.soldableQte = soldableQte;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Category getSoldableCategory() {
        return soldableCategory;
    }

    public void setSoldableCategory(Category soldableCategory) {
        this.soldableCategory = soldableCategory;
    }

    public String getSoldableDescription() {
        return soldableDescription;
    }

    public void setSoldableDescription(String soldableDescription) {
        this.soldableDescription = soldableDescription;
    }
}
