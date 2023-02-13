package com.aos.seed.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Model.Address;
import com.aos.seed.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class PaymentRecycler extends RecyclerView.Adapter<PaymentRecycler.ViewHolder> {

    private Context context;
    private List<Address> addressList;

    public PaymentRecycler(Context context, List<Address> addressList) {
        this.context = context;
        this.addressList = addressList;
    }

    @NonNull
    @Override
    public PaymentRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.address_name,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentRecycler.ViewHolder holder, int position) {
        final Address address = addressList.get(holder.getAdapterPosition());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        holder.addressName.setText(address.getName());
        holder.region.setText(address.getRegion());
        holder.city.setText(address.getCity());
        holder.district.setText(address.getDistrict());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (address.getDefaultAddress() == 1){
                    db.collection("Address").document(address.getAddressId()).delete();
                    db.collection("Address").limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()){
                                db.collection("Address").document(snapshot.getId()).update("defaultAddress",1);
                            }
                        }
                    });
                }else {
                    db.collection("Address").document(address.getAddressId()).delete();
                }
            }
        });
        if (address.getDefaultAddress() == 1){
            holder.addressIcon.setImageResource(R.drawable.address);
            holder.checked.setVisibility(View.VISIBLE);
            holder.border.setBackgroundResource(R.drawable.border1);
        }
        else {
            holder.addressIcon.setImageResource(R.drawable.address_greay);
            holder.checked.setVisibility(View.INVISIBLE);
            holder.border.setBackgroundResource(R.drawable.border);
        }
        holder.border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection("Address").whereEqualTo("defaultAddress",1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            db.collection("Address").document(document.getId()).update("defaultAddress",0);
                            db.collection("Address").document(address.getAddressId()).update("defaultAddress",1);
                        }
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView addressName, region, city, district;
        ImageView checked, addressIcon, delete;
        LinearLayout border;

        public ViewHolder(View view) {
            super(view);
            addressName = view.findViewById(R.id.addressName);
            region = view.findViewById(R.id.region);
            city = view.findViewById(R.id.city);
            district = view.findViewById(R.id.district);
            checked = view.findViewById(R.id.checked);
            addressIcon = view.findViewById(R.id.addressIcon);
            delete = view.findViewById(R.id.delete);
            border = view.findViewById(R.id.border);
        }
    }
}
