package com.createch.meublessalsabil.models;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {

    public static String WAITING = "waiting";
    public static String ONWAY = "onway";
    public static String DELIVERED = "delivered";
    HashMap<String, Object> date;
    double totalPrice;
    double delivery;
    String userId;
    ArrayList<String> comments;
    String state;
    String soldItems;

    public Order(HashMap<String, Object> date, double totalPrice, double delivery, ArrayList<String> comments, String state, String soldItems, String userId) {
        this.date = date;
        this.totalPrice = totalPrice;
        this.comments = comments;
        this.state = state;
        this.soldItems = soldItems;
        this.userId = userId;
        this.delivery = delivery;
    }

    public Order() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(String soldItems) {
        this.soldItems = soldItems;
    }

    public HashMap<String, Object> getDate() {
        return date;
    }

    public void setDate(HashMap<String, Object> date) {
        this.date = date;
    }

    public double getDelivery() {
        return delivery;
    }

    public void setDelivery(double delivery) {
        this.delivery = delivery;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
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
