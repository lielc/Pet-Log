package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.ViewModel;

import com.example.lielco.petlog.Pet.Model.PetRepository;

import java.util.List;

/**
 * Created by Liel on 27/01/2018.
 */

public class PetGridFragmentViewModel extends ViewModel {
    private List<Pet> petList;

    public PetGridFragmentViewModel() {
        petList = PetRepository.instance.getAllPets();
    }

    public List<Pet> getAllPets() {
        return petList;
    }

    public Pet getPetById (String petId) { return petList.get(Integer.parseInt(petId)); }
}
