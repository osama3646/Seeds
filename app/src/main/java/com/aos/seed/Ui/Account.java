package com.aos.seed.Ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aos.seed.Model.Customer;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentAccountBinding;

import java.util.Locale;

public class Account extends Fragment {

    FragmentAccountBinding binding;
    private String LanguagecText;
    private String LanguagecCod;
    private SharedPreferences sharedPref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

          binding = FragmentAccountBinding.inflate(inflater,container,false);

        LanguagecText = binding.Language.getText().toString().trim();


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

        binding.Language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (LanguagecText.equals("English")){
                    LanguagecCod = "en";
                    setLanguage(LanguagecCod);
                    Toast.makeText(requireContext(),  "English "  , Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Account()).commit();

                }else if (LanguagecText.equals("عربي")){
                    LanguagecCod = "ar";
                    setLanguage(LanguagecCod);
                    Toast.makeText(requireContext(),  "عربي  "  , Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Account()).commit();
                }


            }
        });

        return binding.getRoot();
    }
    public void setLanguage(String languageCode) {
        Resources resources = this.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration,resources.getDisplayMetrics());
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("languageCode",LanguagecCod.toString());
//        editor.commit();
    };
}