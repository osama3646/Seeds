package com.aos.seed.Model;

public class Product {
    String name;
    String description;
    float price;
    int stock;
    String category;
    String image[];
    String storeId;

    public Product(String name, String description, float price, int stock, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
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

}