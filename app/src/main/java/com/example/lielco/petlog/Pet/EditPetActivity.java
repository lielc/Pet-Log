package com.example.lielco.petlog.Pet;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

public class EditPetActivity extends AppCompatActivity {
    private final String PET_ID = "petId";
    private static String petId;
    static final int REQUEST_IMAGE_CAPUTRE = 1;
    Pet petToEdit = new Pet();
    ImageView petImage;
    Bitmap imageBitmap;
    ProgressBar progressBar;
    static EditPetViewModel editPetVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet);

        Intent intent = getIntent();
        petId = intent.getExtras().getString(PET_ID);

        EditPetViewModel.Factory factory = new EditPetViewModel.Factory(petId);
        editPetVM = ViewModelProviders.of(this,factory).get(EditPetViewModel.class);

        final EditText etPetName = findViewById(R.id.edit_pet_name_field);
        progressBar = findViewById(R.id.edit_pet_pb);
        petImage = findViewById(R.id.edit_pet_image);
        Button confirmButton = findViewById(R.id.edit_pet_confirm_btn);
        Button cancelButton = findViewById(R.id.edit_pet_cancel_btn);

        editPetVM.getPetObservable().observe(this, new Observer<Pet>() {
            @Override
            public void onChanged(@Nullable Pet pet) {
                petToEdit = pet;
                etPetName.setText(pet.getPetName());
                petImage.setTag(petToEdit.getPetImageUrl());
                if (pet.getPetImageUrl() != null
                        && !(pet.getPetImageUrl().isEmpty())
                        && !(pet.getPetImageUrl().equals(""))) {
                    editPetVM.getPetImage(pet.getPetImageUrl(), getApplicationContext(), new EditPetViewModel.ResultsCallback() {
                        @Override
                        public void onSuccess(Object data) {
                            petImage.setImageBitmap((Bitmap) data);
                        }

                        @Override
                        public void onFailure(String error) {}
                    });
                }
            }
        });

        petImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImage();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final Pet editedPet = new Pet();
                editedPet.setPetId(petToEdit.getPetId());
                editedPet.setPetName(etPetName.getText().toString());

                // Checking if The pet hasn't changed
                if (editedPet.equals(petToEdit) && petImage.getTag().equals(petToEdit.getPetImageUrl())) {
                    endActivity(Activity.RESULT_CANCELED);
                } else {
                    // Check if petImage have changed
                    if (imageBitmap != null) {
                        editPetVM.saveImage(getApplicationContext(),
                                imageBitmap, FirebaseAuth.getInstance().getUid(),
                                new EditPetViewModel.ResultsCallback() {
                            @Override
                            public void onSuccess(Object data) {
                                editedPet.setPetImageUrl(data.toString());
                                updatePet(editedPet);
                            }

                            @Override
                            public void onFailure(String error) {
                                Toast.makeText(EditPetActivity.this, "Problem proccessing image.", Toast.LENGTH_LONG).show();
                                Toast.makeText(EditPetActivity.this, "Could not update pet.", Toast.LENGTH_LONG).show();
                                endActivity(Activity.RESULT_CANCELED);
                            }
                        });

                    }
                    // Image was not changed
                    else {
                        updatePet(editedPet);

                    }
                }
            }
        });
    }

    private void updatePet(Pet editedPet) {
        editPetVM.editPet(editedPet, new EditPetViewModel.VoidCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(EditPetActivity.this, "Pet updated successfully", Toast.LENGTH_LONG).show();
                endActivity(Activity.RESULT_OK);
            }
            @Override
            public void onFailure() {
                Toast.makeText(EditPetActivity.this, "Could not update pet", Toast.LENGTH_LONG).show();
                endActivity(Activity.RESULT_CANCELED);
            }
        });
    }

    private void endActivity(int activityResult){
        setResult(activityResult);
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
                petImage.setTag("");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
