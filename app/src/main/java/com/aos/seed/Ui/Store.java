package com.aos.seed.Ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aos.seed.Adapter.StoreRecyclerView;
import com.aos.seed.Model.Product;
import com.aos.seed.R;

import java.util.ArrayList;
import java.util.List;

public class Store extends Fragment {

    RecyclerView storeRecyclerView;
    List<Product> dataHolder;
    StoreRecyclerView storeAdapter;
    String lorem;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);

        lorem = "Lorem.";

        storeRecyclerView = root.findViewById(R.id.storeRecyclerView);
        dataHolder = new ArrayList<>();

        if (1 == 2){
            linearLayout();
        }else {
            gridLayout();
        }

        return root;
    }

    private void linearLayout() {
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        for (int i=0;i<5;i++){
            Product product = new Product("Flower", lorem, 3.50f, 6, "seed", 1);
            dataHolder.add(product);
        }
        storeAdapter = new StoreRecyclerView(dataHolder, getContext());
        storeRecyclerView.setAdapter(storeAdapter);
    }

    private void gridLayout() {
        storeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        storeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        for (int i=0;i<5;i++){
            Product product = new Product("Flower", lorem, 3.50f, 6, "seed", 2);
            dataHolder.add(product);
        }
        storeAdapter = new StoreRecyclerView(dataHolder, getContext());
        storeRecyclerView.setAdapter(storeAdapter);
    }
}