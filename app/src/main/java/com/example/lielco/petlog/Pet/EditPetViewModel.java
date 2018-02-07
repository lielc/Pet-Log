package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.example.lielco.petlog.Pet.Model.PetRepository;

import javax.xml.transform.Result;


public class EditPetViewModel extends ViewModel {
    private static LiveData<Pet> displayedPetObservable = new MutableLiveData<>();

    public EditPetViewModel(String petId) {
        displayedPetObservable = PetRepository.getInstance().getPetById(petId);
    }

    public LiveData<Pet> getPetObservable() {
        return displayedPetObservable;
    }

    public void getPetImage(String imageUrl, Context context, final  ResultsCallback callback){
        PetRepository.getInstance().getPetImage(imageUrl, context, new PetRepository.GetImageCallback() {
            @Override
            public void onSuccess(Bitmap image) {
                callback.onSuccess(image);
            }

            @Override
            public void onFailure() {}
        });
    }

    public void editPet(Pet pet, final VoidCallback callback){
        PetRepository.getInstance().updatePet(pet, new PetRepository.VoidCallback() {
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

    public void saveImage(Context context, Bitmap imageBitmap, String userId, final ResultsCallback callback) {
        PetRepository.getInstance().savePetImage(context, imageBitmap, userId, new PetRepository.ImageCallback() {
            @Override
            public void onComplete(String url) {
                callback.onSuccess(url);
            }

            @Override
            public void onFailure() {
                callback.onFailure("");
            }
        });
    }

    public void refreshList(){
        PetRepository.getInstance().getAllPets();
    }

    public interface ResultsCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }

    public interface VoidCallback<T> {
        void onSuccess();
        void onFailure();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory{
        private final String petId;

        public Factory(String petId) { this.petId = petId; }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new EditPetViewModel(petId);
        }
    }
}
