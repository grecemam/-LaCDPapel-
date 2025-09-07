package com.example.lacdpapel.Models;

import java.util.ArrayList;

public class Order {
    private String order_id;
    private String id_user;
    private String order_number;
    private String address;
    private String number_card;
    private String date;
    private String status;
    private Integer price;
    private String payment_method;

    public Order() {
    }

    public Order(String order_id, String id_user, String order_number, String address, String number_card, String date, String status, Integer price, String payment_method) {
        this.order_id = order_id;
        this.id_user = id_user;
        this.order_number = order_number;
        this.address = address;
        this.number_card = number_card;
        this.date = date;
        this.status = status;
        this.price = price;
        this.payment_method = payment_method;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber_card() {
        return number_card;
    }

    public void setNumber_card(String number_card) {
        this.number_card = number_card;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
}
