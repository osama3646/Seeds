package com.aos.seed.Ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aos.seed.Adapter.StoreTopRecyclerView;
import com.aos.seed.Model.Product;
import com.aos.seed.Model.StoreTopView;
import com.aos.seed.R;
//import com.aos.seed.databinding.FragmentProductDetailBinding;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ProductDetail extends Fragment {

    RecyclerView categoryRecyclerView;
    List<StoreTopView> dataHolder;
    StoreTopRecyclerView storeTopAdapter;
    FirebaseFirestore db;
//    FragmentProductDetailBinding binding;
    ImageSlider imageSlider;
    Product product;
    TextView name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        binding = FragmentProductDetailBinding.inflate(inflater, container, false);
        View root = inflater.inflate(R.layout.fragment_product_detail, container, false);

//         categoryRecyclerView = binding.categoryRecyclerView;
//        imageSlider = binding.ImageSlider;
        categoryRecyclerView = root.findViewById(R.id.categoryRecyclerView);
        imageSlider = root.findViewById(R.id.imageSlider);
        name = root.findViewById(R.id.productName);
        db = FirebaseFirestore.getInstance();
        ArrayList<SlideModel> slideModel = new ArrayList<>();

        String productId = getArguments().getString("productId");

        db.collection("Products").document(productId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                ArrayList<String> image = (ArrayList<String>) document.get("image");
                for (int i=0;i<image.size();i++){
                    slideModel.add(new SlideModel(image.get(i), ScaleTypes.CENTER_CROP));
                }
                name.setText(document.get("name").toString());

            }
        });
        imageSlider.setImageList(slideModel,ScaleTypes.CENTER_CROP);


        dataHolder = new ArrayList<>();
        setCategoryRecyclerView();
//        return binding.getRoot();
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
}