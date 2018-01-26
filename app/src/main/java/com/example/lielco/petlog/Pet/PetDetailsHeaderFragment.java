package com.example.lielco.petlog.Pet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lielco.petlog.R;

import java.util.List;

public class PetDetailsHeaderFragment extends Fragment {
    private static final String PET_POS = "PetPos";
    private onFragmentInteractionListener mListener;
    private int petPos;

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

    public interface onFragmentInteractionListener {}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
