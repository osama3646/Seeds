package com.aos.seed.Model;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Address {
    String name, region, city, district, addressId;
    int defaultAddress;

    public Address(String name, String region, String city, String district) {
        this.name = name;
        this.region = region;
        this.city = city;
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getDistrict() {
        return district;
    }

    public int getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(int defaultAddress) {
        this.defaultAddress = defaultAddress;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public void setAddress(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,Object> address = new HashMap<>();
        address.put("name",name);
        address.put("region",region);
        address.put("city",city);
        address.put("district",district);
        address.put("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid());
        db.collection("Address").whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()){
                    address.put("defaultAddress",1);
                    db.collection("Address").add(address);
                }else {
                    address.put("defaultAddress",0);
                    db.collection("Address").add(address);
                }
            }
        });
    }
}
