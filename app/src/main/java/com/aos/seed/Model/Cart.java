package com.aos.seed.Model;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    String productId;
    String customerId;
    int quantity;
    Date addedDate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean bool;


    private String CustomerId;
    private String ItemId;

    public Cart(String productId, String customerId, int quantity) {
        this.productId = productId;
        this.customerId = customerId;
        this.quantity = quantity;
    }

    public boolean addToDatabase(){

        Date date = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        try {
            addedDate = format.parse(format.format(date));
        }catch (ParseException e){
            e.printStackTrace();
        }
        Map<String, Object> item = new HashMap<>();
        item.put("productId", productId);
        item.put("customerId", customerId);
        item.put("quantity", quantity);
        item.put("addedDate", addedDate);

        db.collection("Cart").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                bool = true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                bool = false;
            }
        });

        return bool;
    }



}
