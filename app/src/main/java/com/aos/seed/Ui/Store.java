package com.aos.seed.Ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

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

import com.aos.seed.Adapter.StoreRecyclerView;
import com.aos.seed.Model.Product;
import com.aos.seed.R;

import java.util.ArrayList;
import java.util.List;

public class Store extends Fragment {

    RecyclerView storeRecyclerView;
    List<Product> dataHolder;
    StoreRecyclerView storeAdapter;
    SharedPreferences shared;
    int layoutCase;
    String lorem;
    Button lcc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_store, container, false);

        lorem = "Lorem.";
//        lcc = root.findViewById(R.id.button);
        storeRecyclerView = root.findViewById(R.id.storeRecyclerView);
        dataHolder = new ArrayList<>();
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

//        lcc.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SharedPreferences.Editor editor = shared.edit();
//                if (layoutCase == 1){
//                    editor.putInt("case", 2);
//                }else {
//                    editor.putInt("case", 1);
//                }
//                editor.commit();
//                refresh();
//            }
//        });

        return root;
    }

    private void linearLayout() {
        storeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        storeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        for (int i=0;i<5;i++){
            Product product = new Product("Flower", lorem, 3.50f, 6, "seed");
            dataHolder.add(product);
        }
        storeAdapter = new StoreRecyclerView(dataHolder, getContext());
        storeRecyclerView.setAdapter(storeAdapter);
    }

    private void gridLayout() {
        storeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        storeRecyclerView.setItemAnimator(new DefaultItemAnimator());
        for (int i=0;i<5;i++){
            Product product = new Product("Flower", lorem, 3.50f, 6, "seed");
            dataHolder.add(product);
        }
        storeAdapter = new StoreRecyclerView(dataHolder, getContext());
        storeRecyclerView.setAdapter(storeAdapter);
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