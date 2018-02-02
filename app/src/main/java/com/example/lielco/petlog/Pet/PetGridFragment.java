package com.example.lielco.petlog.Pet;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.lielco.petlog.R;

import java.util.LinkedList;
import java.util.List;


public class PetGridFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private PetGridFragmentViewModel petGridFragmentVM;
    List<Pet> petList = new LinkedList<>();
    CustomGVAdapter petGvAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        petGridFragmentVM = ViewModelProviders.of(this).get(PetGridFragmentViewModel.class);
        petGridFragmentVM.getAllPets().observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(@Nullable List<Pet> pets) {
                petList = pets;
                if (petGvAdapter != null) {
                    petGvAdapter.notifyDataSetChanged();
                };
            }
        });
        //petList = petGridFragmentVM.getAllPets();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_grid, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        GridView petGv = view.findViewById(R.id.pet_grid);
        petGvAdapter = new CustomGVAdapter();
        petGv.setAdapter(petGvAdapter);
        petGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mListener != null) {
                    onPetSelected(position);
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onPetSelected(int position);
    }

    public void onPetSelected(int position){
        mListener.onPetSelected(position);
    }

    // The custom GridView adapter
    class CustomGVAdapter extends BaseAdapter {
//        private Integer[] mThumbIds = {
//                R.drawable.cat_001,
//                R.drawable.dog_001,
//                R.drawable.dog_002,
//                R.drawable.rabbit_001};

        LayoutInflater inflater = getActivity().getLayoutInflater();

        @Override
        public int getCount() { return petList.size(); }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.pet_grid_single,null);
            }

            ImageView petImage = view.findViewById(R.id.pet_grid_image);
            petImage.setImageResource(Integer.parseInt(petList.get(position).petImageUrl));

            return view;
        }
    }
}


