package com.aos.seed.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Model.Prodect;

import java.util.List;

public class StoreRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Prodect> prodects;
    private Context context;
    private SharedPreferences shared;

    public StoreRecyclerView(List<Prodect> prodects, Context context) {
        this.prodects = prodects;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return prodects.size();
    }
}
