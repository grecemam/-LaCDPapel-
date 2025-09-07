package com.example.lacdpapel.Models;

public class Basket {
    private String productId;

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int quantity;

    public Basket(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
    @Override
    public String toString() {
        return "Basket{productId='" + productId + "', quantity=" + quantity + "}";
    }
}
