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


                final String Username = binding.username.getText().toString();
                final String Email = binding.email.getText().toString();
                final String Password = binding.password.getText().toString();
                final String Reset_Password = binding.resetPassword.getText().toString();
                final String Mobile = binding.mobile.getText().toString();


                if (Username.isEmpty() || Email.isEmpty() || Password.isEmpty() || Reset_Password.isEmpty() ||
                        Mobile.isEmpty()) {


                } else if (!Password.equals(Reset_Password)) {


                } else {
                    Customer customer = new Customer(Email, Password);
                    customer.setUsername(Username);
                    customer.setMobile(Mobile);
                    customer.signUp();
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Store()).commit();


                }

            }
        });


        return binding.getRoot();
    }
}
