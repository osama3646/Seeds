package com.aos.seed.Ui;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Adapter.ReviewRecyclerView;
import com.aos.seed.Adapter.StoreTopRecyclerView;
import com.aos.seed.Model.Cart;
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
    ScrollView scrollView;
    Button addToCart;
    TextView name, plantSize, plantHumidity, plantLight, plantTemperature, description, price, stock, cancelReview, saveReview, reviewCount, totalStar;
    AlertDialog dialog;
    ImageView plus, minus, star1, star2, star3, star4, star5,star01, star02, star03, star04, star05;
    EditText starDescription;
    int iStock, star, scoreTotal, star1Count, star2Count, star3Count, star4Count, star5Count, count;
    ProgressBar progressStar1, progressStar2, progressStar3, progressStar4, progressStar5;
    LinearLayout starLayout;
    String productId;
    Date addedDate;
    ProgressDialog loading;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<SlideModel> slideModel = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);
        addReviewLayout = getLayoutInflater().inflate(R.layout.add_review, null);
        setStar();

        loading = new ProgressDialog(getContext());
        loading.show();
        loading.setContentView(R.layout.loading);
        loading.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        addToCart = root.findViewById(R.id.addToCart);
        categoryRecyclerView = root.findViewById(R.id.categoryRecyclerView);
        userReview = root.findViewById(R.id.userReview);
        addReview = root.findViewById(R.id.addReview);
        imageSlider = root.findViewById(R.id.imageSlider);
        name = root.findViewById(R.id.productName);
        plantSize = root.findViewById(R.id.plantSize);
        plantHumidity = root.findViewById(R.id.plantHumidity);
        plantLight = root.findViewById(R.id.plantLight);
        plantTemperature = root.findViewById(R.id.plantTemperature);
        description = root.findViewById(R.id.description);
        price = root.findViewById(R.id.price);
        stock = root.findViewById(R.id.stock);
        plus = root.findViewById(R.id.plus);
        minus = root.findViewById(R.id.minus);
        scrollView = root.findViewById(R.id.scrollView);
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

        userReview.setLayoutManager(new LinearLayoutManager(getContext()));
        userReview.setItemAnimator(new DefaultItemAnimator());
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());

        reviewAdapter = new ReviewRecyclerView(reviewList,getContext());
        userReview.setAdapter(reviewAdapter);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart cart = new Cart(product.getProductId(), Integer.parseInt(stock.getText().toString()));
                cart.addToDatabase();
            }
        });

        dialog = builder.create();

        productId = getArguments().getString("productId");

        db.collection("Products").document(productId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                product = new Product(document.get("name").toString(), document.get("description").toString(), Float.parseFloat(document.get("price").toString()), Integer.parseInt(document.get("stock").toString()));
                product.setSize(document.get("size").toString());
                product.setHumidity(document.get("humidity").toString());
                product.setLight(document.get("light").toString());
                product.setTemperature(document.get("temperature").toString());
                product.setImage((ArrayList<String>) document.get("image"));
                product.setCategory((ArrayList<String>) document.get("category"));
                product.setProductId(productId);

                name.setText(product.getName());
                description.setText(product.getDescription());
                price.setText(product.getPrice()+"");
                plantSize.setText(product.getSize());
                plantHumidity.setText(product.getHumidity());
                plantLight.setText(product.getLight());
                plantTemperature.setText(product.getTemperature());
                for (int i=0;i<product.getImage().size();i++){
                    slideModel.add(new SlideModel(product.getImage().get(i),ScaleTypes.CENTER_CROP));
                }
                for (int i=0;i<product.getCategory().size();i++){
                    StoreTopView view = new StoreTopView(product.getCategory().get(i),3);
                    dataHolder.add(view);
                }
                imageSlider.setImageList(slideModel,ScaleTypes.CENTER_CROP);
                storeTopAdapter = new StoreTopRecyclerView(dataHolder,getContext());
                categoryRecyclerView.setAdapter(storeTopAdapter);
                onDestroy();
            }
        });
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iStock = Integer.parseInt(stock.getText().toString());
                if (iStock<product.getStock()){
                    iStock++;
                    stock.setText(""+iStock);
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iStock = Integer.parseInt(stock.getText().toString());
                if (iStock>1){
                    iStock--;
                    stock.setText(""+iStock);
                }
            }
        });
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                Log.d(TAG,"i1: "+i1);
            }
        });
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlert();
            }
        });



        dataHolder = new ArrayList<>();
        getReview();
        return root;
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
            refresh();
        }
    }
    private void getReview(){
        db.collection("Review").whereEqualTo("ProductId",productId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    if (!task.getResult().isEmpty()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Review review = new Review(document.get("CustomerId").toString(), document.get("ProductId").toString(),document.get("Description").toString(),Integer.parseInt(document.get("Star").toString()),document.getDate("Time"));
                            reviewList.add(review);
                            reviewAdapter.notifyDataSetChanged();
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
                    }else {
                    }
                }
            }
        });
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }
    private void refresh(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getFragmentManager().beginTransaction().detach(this).commitNow();
            getFragmentManager().beginTransaction().attach(this).commitNow();
        }else {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }
}