package com.aos.seed.Model;


import android.text.format.DateUtils;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Review {

    private String CustomerId, ProductId, Description;
    private int Star;
    private Date Time;
    private FirebaseFirestore db;

    public Review(String customerId, String productId, String description, int star, Date time) {
        CustomerId = customerId;
        ProductId = productId;
        Description = description;
        Star = star;
        Time = time;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public String getProductId() {
        return ProductId;
    }

    public String getDescription() {
        return Description;
    }

    public int getStar() {
        return Star;
    }

    public String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        long time = Time.getTime();
        long now = System.currentTimeMillis();
        CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        return ago.toString();
    }

    public void setReview(){
        db = FirebaseFirestore.getInstance();
        Map<String,Object> item = new HashMap<>();
        item.put("CustomerId",CustomerId);
        item.put("ProductId",ProductId);
        item.put("Description",Description);
        item.put("Star",Star);
        item.put("Time",Time);
        db.collection("Review").add(item);



    }
}
