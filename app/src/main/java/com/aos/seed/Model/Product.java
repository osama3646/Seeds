package com.aos.seed.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class Product {
    String productId, name, description, storeId, size, humidity, light, temperature;
    ArrayList<String> image = new ArrayList<>(), category = new ArrayList<>();
    float price;
    int stock, quantity;

    public Product(String name, String description, float price, int stock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public void addProduct(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        HashMap<String,Object> item = new HashMap<>();
        item.put("name",name);
        item.put("description",description);
        item.put("category",category);
        item.put("image",image);
        item.put("storeId",storeId);
        item.put("price",price);
        item.put("stock",stock);
        item.put("size",size);
        item.put("humidity",humidity);
        item.put("light",light);
        item.put("temperature",temperature);
        db.collection("Products").add(item);
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
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

    public ArrayList<String> getCategory() {
        return category;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public void setCategory(ArrayList<String> category) {
        this.category = category;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}