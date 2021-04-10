package com.createch.adminmeublessalsabil.Model;


public class User {
    String email;
    String fname;
    String phone;
    String photo;
    String wilaya;
    String adr;
    boolean blocked;

    public User() {
    }

    public User(String email, String fname, String phone, String photo, String wilaya, String adr, boolean blocked) {
        this.email = email;
        this.fname = fname;
        this.phone = phone;
        this.photo = photo;
        this.wilaya = wilaya;
        this.adr = adr;
        this.blocked = blocked;
    }


    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getAdr() {
        return adr;
    }

    public void setAdr(String adr) {
        this.adr = adr;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
