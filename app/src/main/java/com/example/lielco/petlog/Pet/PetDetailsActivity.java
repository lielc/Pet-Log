package com.example.lielco.petlog.Pet;

import android.app.Fragment;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lielco.petlog.R;

public class PetDetailsActivity extends AppCompatActivity implements PetDetailsHeaderFragment.onFragmentInteractionListener,PetDetailsNavFragment.onFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        Intent intent = getIntent();
        int petPos = intent.getExtras().getInt("petPos");

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        PetDetailsHeaderFragment Frag1 = PetDetailsHeaderFragment.newInstance(petPos);
        PetDetailsNavFragment Frag2 = PetDetailsNavFragment.newInstance(petPos);

        tran.add(R.id.details_header_frag,Frag1);
        tran.add(R.id.details_content_frag,Frag2);
        tran.addToBackStack("tran21");
        tran.commit();
    }
}
