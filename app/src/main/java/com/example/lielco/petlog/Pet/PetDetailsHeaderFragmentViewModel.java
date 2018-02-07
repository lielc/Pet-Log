package com.example.lielco.petlog.Pet;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lielco.petlog.Pet.Model.PetRepository;

import java.util.List;

/**
 * Created by Liel on 28/01/2018.
 */

public class PetDetailsHeaderFragmentViewModel extends ViewModel {
    private static LiveData<Pet> pet = new MutableLiveData<>();

    public PetDetailsHeaderFragmentViewModel(String petId) {
        pet = PetRepository.getInstance().getPetById(petId);
    }

    public LiveData<Pet> getPet() {
        return pet;
    }

    public void getPetImage(String imageUrl, Context context, final Callback callback){
       PetRepository.getInstance().getPetImage(imageUrl, context, new PetRepository.GetImageCallback() {
           @Override
           public void onSuccess(Bitmap image) {
               callback.onSuccess(image);
           }

           @Override
           public void onFailure() {

           }
       });
    }

    public interface Callback{
        void onSuccess(Bitmap image);
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
