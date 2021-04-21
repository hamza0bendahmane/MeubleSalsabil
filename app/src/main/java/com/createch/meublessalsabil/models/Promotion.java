package com.createch.meublessalsabil.models;

import java.sql.Date;

public class Promotion {


    String image;
    String productId;
    int discount;
    // Date endDate ;

    public Promotion() {
    }

    public Promotion(String image, String productId, Date endDate, int discount) {
        this.image = image;
        this.productId = productId;
        //  this.endDate = endDate;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

   /* public Date getEndDate() {
        return endDate;
    }*/

  /*  public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }*/
}
