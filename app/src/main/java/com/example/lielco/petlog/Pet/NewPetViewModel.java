package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Bitmap;

import com.example.lielco.petlog.Pet.Model.PetRepository;

/**
 * Created by Liel on 03/02/2018.
 */

public class NewPetViewModel extends ViewModel{
    public void addNewPet(Pet newPet){
        PetRepository.getInstance().addNewPet(newPet);
    }

    public void saveImage(Context context, Bitmap imageBitmap, String userId, final Callback callback) {
        PetRepository.getInstance().savePetImage(context, imageBitmap, userId, new PetRepository.ImageCallback() {
            @Override
            public void onComplete(String url) {
                callback.onSuccess(url);
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    public void refreshList(){
        PetRepository.getInstance().getAllPets();
    }

    public interface Callback {
        void onSuccess(String imageUrl);
        void onFailure();
    }
}
