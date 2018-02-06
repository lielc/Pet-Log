package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import android.telecom.Call;

import com.example.lielco.petlog.Pet.Model.PetRepository;
import com.example.lielco.petlog.User.Model.UserRepository;

/**
 * Created by Liel on 06/02/2018.
 */

public class PetDetailsViewModel extends ViewModel{
    private static Pet displayedPet = new Pet();

    public PetDetailsViewModel(String petId) {
        displayedPet = PetRepository.getInstance().getPetById(petId).getValue();
    }

    public static Pet getPet() {
        return displayedPet;
    }

    public void getUserIdByEmail (String userEmail, final ResultsCallback callback) {
        UserRepository.getInstance().getUserIdByMail(userEmail, new UserRepository.ResultsCallback() {
            @Override
            public void onSuccess(Object data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    public void addPetPermissions(String userId, String petId, final Callback callback){
        PetRepository.getInstance().addPetPermissions(userId, petId, new PetRepository.VoidCallback() {
            @Override
            public void onComplete() {
                callback.onSuccess();
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    public interface ResultsCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }

    public interface Callback<T> {
        void onSuccess();
        void onFailure();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        private final String petId;

        public Factory(String petId) { this.petId = petId; }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new PetDetailsViewModel(petId);
        }
    }
}
