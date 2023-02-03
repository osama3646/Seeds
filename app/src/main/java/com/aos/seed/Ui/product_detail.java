package com.aos.seed.Ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aos.seed.Adapter.StoreTopRecyclerView;
import com.aos.seed.Model.StoreTopView;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentDesignerBinding;
import com.aos.seed.databinding.FragmentProductDetailBinding;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class product_detail extends Fragment {

    RecyclerView categoryRecyclerView;
    List<StoreTopView> dataHolder;
    StoreTopRecyclerView storeTopAdapter;
    FirebaseFirestore db;
    FragmentProductDetailBinding binding;
    ImageSlider imageSlider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProductDetailBinding.inflate(inflater, container, false);

         categoryRecyclerView = binding.categoryRecyclerView;
        imageSlider = binding.ImageSlider;
        ArrayList<SlideModel> slideModel = new ArrayList<>();

        slideModel.add(new SlideModel("https://www.edarabia.com/ar/wp-content/uploads/2020/04/learn-about-plants-their-importance-types-classifications.jpg", ScaleTypes.FIT));
        slideModel.add(new SlideModel("https://www.bostanji.net/img/articles/%D8%A3%D8%B3%D9%85%D8%A7%D8%A1%20%D9%86%D8%A8%D8%A7%D8%AA%D8%A7%D8%AA%20%D8%A7%D9%84%D8%B2%D9%8A%D9%86%D8%A9.jpg", ScaleTypes.FIT));
        slideModel.add(new SlideModel("https://www.edarabia.com/ar/wp-content/uploads/2019/04/7-most-important-best-shade-plants-cut-flowers.jpg", ScaleTypes.FIT));
        imageSlider.setImageList(slideModel,ScaleTypes.FIT);

        dataHolder = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        setCategoryRecyclerView();
        return binding.getRoot();
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