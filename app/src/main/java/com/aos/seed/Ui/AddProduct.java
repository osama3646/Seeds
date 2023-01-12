package com.aos.seed.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aos.seed.Model.Product;
import com.aos.seed.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class AddProduct extends Fragment {

    EditText productName, description, price, stock, category;
    Button save;
    List<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_product, container, false);

        productName = root.findViewById(R.id.productName);
        description = root.findViewById(R.id.description);
        price = root.findViewById(R.id.price);
        stock = root.findViewById(R.id.stock);
        category = root.findViewById(R.id.category);
        save = root.findViewById(R.id.save);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Product product = new Product(productName.getText().toString(), description.getText().toString(),
                        Integer.parseInt(price.getText().toString()), Integer.parseInt(stock.getText().toString()),
                        category.getText().toString());
                product.setImage(null);
                product.setStoreId(null);

                db.collection("Products").add(product).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(),"DocumentSnapshot added with ID: "+ documentReference.getId(),Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error adding document"+e, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        return root;
    }
}