package com.aos.seed.Ui;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aos.seed.Adapter.StoreTopRecyclerView;
import com.aos.seed.Model.Product;
import com.aos.seed.Model.StoreTopView;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentAddProductBinding;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddProduct extends Fragment {

    String name, description, price, stock, category, size, humidity, light, temperature,image[];

    Button save;
    ImageSlider addImage;
    FirebaseFirestore db;
    FragmentAddProductBinding binding;
    View imageSelecter;
    AlertDialog dialog;
    StoreTopRecyclerView storeTopAdapter;
    List<StoreTopView> dataHolder = new ArrayList<>();
    ArrayList<StorageReference> referencesList;
    ArrayList<SlideModel> models = new ArrayList<>();
    ArrayList<String> imageUrl = new ArrayList<>(), categoryList = new ArrayList<>(), getCategoryList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddProductBinding.inflate(inflater,container,false);
        imageSelecter = getLayoutInflater().inflate(R.layout.image_selecter,null);
        db = FirebaseFirestore.getInstance();
        referencesList = new ArrayList<>();


        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.frameLayout, new Account()).commit();

            }
        });
        binding.categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        binding.categoryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        setAlertDialog();

        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkData();
            }
        });

        db.collection("Category").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()){
                        categoryList.add(document.get("name").toString());
                    }
                }
            }
        });

        storeTopAdapter = new StoreTopRecyclerView(dataHolder,getContext());
        binding.categoryRecyclerView.setAdapter(storeTopAdapter);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.dropdown_item,categoryList);
        ArrayAdapter<CharSequence> sizeAdapter = ArrayAdapter.createFromResource(getContext(),R.array.plant_size_array,R.layout.dropdown_item);
        ArrayAdapter<CharSequence> lightAdapter = ArrayAdapter.createFromResource(getContext(),R.array.plant_light_array,R.layout.dropdown_item);
        binding.category.setAdapter(adapter);
        binding.plantSize.setAdapter(sizeAdapter);
        binding.plantLight.setAdapter(lightAdapter);
        binding.category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StoreTopView view1 = new StoreTopView(categoryList.get(i),3);
                if (getCategoryList.contains(categoryList.get(i))){
                    storeTopAdapter.notifyItemRemoved(getCategoryList.indexOf(categoryList.get(i)));
                    dataHolder.remove(getCategoryList.indexOf(categoryList.get(i)));
                    getCategoryList.remove(categoryList.get(i));
                }else {
                    dataHolder.add(view1);
                    getCategoryList.add(categoryList.get(i));
                    storeTopAdapter.notifyDataSetChanged();
                }
                binding.category.setText("");
            }
        });
        LinearLayout camera = imageSelecter.findViewById(R.id.camera);
        LinearLayout gallery = imageSelecter.findViewById(R.id.gallery);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture,0);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);
            }
        });

        return binding.getRoot();
    }
    private void setAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(imageSelecter);
        builder.setTitle("Osama");
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog = builder.create();
    }
    private void saveImageToDb(Bitmap bitmap){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Date date = Calendar.getInstance().getTime();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference photoRef = storageRef.child("Product/HolderImage/"+user.getUid()+"_"+date);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                referencesList.add(taskSnapshot.getStorage());
                getImageFromDb(taskSnapshot.getStorage());
            }
        });
    }
    public void getImageFromDb(StorageReference reference){
        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageUrl.add(uri.toString());
                models.add(new SlideModel(uri.toString(),ScaleTypes.CENTER_CROP));
                binding.imageSlider.setImageList(models);
                binding.imageSlider.setVisibility(View.VISIBLE);
            }
        });
    }
    private void deleteImageFromDb(StorageReference reference){
        reference.delete();
    }
    private void checkData(){
        name = binding.productName.getText().toString();
        description = binding.description.getText().toString();
        price = binding.price.getText().toString();
        stock = binding.stock.getText().toString();
        category = binding.category.getText().toString();
        size = binding.plantSize.getText().toString();
        humidity = binding.plantHumidity.getText().toString()+"%";
        light = binding.plantLight.getText().toString();
        temperature = binding.plantTemperatureFrom.getText().toString()+" - "+binding.plantTemperatureTo.getText().toString()+"c";


        Product product = new Product(name, description, Float.parseFloat(price), Integer.parseInt(stock));
        product.setCategory(getCategoryList);
        product.setImage(imageUrl);
        product.setSize(size);
        product.setHumidity(humidity);
        product.setLight(light);
        product.setTemperature(temperature);
        product.addProduct();
        referencesList.clear();
        getFragmentManager().beginTransaction().replace(R.id.frameLayout,new Account()).commit();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                if (requestCode == 0) {
                    if (resultCode == RESULT_OK) {
                        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                        saveImageToDb(bitmap);
                    }
                }
                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    saveImageToDb(bitmap);
                }
                break;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!referencesList.isEmpty()){
            for (int i=0; i<referencesList.size();i++){
                deleteImageFromDb(referencesList.get(i));
            }
        }
    }
}