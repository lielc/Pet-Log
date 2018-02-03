package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.ViewModel;

import com.example.lielco.petlog.Pet.Model.PetRepository;

/**
 * Created by Liel on 03/02/2018.
 */

public class NewPetViewModel extends ViewModel{
    public void addNewPet(Pet newPet){
        PetRepository.instance.addNewPet(newPet);
    }
}
