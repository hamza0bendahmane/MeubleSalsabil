package com.createch.adminmeublessalsabil.Model;

public class User implements Comparable {
    String email;
    String fname;
    String lname;
    String phone;
    String photo;
    Adresse adr;
    boolean blocked;

    public User() {
    }

    public User(String email, String fname, String phone, String photo, Adresse adr, boolean blocked, String lname) {
        this.email = email;
        this.fname = fname;
        this.phone = phone;
        this.photo = photo;
        this.adr = adr;
        this.lname = lname;
        this.blocked = blocked;
    }


    public Adresse getAdr() {
        return adr;
    }

    public void setAdr(Adresse adr) {
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

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof User) {
            User oo = (User) o;
            return this.getFname().compareTo(oo.getFname());

        } else
            return 1;
    }
}
