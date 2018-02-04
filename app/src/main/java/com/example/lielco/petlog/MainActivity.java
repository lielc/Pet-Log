package com.example.lielco.petlog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lielco.petlog.Pet.Model.PetFirebase;
import com.example.lielco.petlog.Pet.Pet;
import com.example.lielco.petlog.Pet.PetDetailsActivity;
import com.example.lielco.petlog.Pet.PetGridFragment;

public class MainActivity extends AppCompatActivity implements PetGridFragment.OnFragmentInteractionListener{
    PetGridFragment petGridFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        petGridFrag = (PetGridFragment) getSupportFragmentManager().findFragmentById(R.id.main_pet_grid_frag);
    }

    @Override
    public void onPetSelected(String petId) {
        Log.d("TAG","pet selected: " + petId);
        Intent intent = new Intent(getApplicationContext(),PetDetailsActivity.class);
        intent.putExtra("petId",petId);
        startActivity(intent);
    }
}
