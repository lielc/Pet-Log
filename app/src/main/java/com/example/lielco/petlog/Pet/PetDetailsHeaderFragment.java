package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lielco.petlog.R;

import java.util.List;

public class PetDetailsHeaderFragment extends Fragment {
    private static final String PET_ID = "PetId";
    private onFragmentInteractionListener mListener;
    private static String petId;
    private PetDetailsHeaderFragmentViewModel petDetailsVM;
    private Pet displayedPet;
    ImageView petImage;
    TextView petName;
    public PetDetailsHeaderFragment() {}

    public static PetDetailsHeaderFragment newInstance(String petId) {
        PetDetailsHeaderFragment fragment = new PetDetailsHeaderFragment();
        Bundle args = new Bundle();
        args.putString(PET_ID, petId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onFragmentInteractionListener) {
            mListener = (onFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        Log.d("TAG","onAttach");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PetDetailsHeaderFragmentViewModel.Factory factory = new PetDetailsHeaderFragmentViewModel.Factory(petId);

        petDetailsVM = ViewModelProviders.of(this,factory).get(PetDetailsHeaderFragmentViewModel.class);
        //displayedPet = petDetailsVM.getPet();
        petDetailsVM.getPet().observe(this, new Observer<Pet>() {
            @Override
            public void onChanged(@Nullable Pet pet) {
                displayedPet = pet;
                petImage.setImageResource(Integer.parseInt(displayedPet.petImageUrl));
                petName.setText(displayedPet.petName);
            }
        });

        Log.d("TAG","onActCreate");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            petId = getArguments().getString(PET_ID);
        }
        Log.d("TAG","onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("TAG","onCrateView");
        return inflater.inflate(R.layout.fragment_pet_details_header, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        petImage = view.findViewById(R.id.details_pet_image);
        petName = view.findViewById(R.id.details_pet_name);

    }

    public interface onFragmentInteractionListener {}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void changeInfoVisibility(boolean isVisible){
        if (isVisible) {
            petName.setVisibility(View.VISIBLE);
        }
        else {
            petName.setVisibility(View.INVISIBLE);
        }
    }
}
