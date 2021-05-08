package com.createch.adminmeublessalsabil.Model;

import java.util.ArrayList;

public class Order {

    long date;
    int totalPrice;
    String userId;
    ArrayList<String> comments;
    String state;
    ArrayList<String> soldItems;

    public Order(long date, int totalPrice, ArrayList<String> comments, String state, ArrayList<String> soldItems, String userId) {
        // this.date = date;
        this.totalPrice = totalPrice;
        this.comments = comments;
        this.state = state;
        this.soldItems = soldItems;
        this.userId = userId;
    }

    public String getString() {
        return userId;
    }

    public void setString(String userId) {
        this.userId = userId;
    }

    public ArrayList<String> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(ArrayList<String> soldItems) {
        this.soldItems = soldItems;
    }

    public Order() {
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
        this.comments = comments;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
