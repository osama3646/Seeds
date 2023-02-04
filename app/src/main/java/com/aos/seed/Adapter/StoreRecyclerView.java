package com.aos.seed.Adapter;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Model.Cart;
import com.aos.seed.Model.Product;
import com.aos.seed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class StoreRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> products;
    private Context context;
    private SharedPreferences shared;

    public StoreRecyclerView(List<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 1:{
                View view1 = inflater.inflate(R.layout.product_card_model_1,parent,false);
                viewHolder = new viewModel1(view1);
                break;
            }
            case 2:{
                View view1 = inflater.inflate(R.layout.product_card_model_2,parent,false);
                viewHolder = new viewModel2(view1);
                break;
            }
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final Product product = products.get(holder.getAdapterPosition());
        switch (holder.getItemViewType()){
            case 1:{
                viewModel1 model = (viewModel1) holder;
                model.productName.setText(product.getName());
//                model.description.setText(product.getDescription());
//                model.stock.setText(product.getStock()+"");
//                model.category.setText(product.getCategory());
                model.price.setText(product.getPrice()+"");
                break;
            }
            case 2:{
                viewModel2 model = (viewModel2) holder;
                model.productName.setText(product.getName());
//                model.description.setText(product.getDescription());
//                model.stock.setText(product.getStock()+"");
//                model.category.setText(product.getCategory());
                model.price.setText(product.getPrice()+"");
                model.addToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG,"exit1:");
                        Cart cart = new Cart(product.getProductId(), 1);
                        cart.addToDatabase();
                    }
                });
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        shared = context.getSharedPreferences("storeRecyclerLayout",Context.MODE_PRIVATE);
        return shared.getInt("case",1);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    private class viewModel1 extends RecyclerView.ViewHolder {
        TextView productName, price;
//        ImageView productImage;
        public viewModel1(View view1) {
            super(view1);
            productName = view1.findViewById(R.id.productName);
            price = view1.findViewById(R.id.price);
//            productImage = view1.findViewById(R.id.productImage);
        }
    }

    private class viewModel2 extends RecyclerView.ViewHolder {
        TextView productName,description, stock, category, price;
        ImageView productImage, addToCart;
        public viewModel2(View view2) {
            super(view2);
            productName = view2.findViewById(R.id.productName);
//            description = view2.findViewById(R.id.description);
//            stock = view2.findViewById(R.id.stock);
//            category = view2.findViewById(R.id.category);
            price = view2.findViewById(R.id.price);
//            productImage = view2.findViewById(R.id.productImage);
            addToCart = view2.findViewById(R.id.addToCart);
        }
    }
}
