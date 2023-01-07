package com.aos.seed.Adapter.Ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aos.seed.Model.Customer;
import com.aos.seed.R;


public class Sign_in extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_sign__up, container, false);

        Customer customer = new Customer("saad@gmail.com","123Sss548");

        customer.signIn();

        return root;
    }
}