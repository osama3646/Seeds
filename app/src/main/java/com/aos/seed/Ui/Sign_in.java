package com.aos.seed.Ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.aos.seed.Model.Customer;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentSignInBinding;


public class Sign_in extends Fragment {

    FragmentSignInBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        binding = FragmentSignInBinding.inflate(inflater,container,false);

        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Customer customer = new Customer("saad@gmail.com","123Sss548");

                customer.signIn();

            }
        });



        return binding.getRoot();
    }
}