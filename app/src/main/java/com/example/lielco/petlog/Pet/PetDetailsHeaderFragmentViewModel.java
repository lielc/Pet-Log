package com.example.lielco.petlog.Pet;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.lielco.petlog.Pet.Model.PetRepository;

import java.util.List;

/**
 * Created by Liel on 28/01/2018.
 */

public class PetDetailsHeaderFragmentViewModel extends ViewModel {
    private static LiveData<Pet> pet;

    public PetDetailsHeaderFragmentViewModel(String petId) {
        this.pet = PetRepository.instance.getPetById(petId);
    }

    public LiveData<Pet> getPet() {
        return pet;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        private final String petId;

        public Factory(String petId) {
            this.petId = petId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PetDetailsHeaderFragmentViewModel(petId);
        }
    }
}
