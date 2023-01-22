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
import com.aos.seed.databinding.FragmentSignUpBinding;


public class Sign_up extends Fragment {

    FragmentSignUpBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSignUpBinding.inflate(inflater, container, false);

        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = binding.name.getText().toString();
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();
                String resetPassword = binding.resetPassword.getText().toString();
                String phone = binding.phone.getText().toString();

                if (name.isEmpty()){
                    binding.name.setError("rename");
                    binding.name.setFocusable(true);
                }if (email.isEmpty()){
                    binding.email.setError("rename");
                    binding.email.setFocusable(true);
                }if (password.isEmpty()){
                    binding.password.setError("rename");
                    binding.password.setFocusable(true);
                }if (resetPassword.isEmpty()){
                    binding.resetPassword.setError("rename");
                    binding.resetPassword.setFocusable(true);
                }if (phone.isEmpty()){
                    binding.phone.setError("rename");
                    binding.phone.setFocusable(true);
                }else {
                    if (password.equals(resetPassword)){
                        Customer customer = new Customer(email, password);
                        customer.setName(name);
                        customer.setPhone(phone);
                        customer.signUp();
                        getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Store()).commit();
                    }else {
                        binding.resetPassword.setError("Password is not same");
                    }
                }
            }
        });


        return binding.getRoot();
    }
}
