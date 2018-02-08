package com.example.lielco.petlog.Pet;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lielco.petlog.R;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.Random;

public class NewPetActivity extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPUTRE = 1;
    static final int ADD_IMAGE_CAMERA = 1;
    static final int ADD_IMAGE_GALLERY = 2;
    ImageView petImage;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    static NewPetViewModel NewPetVM;

    ImageView btnCamera;
    ImageView btnGallery;

    EditText etPetName;
    EditText etPetGender;
    EditText etPetType;
    EditText etPetBreed;
    EditText etPetBirthday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pet);

        etPetName = findViewById(R.id.new_pet_name_field);
        etPetGender = findViewById(R.id.new_pet_gender_field);
        etPetType = findViewById(R.id.new_pet_type_field);
        etPetBreed = findViewById(R.id.new_pet_breed_field);
        etPetBirthday = findViewById(R.id.new_pet_birthday_field);

        NewPetVM = ViewModelProviders.of(this).get(NewPetViewModel.class);

        progressBar = findViewById(R.id.new_pet_pb);
        petImage = findViewById(R.id.new_pet_image);

        btnGallery = findViewById(R.id.new_pet_gallery);
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage(ADD_IMAGE_GALLERY);
            }
        });

        btnCamera = findViewById(R.id.new_pet_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage(ADD_IMAGE_CAMERA);
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
                if (etPetName.getText().toString() != null
                        && !(etPetName.getText().toString().isEmpty())
                        && !(etPetName.getText().toString().equals(""))) {
                    newPet.setPetName(etPetName.getText().toString());

                    if (etPetGender.getText() != null) {
                        newPet.setPetGender(etPetGender.getText().toString());
                    }

                    if (etPetType.getText() != null) {
                        newPet.setPetType(etPetType.getText().toString());
                    }

                    if (etPetBreed.getText() != null) {
                        newPet.setPetBreed(etPetBreed.getText().toString());
                    }

                    if (etPetBirthday.getText() != null) {
                        newPet.setPetBirthday(etPetBirthday.getText().toString());
                    }

                    if (imageBitmap != null) {
                        NewPetVM.saveImage(getApplicationContext(), imageBitmap, FirebaseAuth.getInstance().getUid(), new NewPetViewModel.Callback() {
                            @Override
                            public void onSuccess(String imageUrl) {
                                newPet.setPetImageUrl(imageUrl);
                                savePetAndClose(newPet);
                                ;
                            }

                            @Override
                            public void onFailure() {
                                savePetAndClose(newPet);
                            }
                        });
                    } else {
                        savePetAndClose(newPet);
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(NewPetActivity.this, "Pet name can't be empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void savePetAndClose(Pet newPet) {
        NewPetVM.addNewPet(newPet);
        setResult(RESULT_OK);
        progressBar.setVisibility(View.GONE);
        Toast.makeText(NewPetActivity.this, "Pet added successfully", Toast.LENGTH_LONG).show();
        finish();
    }


    private void addImage(int add_image_code) {
        if (add_image_code == ADD_IMAGE_CAMERA) {
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePhotoIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePhotoIntent, REQUEST_IMAGE_CAPUTRE);
            }
        } else if (add_image_code == ADD_IMAGE_GALLERY) {
            Intent getFromGalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(getFromGalleryIntent, ADD_IMAGE_GALLERY);
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
        } else if (requestCode == ADD_IMAGE_GALLERY) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Uri selectedImage = data.getData();
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    petImage.setImageBitmap(imageBitmap);

                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}