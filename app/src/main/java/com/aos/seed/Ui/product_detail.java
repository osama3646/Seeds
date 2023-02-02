package com.aos.seed.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aos.seed.Adapter.StoreTopRecyclerView;
import com.aos.seed.Model.StoreTopView;
import com.aos.seed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class product_detail extends Fragment {

    RecyclerView categoryRecyclerView;
    List<StoreTopView> dataHolder;
    StoreTopRecyclerView storeTopAdapter;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);

        categoryRecyclerView = root.findViewById(R.id.categoryRecyclerView);
        dataHolder = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        setCategoryRecyclerView();
        return root;
    }

    private void setCategoryRecyclerView(){
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        db.collection("Category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        StoreTopView view = new StoreTopView(document.get("name").toString(),3);
                        dataHolder.add(view);
                    }
                    storeTopAdapter = new StoreTopRecyclerView(dataHolder,getContext());
                    categoryRecyclerView.setAdapter(storeTopAdapter);
                }
            }
        });
    }
}