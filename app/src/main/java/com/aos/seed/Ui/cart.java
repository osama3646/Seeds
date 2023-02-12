package com.aos.seed.Ui;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aos.seed.Adapter.CartRecyclerView;
import com.aos.seed.Model.Cart;
import com.aos.seed.Model.Product;
import com.aos.seed.databinding.FragmentCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class cart extends Fragment {

   private FragmentCartBinding binding;
   private FirebaseFirestore db = FirebaseFirestore.getInstance();
   private CartRecyclerView cartAdapter;
   private List<Product> dataHolder = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater,container,false);
        binding.ItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cartAdapter = new CartRecyclerView(getContext(), dataHolder);
        binding.ItemRecyclerView.setAdapter(cartAdapter);


        db.collection("Cart").whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        for (QueryDocumentSnapshot document : task.getResult()){

                            Log.d("osama",document.getData()+"");
                            db.collection("Products").document(document.get("productId").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        if (task.getResult().exists()){
                                            DocumentSnapshot snapshot = task.getResult();
                                            Product product = new Product(snapshot.get("name").toString(), snapshot.get("description").toString(), Float.parseFloat(snapshot.get("price").toString()), Integer.parseInt(snapshot.get("stock").toString()));
                                            product.setImage((ArrayList<String>) snapshot.get("image"));
                                            product.setQuantity(Integer.parseInt(document.get("quantity").toString()));
                                            product.setProductId(snapshot.getId());
                                            dataHolder.add(product);
                                            cartAdapter.notifyDataSetChanged();
                                            Log.d("osama",snapshot.getData()+"");
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
        return binding.getRoot();
    }
}
