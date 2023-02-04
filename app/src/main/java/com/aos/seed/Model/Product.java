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
    String productId, name, description, category, storeId;
    ArrayList<String> image = new ArrayList<>();
    float price;
    int stock;

    public Product(String name, String description, float price, int stock, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
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
        db.collection("Products").add(item);
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

    public String getCategory() {
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

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }
}