package com.aos.seed.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Model.StoreTopView;
import com.aos.seed.R;

import java.util.List;

public class StoreTopRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StoreTopView> items;
    private Context context;

    public StoreTopRecyclerView(List<StoreTopView> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 1:{
                View view = inflater.inflate(R.layout.backgound_style1,parent,false);
                viewHolder = new StoreTopRecyclerView.viewModel1(view);
                break;
            }
            case 2:{
                View view = inflater.inflate(R.layout.store_offer,parent,false);
                viewHolder = new StoreTopRecyclerView.viewModel2(view);
                break;
            }
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final StoreTopView item = items.get(holder.getAdapterPosition());
        switch (holder.getItemViewType()){
            case 1:
                viewModel1 model1 = (viewModel1) holder;
                model1.category.setText(item.getData());
            case 2:
//                viewModel2 model2 = (viewModel2) holder;
//                model2.offer.setImageURI(Uri.EMPTY);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private class viewModel1 extends RecyclerView.ViewHolder {
        TextView category;
        public viewModel1(View view) {
            super(view);
            category = view.findViewById(R.id.category);
        }
    }

    private class viewModel2 extends RecyclerView.ViewHolder {
        ImageView offer;
        public viewModel2(View view) {
            super(view);
            offer = view.findViewById(R.id.offer);
        }
    }
}
