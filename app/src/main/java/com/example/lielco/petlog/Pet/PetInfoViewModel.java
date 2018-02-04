package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.util.Log;

import com.example.lielco.petlog.Pet.Model.PetRepository;

/**
 * Created by Liel on 01/02/2018.
 */

public class PetInfoViewModel extends ViewModel{
    private static LiveData<Pet> pet;

    public PetInfoViewModel(String petId) {
        pet = PetRepository.getInstance().getPetById(petId);
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
            return (T) new PetInfoViewModel(petId);
        }
    }
}
