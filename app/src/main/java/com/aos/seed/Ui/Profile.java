package com.aos.seed.Ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aos.seed.Model.StoreTopView;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.Executor;

public class Profile extends Fragment {

    FragmentProfileBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore mDb ;
    private String uid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater,container,false);



        auth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
        uid = auth.getCurrentUser().getUid();

        String name = binding.Fname.getText().toString();
        String phone = binding.Mobile.getText().toString();


         mDb.collection("Customer").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if (task.isSuccessful()) {
                     DocumentSnapshot snapshots = task.getResult();

                     binding.Fname.setText(snapshots.getString("Name"));
                     binding.em.setText(snapshots.getString("email"));
                     binding.Mobile.setText(snapshots.getString("phone"));


                 }

             }
         });




        binding.saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (name.isEmpty()){
//                    binding.Fname.setError("Full Name");
//                    binding.Fname.setFocusable(true);
//                }else if(phone.isEmpty()){
//                    binding.Mobile.setError("Mobile");
//                    binding.Mobile.setFocusable(true);
//                }else {
//                    mDb.collection("Customer").document(uid).update("Name",binding.Fname.getText().toString());
//                    mDb.collection("Customer").document(uid).update("phone",binding.Mobile.getText().toString());
//                    getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Store()).commit();
//                }
                mDb.collection("Customer").document(uid).update("Name",binding.Fname.getText().toString());
                mDb.collection("Customer").document(uid).update("phone",binding.Mobile.getText().toString());
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Store()).commit();



                }
        });


        return binding.getRoot();
    }
}