package com.createch.adminmeublessalsabil.Model;



public class User {
    String email;
    String fname;
    String lname;
    String uid ;
    String phone;
    String photo;
    Adresse adresse;
    boolean blocked;

    public User() {
    }

    public User(String fname, String lname, String email, String phone, Adresse adresse, String photo, String uid ,boolean blocked) {
        this.email = email;
        this.fname = fname;
        this.phone = phone;
        this.photo = photo;
        this.adresse = adresse;
        this.lname = lname;
        this.uid =uid;
        this.blocked = blocked;
    }


    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
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

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
