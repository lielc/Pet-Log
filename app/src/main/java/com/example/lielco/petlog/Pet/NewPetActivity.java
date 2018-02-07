package com.example.lielco.petlog.Pet;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.lielco.petlog.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class NewPetActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPUTRE = 1;
    ImageView petImage;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    static NewPetViewModel NewPetVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);

        NewPetVM = ViewModelProviders.of(this).get(NewPetViewModel.class);

        final EditText etPetName = findViewById(R.id.new_pet_name_field);
        progressBar = findViewById(R.id.new_pet_pb);
        petImage = findViewById(R.id.new_pet_image);
        petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });
        Button btnConfirm = findViewById(R.id.new_pet_confirm_btn);
        Button btnCancel = findViewById(R.id.new_pet_cancel_btn);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final Pet newPet = new Pet();
                newPet.setPetName(etPetName.getText().toString());
                if (imageBitmap != null) {
                    NewPetVM.saveImage(getApplicationContext(),imageBitmap, FirebaseAuth.getInstance().getUid(), new NewPetViewModel.Callback() {
                        @Override
                        public void onSuccess(String imageUrl) {
                            newPet.setPetImageUrl(imageUrl);
                            savePetAndClose(newPet);;
                        }

                        @Override
                        public void onFailure() {
                            savePetAndClose(newPet);
                        }
                    });
                }
                else {
                    savePetAndClose(newPet);
                }
            }
        });
    }

    private void savePetAndClose(Pet newPet) {
        NewPetVM.addNewPet(newPet);
        setResult(RESULT_OK);
        progressBar.setVisibility(View.GONE);
        finish();
    }


    private void addImage(){
        Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePhotoIntent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takePhotoIntent,REQUEST_IMAGE_CAPUTRE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPUTRE){
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                petImage.setImageBitmap(imageBitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}