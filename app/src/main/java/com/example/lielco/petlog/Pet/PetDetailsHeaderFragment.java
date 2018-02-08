package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
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

import org.w3c.dom.Text;

import java.util.List;

public class PetDetailsHeaderFragment extends Fragment {
    private static final String PET_ID = "petId";
    private onFragmentInteractionListener mListener;
    private static String petId;
    private PetDetailsViewModel petDetailsVM;
    private Pet displayedPet;
    ImageView petImage;
    TextView petName;
    TextView petAge;
    TextView petBreed;
    TextView petType;
    ImageView petGender;
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
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            petId = getArguments().getString(PET_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_details_header, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        petImage = view.findViewById(R.id.details_pet_image);
        petName = view.findViewById(R.id.details_pet_name);
        petAge = view.findViewById(R.id.details_pet_age);
        petBreed = view.findViewById(R.id.details_pet_breed);
        petType = view.findViewById(R.id.details_pet_type);
        petGender = view.findViewById(R.id.details_pet_sex);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PetDetailsViewModel.Factory factory = new PetDetailsViewModel.Factory(petId);

        petDetailsVM = ViewModelProviders.of(this,factory).get(PetDetailsViewModel.class);
        petDetailsVM.getPetObservable().observe(this, new Observer<Pet>() {
            @Override
            public void onChanged(@Nullable Pet pet) {
                displayedPet = pet;
                petName.setText(displayedPet.getPetName());
                if (displayedPet.getPetBreed() != null) {
                    petBreed.setText(displayedPet.getPetBreed());
                }
                if (displayedPet.getPetType() != null) {
                    petType.setText(displayedPet.getPetType());
                }
                if (displayedPet.getPetGender() != null) {
                    petGender.setVisibility(View.VISIBLE);
                    if (displayedPet.getPetGender().equals("male")) {
                        petGender.setImageResource(R.drawable.dog_001);
                    } else {
                        petGender.setImageResource(R.drawable.cat_001);
                    }
                }
                else {
                    petGender.setVisibility(View.GONE);
                }

                if (displayedPet.getPetImageUrl() != null
                    && !(displayedPet.getPetImageUrl().isEmpty())
                    && !(displayedPet.getPetImageUrl().equals(""))){

                petDetailsVM.getPetImage(displayedPet.getPetImageUrl(), getContext(), new PetDetailsViewModel.ResultsCallback() {
                    @Override
                    public void onSuccess(Object data) {
                        petImage.setImageBitmap((Bitmap) data);
                    }

                    @Override
                    public void onFailure(String error) {}
                });
                }

                Log.d("TAG","displayed pet has changed");

            }
        });
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
