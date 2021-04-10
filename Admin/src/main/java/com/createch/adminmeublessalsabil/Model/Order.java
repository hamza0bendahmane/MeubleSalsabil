package com.createch.adminmeublessalsabil.Model;

import java.util.ArrayList;
import java.util.Date;

public class Order {

    Date date;
    int totalPrice;
    String[] comments;
    String state;
    ArrayList<Soldable> soldItems;

    public Order(Date date, int totalPrice, String[] comments, String state, ArrayList<Soldable> soldItems) {
        this.date = date;
        this.totalPrice = totalPrice;
        this.comments = comments;
        this.state = state;
        this.soldItems = soldItems;
    }

    public Order() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String[] getComments() {
        return comments;
    }

    public void setComments(String[] comments) {
        this.comments = comments;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
