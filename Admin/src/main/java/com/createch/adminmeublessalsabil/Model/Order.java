package com.createch.adminmeublessalsabil.Model;

import java.sql.Timestamp;
import java.util.ArrayList;

public class Order {

    //Timestamp date;
    int totalPrice;
    User user;
    ArrayList<String> comments;
    String state;
    ArrayList<Soldable> soldItems;

    public Order(Timestamp date, int totalPrice, ArrayList<String> comments, String state, ArrayList<Soldable> soldItems,User user) {
       // this.date = date;
        this.totalPrice = totalPrice;
        this.comments = comments;
        this.state = state;
        this.soldItems = soldItems;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Soldable> getSoldItems() {
        return soldItems;
    }

    public void setSoldItems(ArrayList<Soldable> soldItems) {
        this.soldItems = soldItems;
    }

    public Order() {
    }

  /*  public Timestamp getDate() {
      return date;
 }

    public void setDate(Timestamp date) {
        this.date = date;
    }
*/
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
