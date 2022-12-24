package com.aos.seed.Model;

public class Product {
    String name;
    String description;
    float price;
    int stock;
    String category;
    String image[];
    String storeId;
    int viewPort;

    public Product(String name, String description, float price, int stock, String category, int viewPort) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.viewPort = viewPort;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }

    public String[] getImage() {
        return image;
    }

    public String getStoreId() {
        return storeId;
    }

    public int getViewPort() {
        return viewPort;
    }
}