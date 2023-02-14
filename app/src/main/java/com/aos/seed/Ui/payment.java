package com.aos.seed.Ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aos.seed.Adapter.CardRecycler;
import com.aos.seed.Adapter.PaymentRecycler;
import com.aos.seed.Model.Address;
import com.aos.seed.Model.CreditCard;
import com.aos.seed.Model.Customer;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentPaymentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class payment extends Fragment {

    FragmentPaymentBinding binding;
    LinearLayout addAddress, addCard;
    ImageView add, card;
    Button saveAddress, signIn1, submit;
    TextView saveCard, singUp, singIn;
    EditText addressName, region, city, district, number, name, date, username, password, name1, email, password1, phone;
    RecyclerView itemRecyclerView;
    PaymentRecycler paymentAdapter;
    CardRecycler cardAdapter;
    List<CreditCard> dataHolderCard = new ArrayList<>();
    List<Address> dataHolder = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPaymentBinding.inflate(inflater,container,false);

        binding.editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialog();
            }
        });
        binding.editCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialogCard();
            }
        });
        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                singIn();
            }
        });

        db.collection("Card").whereEqualTo("defaultCard",1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    for (QueryDocumentSnapshot snapshot : value){
                        binding.number.setText(snapshot.get("number").toString());
                        binding.name.setText(snapshot.get("name").toString());
                        binding.date.setText(snapshot.get("date").toString());
                    }
                }
            }
        });
        db.collection("Address").whereEqualTo("defaultAddress",1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    for (QueryDocumentSnapshot snapshot : value){
                        binding.addressName.setText(snapshot.get("name").toString());
                        binding.region.setText(snapshot.get("region").toString());
                        binding.city.setText(snapshot.get("city").toString());
                        binding.district.setText(snapshot.get("district").toString());
                    }
                }
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new cart()).commit();
            }
        });

        return binding.getRoot();
    }
    public void singIn(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.signin);
        singUp = dialog.findViewById(R.id.newAccount);
        username = dialog.findViewById(R.id.username);
        password = dialog.findViewById(R.id.Password);
        signIn1 = dialog.findViewById(R.id.signIn1);

        signIn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer customer = new Customer(username.getText().toString(), password.getText().toString());
                customer.signIn();
                dialog.dismiss();
            }
        });

        singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                singUp();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void singUp(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.singup);
        singIn = dialog.findViewById(R.id.singIn);
        submit = dialog.findViewById(R.id.submit);
        name1 = dialog.findViewById(R.id.name);
        email = dialog.findViewById(R.id.email);
        password1 = dialog.findViewById(R.id.password);
        phone = dialog.findViewById(R.id.phone);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Customer customer = new Customer(email.getText().toString(), password1.getText().toString());
                customer.setName(name1.getText().toString());
                customer.setPhone(phone.getText().toString());
                customer.signUp();
                dialog.dismiss();
            }
        });
        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                singIn();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void setDialogCard(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.credit_card);

        card = dialog.findViewById(R.id.add);
        addCard = dialog.findViewById(R.id.addCard);
        saveCard = dialog.findViewById(R.id.saveCard);
        number = dialog.findViewById(R.id.number);
        name = dialog.findViewById(R.id.name);
        date = dialog.findViewById(R.id.date);
        itemRecyclerView = dialog.findViewById(R.id.ItemRecyclerView);

        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        cardAdapter = new CardRecycler(getContext(), dataHolderCard);
        itemRecyclerView.setAdapter(cardAdapter);

        db.collection("Card").whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    dataHolderCard.clear();
                    for (QueryDocumentSnapshot document : value){
                        CreditCard card = new CreditCard(document.get("number").toString(), document.get("date").toString(), document.get("name").toString());
                        card.setDefaultCard(Integer.parseInt(document.get("defaultCard").toString()));
                        card.setCardId(document.getId());
                        dataHolderCard.add(card);
                        cardAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCard.setVisibility(View.VISIBLE);
            }
        });
        saveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCard.setVisibility(View.GONE);
                CreditCard card = new CreditCard(number.getText().toString(), date.getText().toString(), name.getText().toString());
                card.setCard();
                number.setText("");
                name.setText("");
                date.setText("");
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    public void setDialog(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.address);

        addAddress = dialog.findViewById(R.id.addAddress);
        add = dialog.findViewById(R.id.add);
        saveAddress = dialog.findViewById(R.id.saveAddress);
        addressName = dialog.findViewById(R.id.addressName);
        region = dialog.findViewById(R.id.region);
        city = dialog.findViewById(R.id.city);
        district = dialog.findViewById(R.id.district);
        itemRecyclerView = dialog.findViewById(R.id.ItemRecyclerView);

        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemRecyclerView.setItemAnimator(new DefaultItemAnimator());
        paymentAdapter = new PaymentRecycler(getContext(),dataHolder);
        itemRecyclerView.setAdapter(paymentAdapter);

        db.collection("Address").whereEqualTo("customerId", FirebaseAuth.getInstance().getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (!value.isEmpty()){
                    dataHolder.clear();
                    for (QueryDocumentSnapshot document : value){
                        Address address = new Address(document.get("name").toString(), document.get("region").toString(), document.get("city").toString(), document.get("district").toString());
                        address.setDefaultAddress(Integer.parseInt(document.get("defaultAddress").toString()));
                        address.setAddressId(document.getId());
                        dataHolder.add(address);
                        paymentAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddress.setVisibility(View.VISIBLE);
            }
        });
        saveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddress.setVisibility(View.GONE);
                Address address = new Address(addressName.getText().toString(), region.getText().toString(), city.getText().toString(), district.getText().toString());
                address.setAddress();
                addressName.setText("");
                region.setText("");
                city.setText("");
                district.setText("");
            }
        });



        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}