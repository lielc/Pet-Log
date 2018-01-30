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

    public PetRepository() {
        //Testing initialization data
        for(int i=0;i<mThumbIds.length;i++) {
            Pet pet = new Pet(String.valueOf(i),"Pet"+i,String.valueOf(mThumbIds[i]));
            petList.add(pet);
        }
    }

    public List<Pet> getAllPets() {
        return petList;
    }

    public Pet getPetById (String petId) {
        return petList.get(Integer.parseInt(petId));
    }
}
