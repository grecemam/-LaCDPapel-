package com.example.lacdpapel.Models;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;

public class OrderUserModel {
    private String order_id;
    private String id_user;
    private String order_number;
    private String status;
    private Integer price;
    private ArrayList<ProductInOrder> products;

    public OrderUserModel() {

    }

    public OrderUserModel(String order_id, String id_user, String order_number, String status, Integer price) {
        this.order_id = order_id;
        this.id_user = id_user;
        this.order_number = order_number;
        this.status = status;
        this.price = price;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public ArrayList<ProductInOrder> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductInOrder> products) {
        this.products = products;
    }


}
