package com.aos.seed.Ui;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Adapter.ReviewRecyclerView;
import com.aos.seed.Adapter.StoreTopRecyclerView;
import com.aos.seed.Model.Product;
import com.aos.seed.Model.Review;
import com.aos.seed.Model.StoreTopView;
import com.aos.seed.R;
//import com.aos.seed.databinding.FragmentProductDetailBinding;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProductDetail extends Fragment {

    RecyclerView categoryRecyclerView, userReview;
    LinearLayout addReview;
    List<StoreTopView> dataHolder;
    List<Review> reviewList;
    StoreTopRecyclerView storeTopAdapter;
    ReviewRecyclerView reviewAdapter;
    FirebaseFirestore db;
    View addReviewLayout;
    ImageSlider imageSlider;
    Product product;
    TextView name, cancelReview, saveReview, reviewCount, totalStar;
    AlertDialog dialog;
    ImageView star1, star2, star3, star4, star5,star01, star02, star03, star04, star05;
    EditText starDescription;
    int star, scoreTotal, star1Count, star2Count, star3Count, star4Count, star5Count, count;
    ProgressBar progressStar1, progressStar2, progressStar3, progressStar4, progressStar5;
    LinearLayout starLayout;
    String productId;
    Date addedDate;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<SlideModel> slideModel = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);
        addReviewLayout = getLayoutInflater().inflate(R.layout.add_review, null);
        setStar();

        categoryRecyclerView = root.findViewById(R.id.categoryRecyclerView);
        userReview = root.findViewById(R.id.userReview);
        addReview = root.findViewById(R.id.addReview);
        imageSlider = root.findViewById(R.id.imageSlider);
        name = root.findViewById(R.id.productName);
        reviewCount = root.findViewById(R.id.reviewCount);
        totalStar = root.findViewById(R.id.totalStar);
        progressStar1 = root.findViewById(R.id.progressStar1);
        progressStar2 = root.findViewById(R.id.progressStar2);
        progressStar3 = root.findViewById(R.id.progressStar3);
        progressStar4 = root.findViewById(R.id.progressStar4);
        progressStar5 = root.findViewById(R.id.progressStar5);
        star01 = root.findViewById(R.id.star1);
        star02 = root.findViewById(R.id.star2);
        star03 = root.findViewById(R.id.star3);
        star04 = root.findViewById(R.id.star4);
        star05 = root.findViewById(R.id.star5);
        db = FirebaseFirestore.getInstance();
        reviewList = new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(addReviewLayout);
        builder.setTitle(getString(R.string.add_reviews));


        dialog = builder.create();

        productId = getArguments().getString("productId");

        db.collection("Products").document(productId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                ArrayList<String> image = (ArrayList<String>) document.get("image");
                for (int i=0;i<image.size();i++){
                    slideModel.add(new SlideModel(image.get(i), ScaleTypes.CENTER_CROP));
                }
                name.setText(document.get("name").toString());
                imageSlider.setImageList(slideModel,ScaleTypes.CENTER_CROP);
            }
        });

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlert();
            }
        });



        dataHolder = new ArrayList<>();
        setCategoryRecyclerView();
        getReview();
        return root;
    }

    private void setCategoryRecyclerView(){
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        db.collection("Category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        StoreTopView view = new StoreTopView(document.get("name").toString(),3);
                        dataHolder.add(view);
                    }
                    storeTopAdapter = new StoreTopRecyclerView(dataHolder,getContext());
                    categoryRecyclerView.setAdapter(storeTopAdapter);
                }
            }
        });
    }
    private void setAlert(){
        dialog.show();
    }
    private void setStar(){
        star1 = addReviewLayout.findViewById(R.id.star1);
        star2 = addReviewLayout.findViewById(R.id.star2);
        star3 = addReviewLayout.findViewById(R.id.star3);
        star4 = addReviewLayout.findViewById(R.id.star4);
        star5 = addReviewLayout.findViewById(R.id.star5);
        saveReview = addReviewLayout.findViewById(R.id.saveReview);
        cancelReview = addReviewLayout.findViewById(R.id.cancelReview);
        starLayout = addReviewLayout.findViewById(R.id.starLayout);
        starDescription = addReviewLayout.findViewById(R.id.starDescription);


        star1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star1);
                star3.setImageResource(R.drawable.star1);
                star4.setImageResource(R.drawable.star1);
                star5.setImageResource(R.drawable.star1);
                starLayout.setBackground(null);
                star = 1;
            }
        });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star1);
                star4.setImageResource(R.drawable.star1);
                star5.setImageResource(R.drawable.star1);
                starLayout.setBackground(null);
                star = 2;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star1);
                star5.setImageResource(R.drawable.star1);
                starLayout.setBackground(null);
                star = 3;
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star1);
                starLayout.setBackground(null);
                star = 4;
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star1.setImageResource(R.drawable.star);
                star2.setImageResource(R.drawable.star);
                star3.setImageResource(R.drawable.star);
                star4.setImageResource(R.drawable.star);
                star5.setImageResource(R.drawable.star);
                starLayout.setBackground(null);
                star = 5;
            }
        });
        cancelReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        saveReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReview();
            }
        });


    }
    private void setReview(){
        if (star == 0){
            starLayout.setBackground(getResources().getDrawable(R.drawable.error_border));
        }if (starDescription.getText().toString().isEmpty()){
            starDescription.setError(getString(R.string.required));
            starDescription.requestFocus(1);
        }if (!starDescription.getText().toString().isEmpty() && star >= 1) {

            Date date = Calendar.getInstance().getTime();
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                addedDate = format.parse(format.format(date));
            }catch (ParseException e){
                e.printStackTrace();
            }

            Review review = new Review(user.getUid(), productId, starDescription.getText().toString(), star, addedDate);
            review.setReview();
            dialog.dismiss();
            star1.setImageResource(R.drawable.star1);
            star2.setImageResource(R.drawable.star1);
            star3.setImageResource(R.drawable.star1);
            star4.setImageResource(R.drawable.star1);
            star5.setImageResource(R.drawable.star1);
            starLayout.setBackground(null);
            starDescription.setText("");
            star = 0;
        }
    }
    private void getReview(){

        userReview.setLayoutManager(new LinearLayoutManager(getContext()));
        userReview.setItemAnimator(new DefaultItemAnimator());
        db.collection("Review").whereEqualTo("ProductId",productId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Review review = new Review(document.get("CustomerId").toString(), document.get("ProductId").toString(),document.get("Description").toString(),Integer.parseInt(document.get("Star").toString()),document.getDate("Time"));
                            reviewList.add(review);
                            if (Integer.parseInt(document.get("Star").toString()) == 5){
                                star5Count++;
                            }if (Integer.parseInt(document.get("Star").toString()) == 4){
                                star4Count++;
                            }if (Integer.parseInt(document.get("Star").toString()) == 3){
                                star3Count++;
                            }if (Integer.parseInt(document.get("Star").toString()) == 2){
                                star2Count++;
                            }if (Integer.parseInt(document.get("Star").toString()) == 1){
                                star1Count++;
                            }
                        }
                        count = star1Count+star2Count+star3Count+star4Count+star5Count;
                        reviewCount.setText(getString(R.string.based_on)+" "+count+getString(R.string.review));
                        progressStar1.setMax(count);
                        progressStar2.setMax(count);
                        progressStar3.setMax(count);
                        progressStar4.setMax(count);
                        progressStar5.setMax(count);
                        progressStar1.setProgress(star1Count);
                        progressStar2.setProgress(star2Count);
                        progressStar3.setProgress(star3Count);
                        progressStar4.setProgress(star4Count);
                        progressStar5.setProgress(star5Count);
                        star2Count*=2;
                        star3Count*=3;
                        star4Count*=4;
                        star5Count*=5;
                        scoreTotal = star1Count+star2Count+star3Count+star4Count+star5Count;
                        int i = scoreTotal/count;
                        totalStar.setText(""+scoreTotal/count);
                        if (i == 1){
                            star01.setImageResource(R.drawable.star);
                        }if (i == 2){
                            star01.setImageResource(R.drawable.star);
                            star02.setImageResource(R.drawable.star);
                        }if (i == 3){
                            star01.setImageResource(R.drawable.star);
                            star02.setImageResource(R.drawable.star);
                            star03.setImageResource(R.drawable.star);
                        }if (i == 4){
                            star01.setImageResource(R.drawable.star);
                            star02.setImageResource(R.drawable.star);
                            star03.setImageResource(R.drawable.star);
                            star04.setImageResource(R.drawable.star);
                        }if (i == 5){
                            star01.setImageResource(R.drawable.star);
                            star02.setImageResource(R.drawable.star);
                            star03.setImageResource(R.drawable.star);
                            star04.setImageResource(R.drawable.star);
                            star05.setImageResource(R.drawable.star);
                        }
                        reviewAdapter = new ReviewRecyclerView(reviewList,getContext());
                        userReview.setAdapter(reviewAdapter);
                    }else {

                    }
                }
            }
        });
    }
}