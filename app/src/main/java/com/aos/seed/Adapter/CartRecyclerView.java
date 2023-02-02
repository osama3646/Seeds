package com.aos.seed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Model.Product;
import com.aos.seed.R;

import java.util.List;

public class CartRecyclerView extends RecyclerView.Adapter<CartRecyclerView.MyViewHolder> {

    private Context context;
    private List<Product> itemArrayList;

    public CartRecyclerView(Context context, List<Product> itemArrayList) {
        this.context = context;
        this.itemArrayList = itemArrayList;
    }





    @NonNull
    @Override
    public CartRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_card,parent,false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerView.MyViewHolder holder, int position) {

        final Product product = itemArrayList.get(holder.getAdapterPosition());
        holder.ProductName.setText(product.getName());
        holder.ItemValue.setText(product.getPrice()+"");
        //holder.ProductName.setText(product.getName());
        //holder.ProductName.setText(product.getName());


    }

    @Override
    public int getItemCount() {
        return  itemArrayList.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder {

        ImageView ItemImage;
        TextView ProductName, ItemValue, ItemsNumber;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ItemImage = itemView.findViewById(R.id.ItemImage);
            ProductName = itemView.findViewById(R.id.ProductName);
            ItemValue = itemView.findViewById(R.id.ItemValue);
            ItemsNumber = itemView.findViewById(R.id.ItemsNumber);
        }
    }
}
