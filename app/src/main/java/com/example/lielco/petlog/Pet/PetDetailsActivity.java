package com.example.lielco.petlog.Pet;

import android.app.Fragment;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lielco.petlog.R;

public class PetDetailsActivity extends AppCompatActivity implements PetInfoFragment.OnFragmentInteractionListener,PetDetailsHeaderFragment.onFragmentInteractionListener,PetDetailsNavFragment.onFragmentInteractionListener{
    PetDetailsHeaderFragment headerFrag;
    PetDetailsNavFragment navFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_details);

        Intent intent = getIntent();
        int petPos = intent.getExtras().getInt("petPos");

        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        headerFrag = PetDetailsHeaderFragment.newInstance(String.valueOf(petPos));
        navFrag = PetDetailsNavFragment.newInstance(String.valueOf(petPos));

        tran.add(R.id.details_header_frag,headerFrag);
        tran.add(R.id.details_content_frag,navFrag);
        //tran.addToBackStack("1");
        tran.commit();
    }

    @Override
    public void showInfoFrag(String petId) {
        PetInfoFragment infoFragment = PetInfoFragment.newInstance(petId);
        //headerFrag.changeInfoVisibility(false);
        FragmentTransaction showInfoTran = getSupportFragmentManager().beginTransaction();
        showInfoTran.replace(R.id.details_content_frag,infoFragment);
        showInfoTran.addToBackStack("NavToInfo");
        showInfoTran.commit();
    }
}
