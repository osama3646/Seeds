package com.aos.seed.Ui;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.aos.seed.Adapter.CartRecyclerView;
import com.aos.seed.Model.Cart;
import com.aos.seed.Model.Product;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentCartBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class cart extends Fragment {

   private FragmentCartBinding binding;
   private FirebaseFirestore db = FirebaseFirestore.getInstance();
   private CartRecyclerView cartAdapter;
   private ProgressDialog loading;
   private List<Product> dataHolder = new ArrayList<>();
   DecimalFormat df = new DecimalFormat("0.00");
   float subTotal, vat, vat1, total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater,container,false);
        binding.ItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.ItemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        loading = new ProgressDialog(getContext());
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        loading.show();
        cartAdapter = new CartRecyclerView(getContext(), dataHolder);
        binding.ItemRecyclerView.setAdapter(cartAdapter);
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Store()).commit();
            }
        });

        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new payment()).addToBackStack(null).commit();
            }
        });


        db.collection("Cart").whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    subTotal = 0;
                    loading.show();
                    dataHolder.clear();
                    for (QueryDocumentSnapshot document : value){
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
                                        vat += product.getQuantity()*product.getPrice()*0.15;
                                        vat1 = product.getQuantity()*product.getPrice()*0.15f;
                                        subTotal += product.getQuantity()*product.getPrice()-vat1;
                                        total += product.getQuantity()*product.getPrice();
                                        binding.subTotal.setText(df.format(subTotal));
                                        binding.vat.setText(df.format(vat));
                                        binding.discount.setText(df.format(0));
                                        binding.total.setText(df.format(total));
                                        onDestroy();
                                    }
                                }
                            }
                        });
                    }
                }
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }
}
