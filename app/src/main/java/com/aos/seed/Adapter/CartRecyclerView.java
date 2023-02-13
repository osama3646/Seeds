package com.aos.seed.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Model.Product;
import com.aos.seed.R;
import com.aos.seed.Ui.cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

public class CartRecyclerView extends RecyclerView.Adapter<CartRecyclerView.MyViewHolder> {

    private Context context;
    private List<Product> itemArrayList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public CartRecyclerView(Context context, List<Product> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }

    @NonNull
    @Override
    public CartRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card,parent,false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerView.MyViewHolder holder, int position) {

        final Product product = itemArrayList.get(holder.getAdapterPosition());
        holder.productName.setText(product.getName());
        holder.price.setText(df.format(product.getPrice()*product.getQuantity()));
        holder.stock.setText(product.getQuantity()+"");
        if (product.getQuantity() == 1){
            holder.minus1.setImageResource(R.drawable.delete);
        }else {
            holder.minus1.setImageResource(R.drawable.minus);
        }

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getQuantity()<product.getStock()){
                    db.collection("Cart")
                            .whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .whereEqualTo("productId", product.getProductId())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                        product.setQuantity(product.getQuantity()+1);
                                        db.collection("Cart").document(snapshot.getId()).update("quantity",product.getQuantity());
                                        if (product.getQuantity() == 1){
                                            holder.minus1.setImageResource(R.drawable.delete);
                                        }else {
                                            holder.minus1.setImageResource(R.drawable.minus);
                                        }
                                    }
                                }
                            });
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product.getQuantity()>1){
                    db.collection("Cart")
                            .whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .whereEqualTo("productId", product.getProductId())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                        product.setQuantity(product.getQuantity()-1);
                                        db.collection("Cart").document(snapshot.getId()).update("quantity",product.getQuantity());
                                        if (product.getQuantity() == 1){
                                            holder.minus1.setImageResource(R.drawable.delete);
                                        }else {
                                            holder.minus1.setImageResource(R.drawable.minus);
                                        }
                                    }
                                }
                            });
                }
                else {
                    db.collection("Cart")
                            .whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .whereEqualTo("productId", product.getProductId())
                            .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (QueryDocumentSnapshot snapshot : task.getResult()){
                                        db.collection("Cart").document(snapshot.getId()).delete();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return  itemArrayList.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {
        ImageView productImage, minus1;
        TextView productName, price, stock;
        FrameLayout plus, minus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
            stock = itemView.findViewById(R.id.stock);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            minus1 = itemView.findViewById(R.id.minus1);
        }
    }
}
