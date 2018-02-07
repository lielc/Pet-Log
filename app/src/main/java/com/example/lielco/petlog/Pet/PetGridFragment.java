package com.example.lielco.petlog.Pet;


import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.lielco.petlog.Pet.Model.PetRepository;
import com.example.lielco.petlog.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


public class PetGridFragment extends Fragment {
    private static final int NEW_PET_REQUEST_CODE = 0;
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
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_pet_grid, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        GridView petGv = view.findViewById(R.id.pet_grid);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            petGridFragmentVM = ViewModelProviders.of(this).get(PetGridFragmentViewModel.class);
            petGridFragmentVM.getAllPets().observe(this, new Observer<List<Pet>>() {
                @Override
                public void onChanged(@Nullable List<Pet> pets) {
                    petList = pets;
                    if (petGvAdapter != null) {
                        petGvAdapter.notifyDataSetChanged();
                        Log.d("TAG","notify DatasetChange for GVadapter");
                    }
                    ;
                }
            });
        }

        petGvAdapter = new CustomGVAdapter();
        petGv.setAdapter(petGvAdapter);
        petGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mListener != null) {
                    onPetSelected(petGvAdapter.getPetIdByPosition(position));
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
        void onPetSelected(String petId);
    }

    public void onPetSelected(String petId){
        mListener.onPetSelected(petId);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.pet_grid_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.grid_menu_add_pet:
                Intent intent = new Intent(getActivity(), NewPetActivity.class);
                startActivityForResult(intent, NEW_PET_REQUEST_CODE);
                break;
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PET_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("TAG","pet saved");
            }
        }
    }


    // The custom GridView adapter
    class CustomGVAdapter extends BaseAdapter {
        private HashMap<Integer,String> posIdMap = new HashMap<>();

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
            final Pet currentPet = petList.get(position);

            if (view == null) {
                view = inflater.inflate(R.layout.pet_grid_single,null);
            }

            for (int i=0; i < petList.size(); i++) {
                posIdMap.put(i,petList.get(i).getPetId());
            }

            final ImageView petImage = view.findViewById(R.id.pet_grid_image);
            petImage.setTag(currentPet.getPetImageUrl());
            if (currentPet.getPetImageUrl() != null
                    && !(currentPet.getPetImageUrl().isEmpty())
                    && !(currentPet.getPetImageUrl().equals(""))) {
                petGridFragmentVM.getPetImage(currentPet.getPetImageUrl(), getContext(), new PetGridFragmentViewModel.Callback() {
                    @Override
                    public void onSuccess(Bitmap image) {
                        if (petImage.getTag().toString().equals(currentPet.getPetImageUrl())) {
                            petImage.setImageBitmap(image);
                        }
                    }
                });
            }
            return view;
        }


        public String getPetIdByPosition(int position){
            return posIdMap.get(position);
        }
    }

}


