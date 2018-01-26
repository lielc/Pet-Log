package com.example.lielco.petlog.Pet;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lielco.petlog.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PetDetailsNavFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PetDetailsNavFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PetDetailsNavFragment extends Fragment {
    private static final String PET_POS = "PetPos";
    private onFragmentInteractionListener mListener;
    private int petPos;

    public PetDetailsNavFragment() {}

    public static PetDetailsNavFragment newInstance(int petPos) {
        PetDetailsNavFragment fragment = new PetDetailsNavFragment();
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
        return inflater.inflate(R.layout.fragment_pet_details_nav, container,false);
    }

    public interface onFragmentInteractionListener {}

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
