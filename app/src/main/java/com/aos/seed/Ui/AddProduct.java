package com.aos.seed.Ui;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aos.seed.Model.Product;
import com.aos.seed.R;
import com.aos.seed.databinding.FragmentAddProductBinding;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddProduct extends Fragment {

    EditText productName, description, price, stock, category;
    Button save;
    ImageSlider addImage;
    FirebaseFirestore db;
    FragmentAddProductBinding binding;
    View imageSelecter;
    AlertDialog dialog;
    ArrayList<SlideModel> models = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddProductBinding.inflate(inflater,container,false);
        imageSelecter = getLayoutInflater().inflate(R.layout.image_selecter,null);
        db = FirebaseFirestore.getInstance();
        setAlertDialog();
        setAddImage();

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
    private void addProductToFirebase(){

    }
    private void setAddImage(){
        models.add(new SlideModel(R.drawable.placeholder_image,ScaleTypes.CENTER_CROP));
        models.add(new SlideModel(R.drawable.placeholder_image,ScaleTypes.CENTER_CROP));
        binding.addImage.setImageList(models);
        binding.addImage.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                switch (i){
                    case 0:
                        dialog.show();
                        break;
                }
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

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                Drawable image = new BitmapDrawable(getResources(),bp);
//                models.add(new SlideModel(image.,ScaleTypes.CENTER_CROP));
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
}