package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.lielco.petlog.Pet.Model.PetRepository;

import java.util.List;

/**
 * Created by Liel on 27/01/2018.
 */

public class PetGridFragmentViewModel extends ViewModel {
    private LiveData<List<Pet>> petList;

    public PetGridFragmentViewModel() {
        petList = PetRepository.getInstance().getAllPets();
    }

    public LiveData<List<Pet>> getAllPets() {
        return petList;
    }

//    public Pet getPetById (String petId) {
//        //return petList.get(Integer.parseInt(petId));
//    }
}
