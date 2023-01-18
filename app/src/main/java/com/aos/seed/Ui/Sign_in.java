package com.aos.seed.Ui;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.aos.seed.Model.Customer;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentSignInBinding;


public class Sign_in extends Fragment {

    FragmentSignInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignInBinding.inflate(inflater, container, false);

        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = binding.username.getText().toString();
                String Password = binding.Password.getText().toString();


                if (Email.isEmpty() || Password.isEmpty()) {

                    Toast.makeText(requireContext(), "NULL", Toast.LENGTH_SHORT).show();
                    return;

                } else {

                    Customer customer = new Customer(Email, Password);
                    customer.signIn();
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Store()).commit();


                }

            }
        });


        return binding.getRoot();
    }
}