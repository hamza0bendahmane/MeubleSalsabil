package com.createch.meublessalsabil.models;



public class Promotion {


    String image;
    String product_id;
    int discount;
    String endDate;

    public Promotion() {
    }

    public Promotion(String image, String productId, String endDate, int discount) {
        this.image = image;
        this.product_id = productId;
        this.endDate = endDate;
        this.discount = discount;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
