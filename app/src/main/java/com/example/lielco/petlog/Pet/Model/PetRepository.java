package com.example.lielco.petlog.Pet.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.lielco.petlog.Pet.Pet;
import com.example.lielco.petlog.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Liel on 27/01/2018.
 */

public class PetRepository {
    public static final PetRepository instance = new PetRepository();
    List<Pet> petList = new LinkedList<>();
    private Integer[] mThumbIds = {
            R.drawable.cat_001,
            R.drawable.dog_001,
            R.drawable.dog_002,
            R.drawable.rabbit_001};
    MutableLiveData<List<Pet>> petListLD;

    public PetRepository() {
        //Testing initialization data
        for(int i=0;i<mThumbIds.length;i++) {
            Pet pet = new Pet(String.valueOf(i),"Pet"+i,String.valueOf(mThumbIds[i]));
            petList.add(pet);
        }
    }

    public LiveData<List<Pet>> getAllPets() {
        synchronized (this){
            if (petListLD == null) {
                petListLD = new MutableLiveData<List<Pet>>();
                // Until I attach to FireBase
                petListLD.setValue(petList);
            }
        }
        return petListLD;
    }

    public LiveData<Pet> getPetById (String petId) {
        //return petList.get(Integer.parseInt(petId));
        MutableLiveData<Pet> data = new MutableLiveData<Pet>();
        data.setValue(petList.get(Integer.parseInt(petId)));
        return data;
    }
}
