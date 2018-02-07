package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lielco.petlog.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PetInfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PetInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
//TODO: BUG: when turning screen, nav fragment returns and overlaps info fragment. Nav fragment should stay hidden.

public class PetInfoFragment extends Fragment {
    private static final String PET_ID = "PetId";
    private OnFragmentInteractionListener mListener;
    private static String petId;
    PetDetailsViewModel petInfoVM;
    static Pet displayedPet;

    // view components
    TextView tvPetName;

    public PetInfoFragment() {}


    public static PetInfoFragment newInstance(String petId) {
        PetInfoFragment fragment = new PetInfoFragment();
        Bundle args = new Bundle();
        args.putString(PET_ID, petId);
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_pet_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        tvPetName = view.findViewById(R.id.info_pet_name_field);
        tvPetName.setText("");
    }

        @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onActivityCreated(savedInstanceState);
        PetDetailsViewModel.Factory factory = new PetDetailsViewModel.Factory(petId);

        petInfoVM = ViewModelProviders.of(this,factory).get(PetDetailsViewModel.class);
        petInfoVM.getPetObservable().observe(this, new Observer<Pet>() {
            @Override
            public void onChanged(@Nullable Pet pet) {
                displayedPet = pet;
                tvPetName.setText(displayedPet.petName);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {}
}
