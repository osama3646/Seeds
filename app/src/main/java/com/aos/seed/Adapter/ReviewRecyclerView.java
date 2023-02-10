package com.aos.seed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Model.Product;
import com.aos.seed.Model.Review;
import com.aos.seed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ReviewRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<Review> reviews;
    Context context;

    public ReviewRecyclerView(List<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_coment,parent,false);
        RecyclerView.ViewHolder viewHolder = new viewModel(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Review review = reviews.get(holder.getAdapterPosition());
        viewModel model = (viewModel) holder;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Customer").document(review.getCustomerId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        model.name.setText(document.get("Name").toString());
                    }else {
                        model.name.setText("زائر");
                    }
                }
            }
        });
        model.time.setText(review.getTime());
        model.reviewDescription.setText(review.getDescription());
        if (review.getStar() == 1){
            model.star1.setImageResource(R.drawable.star);
        }if (review.getStar() == 2){
            model.star1.setImageResource(R.drawable.star);
            model.star2.setImageResource(R.drawable.star);
        }if (review.getStar() == 3){
            model.star1.setImageResource(R.drawable.star);
            model.star2.setImageResource(R.drawable.star);
            model.star3.setImageResource(R.drawable.star);
        }if (review.getStar() == 4){
            model.star1.setImageResource(R.drawable.star);
            model.star2.setImageResource(R.drawable.star);
            model.star3.setImageResource(R.drawable.star);
            model.star4.setImageResource(R.drawable.star);
        }if (review.getStar() == 5){
            model.star1.setImageResource(R.drawable.star);
            model.star2.setImageResource(R.drawable.star);
            model.star3.setImageResource(R.drawable.star);
            model.star4.setImageResource(R.drawable.star);
            model.star5.setImageResource(R.drawable.star);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    private class viewModel extends RecyclerView.ViewHolder {
        ImageView star1, star2, star3, star4, star5;
        TextView name, time, reviewDescription;
        public viewModel(View view) {
            super(view);
            star1 = view.findViewById(R.id.star1);
            star2 = view.findViewById(R.id.star2);
            star3 = view.findViewById(R.id.star3);
            star4 = view.findViewById(R.id.star4);
            star5 = view.findViewById(R.id.star5);
            name = view.findViewById(R.id.name);
            time = view.findViewById(R.id.time);
            reviewDescription = view.findViewById(R.id.reviewDescription);
        }
    }
}
