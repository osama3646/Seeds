package com.aos.seed.Model;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CreditCard {
    String name, number, date, cardId;
    int defaultCard;

    public CreditCard(String number, String date, String name) {
        this.number = number;
        this.date = date;
        this.name = name;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getDate() {
        return date;
    }

    public int getDefaultCard() {
        return defaultCard;
    }

    public void setDefaultCard(int defaultCard) {
        this.defaultCard = defaultCard;
    }

    public void setCard(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> card = new HashMap<>();
        card.put("name",name);
        card.put("number",number);
        card.put("date",date);
        card.put("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.collection("Card").whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    card.put("defaultCard",1);
                    db.collection("Card").add(card);
                }else {
                    card.put("defaultCard",0);
                    db.collection("Card").add(card);
                }
            }
        });
    }
}
