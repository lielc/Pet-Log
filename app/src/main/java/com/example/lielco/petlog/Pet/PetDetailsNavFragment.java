package com.example.lielco.petlog.Pet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lielco.petlog.R;

public class PetDetailsNavFragment extends Fragment {
    private static final String PET_ID = "PetId";
    private onFragmentInteractionListener mListener;
    private static String petId = "";

    public PetDetailsNavFragment() {}

    public static PetDetailsNavFragment newInstance(String petId) {
        PetDetailsNavFragment fragment = new PetDetailsNavFragment();
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
        return inflater.inflate(R.layout.fragment_pet_details_nav, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Button btnPetInfo = view.findViewById(R.id.details_nav_info_btn);
        btnPetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.showInfoFrag(petId);
                }
            }
        });
    }

//    public String getCurrentPetId () {
//        return String.valueOf(getArguments().getInt(PET_POS));
//    }

    public interface onFragmentInteractionListener {
        void showInfoFrag(String petId);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
