package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lielco.petlog.R;

import java.util.List;

public class PetDetailsHeaderFragment extends Fragment {
    private static final String PET_POS = "PetPos";
    private onFragmentInteractionListener mListener;
    private int petPos;
    private PetDetailsHeaderFragmentViewModel petDetailsVM;
    private Pet displayedPet;

    public PetDetailsHeaderFragment() {}

    public static PetDetailsHeaderFragment newInstance(int petPos) {
        PetDetailsHeaderFragment fragment = new PetDetailsHeaderFragment();
        Bundle args = new Bundle();
        args.putInt(PET_POS, petPos);
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

        PetDetailsHeaderFragmentViewModel.Factory factory = new PetDetailsHeaderFragmentViewModel.Factory(getCurrentPetId());

        petDetailsVM = ViewModelProviders.of(this,factory).get(PetDetailsHeaderFragmentViewModel.class);
        displayedPet = petDetailsVM.getPet();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            petPos = getArguments().getInt(PET_POS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_details_header, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ImageView petImage = view.findViewById(R.id.details_pet_image);
        TextView petName = view.findViewById(R.id.details_pet_name);

        petImage.setImageResource(Integer.parseInt(displayedPet.petImageUrl));
        petName.setText(displayedPet.petName);
    }

    public String getCurrentPetId () {
        return String.valueOf(getArguments().getInt(PET_POS));
    }

    public interface onFragmentInteractionListener {}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
