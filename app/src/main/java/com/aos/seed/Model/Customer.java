package com.aos.seed.Model;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class Customer {

    private String email, password, Username, Mobile;
    private FirebaseAuth mAuth;

    public Customer(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void setUsername(String firstName) {
        this.Username = Username;
    }

    public void setMobile(String lastName) {
        this.Mobile = Mobile;
    }

    public void signUp(){
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);

        mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser users = mAuth.getCurrentUser();
//                    Customer customer = new Customer(email, password);
                    Map<String, Object> user = new HashMap<>();
                    user.put("first", Username);
                    user.put("last", Mobile);
                    user.put("email", email);
                    user.put("password", password);
                    db.collection("Customer").document(users.getUid()).set(user);
                }
            }
        });
    }
    public void signIn(){

        mAuth = FirebaseAuth.getInstance();
        Log.w(TAG, "signInWithEmail:mAuth");
        signOut();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Executor) this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            Log.w(TAG, "signInWithEmail:SU", task.getException());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                        }
                    }
                });

    }
    public void signOut(){
        FirebaseAuth.getInstance().signOut();

    }
}