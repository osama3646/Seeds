package com.aos.seed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Model.Address;
import com.aos.seed.Model.CreditCard;
import com.aos.seed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class CardRecycler extends RecyclerView.Adapter<CardRecycler.ViewHolder> {

    private Context context;
    private List<CreditCard> cardList;

    public CardRecycler(Context context, List<CreditCard> cardList) {
        this.context = context;
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.add_credit_card,parent,false);
        return new CardRecycler.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardRecycler.ViewHolder holder, int position) {
        final CreditCard card = cardList.get(holder.getAdapterPosition());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        holder.name.setText(card.getName());
        holder.number.setText(card.getNumber());
        holder.date.setText(card.getDate());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (card.getDefaultCard() == 1){
                    db.collection("Card").document(card.getCardId()).delete();
                    db.collection("Card").limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                db.collection("Card").document(snapshot.getId()).update("defaultCard",1);
                            }
                        }
                    });
                }else {
                    db.collection("Card").document(card.getCardId()).delete();
                }
            }
        });
        if (card.getDefaultCard() == 1){
            holder.creditCard.setBackgroundResource(R.drawable.credit_card_bg);
        }
        else {
            holder.creditCard.setBackgroundResource(R.drawable.cresit_card_bg1);
        }
        holder.creditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Card").whereEqualTo("defaultCard",1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            db.collection("Card").document(document.getId()).update("defaultCard",0);
                            db.collection("Card").document(card.getCardId()).update("defaultCard",1);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView number, name, date;
        ImageView delete;
        LinearLayout creditCard;

        public ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            delete = view.findViewById(R.id.delete);
            creditCard = view.findViewById(R.id.creditCard);
        }
    }
}
