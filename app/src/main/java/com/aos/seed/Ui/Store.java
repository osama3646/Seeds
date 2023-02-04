package com.aos.seed.Ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aos.seed.Adapter.StoreRecyclerView;
import com.aos.seed.Adapter.StoreTopRecyclerView;
import com.aos.seed.Model.Product;
import com.aos.seed.Model.StoreTopView;
import com.aos.seed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Store extends Fragment {

    RecyclerView storeRecyclerView, offerRecyclerView, categoryRecyclerView;
    List<Product> dataHolder;
    List<StoreTopView> categoryDataHolder, offerDataHolder;
    StoreRecyclerView storeAdapter;
    StoreTopRecyclerView storeTopAdapter;
    SharedPreferences shared;
    int layoutCase;
    String lorem;
    ImageView layoutState;
    ImageView CartNavigation;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);

        lorem = "Lorem.";
        layoutState = root.findViewById(R.id.layoutState);
        CartNavigation = root.findViewById(R.id.CartNavigation);
        storeRecyclerView = root.findViewById(R.id.storeRecyclerView);
        offerRecyclerView = root.findViewById(R.id.offer);
        categoryRecyclerView = root.findViewById(R.id.category);
        dataHolder = new ArrayList<>();
        categoryDataHolder = new ArrayList<>();
        offerDataHolder = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        shared = getContext().getSharedPreferences("storeRecyclerLayout", Context.MODE_PRIVATE);
        if (shared.contains("case")){
            layoutCase = shared.getInt("case",1);
        }else {
            SharedPreferences.Editor editor = shared.edit();
            editor.putInt("case",1);
            editor.commit();
            layoutCase = 1;
        }

        if (layoutCase == 1){
            linearLayout();
        }else {
            gridLayout();
        }
        offerRecycler();
        setCategoryRecyclerView();

        layoutState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = shared.edit();
                if (layoutCase == 1){
                    editor.putInt("case", 2);
                }else {
                    editor.putInt("case", 1);
                }
                editor.commit();
                refresh();
            }
        });

        CartNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new cart()).commit();
            }
        });

        return root;
    }


    private void offerRecycler(){
        offerRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        offerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        db.collection("Offer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        StoreTopView view = new StoreTopView(document.get("name").toString(),2);
                        offerDataHolder.add(view);
                    }
                }
                storeTopAdapter = new StoreTopRecyclerView(offerDataHolder,getContext());
                offerRecyclerView.setAdapter(storeTopAdapter);
            }
        });
    }

    private void setCategoryRecyclerView(){
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        db.collection("Category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        StoreTopView view = new StoreTopView(document.get("name").toString(), 1);
                        categoryDataHolder.add(view);
                    }
                    storeTopAdapter = new StoreTopRecyclerView(categoryDataHolder,getContext());
                    categoryRecyclerView.setAdapter(storeTopAdapter);
                }
            }
        });
    }


    private void linearLayout() {
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        db.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Product product = new Product(document.get("name").toString(),document.get("description").toString(),
                                Float.parseFloat(document.get("price").toString()),Integer.parseInt(document.get("stock").toString()),
                                document.get("category").toString());
                        product.setProductId(document.getId());
                        dataHolder.add(product);
                    }
                    storeAdapter = new StoreRecyclerView(dataHolder, getContext());
                    storeRecyclerView.setAdapter(storeAdapter);
                }
            }
        });
    }

    private void gridLayout() {
        storeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        storeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        db.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Product product = new Product(document.get("name").toString(),document.get("description").toString(),
                                Float.parseFloat(document.get("price").toString()),Integer.parseInt(document.get("stock").toString()),
                                document.get("category").toString());
                        ArrayList<String> image = (ArrayList<String>) document.get("image");
                        product.setImage(image);
                        product.setProductId(document.getId());

                        dataHolder.add(product);
                    }
                    storeAdapter = new StoreRecyclerView(dataHolder, getContext());
                    storeRecyclerView.setAdapter(storeAdapter);
                }
            }
        });
    }

    private void refresh(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getFragmentManager().beginTransaction().detach(this).commitNow();
            getFragmentManager().beginTransaction().attach(this).commitNow();
        }else {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}