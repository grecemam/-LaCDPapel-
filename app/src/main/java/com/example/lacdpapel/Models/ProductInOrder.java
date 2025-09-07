package com.example.lacdpapel.Models;

public class ProductInOrder {

    private Product product;
    private int quantity_product;

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity_product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity_product(int quantity_product) {
        this.quantity_product = quantity_product;
    }
}
