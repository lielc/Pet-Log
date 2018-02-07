package com.example.lielco.petlog.Pet;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.graphics.Bitmap;

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
}
