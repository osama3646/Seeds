package com.aos.seed.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aos.seed.Adapter.CartRecyclerView;
import com.aos.seed.Model.Product;
import com.aos.seed.databinding.FragmentCartBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;



public class cart extends Fragment {


   private FragmentCartBinding binding;
   // ArrayList<Item> itemArrayList;
    CartRecyclerView cartRecyclerView;
    FirebaseFirestore db;
    List<Product> itemArrayList;
    RecyclerView storeRecyclerView;

    private String userid ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater,container,false);
        RecyclerView storeRecyclerView = binding.ItemRecyclerView;
        storeRecyclerView.setHasFixedSize(true);
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemArrayList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

       // cartRecyclerView = new CartRecyclerView(getContext() ,itemArrayList);


      //  getItem();

        db.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Product product = new Product(document.get("name").toString(),document.get("description").toString(),
                                Float.parseFloat(document.get("price").toString()),Integer.parseInt(document.get("stock").toString()));
                        product.setProductId(document.getId());
                        itemArrayList.add(product);
                    }
                    cartRecyclerView = new CartRecyclerView(getContext(),itemArrayList);
                    storeRecyclerView.setAdapter(cartRecyclerView);
                }
            }
        });


        return binding.getRoot();
    }




//    private void getItem(){
//        db.collection("Cart")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                    if (error != null){
//
//                        Log.e("firestore", error.getMessage());
//                        return;
//                    }
//                    for (DocumentChange dc : value.getDocumentChanges()){
//
//                        if (dc.getType() == DocumentChange.Type.ADDED){
//
//                        }
//                    }
//
//                });
//    }


}
