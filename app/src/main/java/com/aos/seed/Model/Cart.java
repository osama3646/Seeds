package com.aos.seed.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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

    public Cart(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public boolean addToDatabase(){
        Log.d(TAG,"exit:");

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        customerId = user.getUid();

        Date date = Calendar.getInstance().getTime();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

        db.collection("Cart").whereEqualTo("productId",productId).whereEqualTo("customerId",customerId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot snapshots = task.getResult();
                    if (snapshots.isEmpty()){
                        db.collection("Cart").add(item).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                bool = false;
                            }
                        });
                    }else {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            db.collection("Cart").document(document.getId()).update("quantity",Integer.parseInt(document.get("quantity").toString())+1).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    bool = false;
                                }
                            });
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                    bool = false;
                }
            }
        });

        return bool;
    }



}
