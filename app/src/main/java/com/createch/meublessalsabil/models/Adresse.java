package com.createch.meublessalsabil.models;

public class Adresse {
    String firstAdr;
    String SecondAdr;
    int zip;
    String baladia;
    String wilaya;


    public Adresse() {
    }

    public Adresse(String firstAdr, String secondAdr, int zip, String baladia, String wilaya) {
        this.firstAdr = firstAdr;
        SecondAdr = secondAdr;
        this.zip = zip;
        this.baladia = baladia;
        this.wilaya = wilaya;
    }

    public String getFirstAdr() {
        return firstAdr;
    }

    public void setFirstAdr(String firstAdr) {
        this.firstAdr = firstAdr;
    }

    public String getSecondAdr() {
        return SecondAdr;
    }

    public void setSecondAdr(String secondAdr) {
        SecondAdr = secondAdr;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getBaladia() {
        return baladia;
    }

    public void setBaladia(String baladia) {
        this.baladia = baladia;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }
}
