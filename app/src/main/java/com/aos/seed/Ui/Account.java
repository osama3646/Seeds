package com.aos.seed.Ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aos.seed.Model.Customer;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentAccountBinding;

public class Account extends Fragment {

    FragmentAccountBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

          binding = FragmentAccountBinding.inflate(inflater,container,false);


        binding.productDetail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new ProductDetail()).commit();
            }
        });
        binding.addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new AddProduct()).commit();
            }
        });

        binding.Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Profile()).commit();
            }
        });

        binding.signIp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Sign_in()).commit();

            }
        });


        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Sign_up()).commit();

            }
        });

        binding.signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer customer = new Customer("","");
                customer.signOut();
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Store()).commit();

            }
        });

        return binding.getRoot();
    }
}