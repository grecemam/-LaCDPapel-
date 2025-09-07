package com.example.lacdpapel.Models;

public class Product {
        private String Description;
        private String Name;
        private String PhotoUrl;
        private int Price;
        private String Uid;

    public Product() {
    }

    public Product(String description, String name, String photoUrl, int price, String uid) {
        Description = description;
        Name = name;
        PhotoUrl = photoUrl;
        Price = price;
        Uid = uid;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
