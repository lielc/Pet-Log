package com.example.lielco.petlog.Pet.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.telecom.Call;

import com.example.lielco.petlog.Pet.Pet;
import com.example.lielco.petlog.R;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Liel on 27/01/2018.
 */

public class PetRepository {
    //public static final PetRepository instance = new PetRepository();
    public static PetRepository petRepo;
    List<Pet> petList = new LinkedList<>();
    private Integer[] mThumbIds = {
            R.drawable.cat_001,
            R.drawable.dog_001,
            R.drawable.dog_002,
            R.drawable.rabbit_001};
    MutableLiveData<List<Pet>> petListLD;

    public PetRepository() {
        //Testing initialization data
//        for(int i=0;i<mThumbIds.length;i++) {
//            Pet pet = new Pet("Pet"+i,String.valueOf(mThumbIds[i]));
//            addNewPet(pet);
//       }
    }

    public synchronized static PetRepository getInstance() {
        if (petRepo == null) {
            petRepo = new PetRepository();
        }
        return petRepo;
    }

    public MutableLiveData<List<Pet>> getAllPets() {
        synchronized (this){
            if (petListLD == null) {
                petListLD = new MutableLiveData<List<Pet>>();
                PetFirebase.getAllPets(new PetFirebase.Callback<List<Pet>>() {
                    @Override
                    public void onComplete(List<Pet> data) {
                        if (data != null) {
                            petListLD.setValue(data);
                        }
                    }
                });
            }
        }
        return petListLD;
    }

//    public void getPetById (String petId, final Callback<MutableLiveData<Pet>> callback) {
//        //return petList.get(Integer.parseInt(petId));
////        MutableLiveData<Pet> data = new MutableLiveData<Pet>();
////        data.setValue(petList.get(Integer.parseInt(petId)));
////        return data;
//        Pet pet;
//        PetFirebase.getPetById(petId, new PetFirebase.Callback<Pet>(){
//            @Override
//            public void onComplete(Pet data) {
//                MutableLiveData<Pet> pet = new MutableLiveData<>();
//                pet.setValue(data);
//                callback.onComplete(pet);
//            }
//        });
//    }

    public LiveData<Pet> getPetById(String petId) {
        final MutableLiveData<Pet> pet = new MutableLiveData<>();

        PetFirebase.getPetById(petId, new PetFirebase.Callback<Pet>() {
            @Override
            public void onComplete(Pet data) {
                pet.setValue(data);
            }
        });

        return pet;
    }

    public void addNewPet(Pet newPet) {
//        Pet pet = new Pet(String.valueOf(petList.size()),newPet.getPetName(),String.valueOf(mThumbIds[new Random().nextInt(mThumbIds.length)]));
//        petList.add(pet);
//        petListLD.setValue(petList);
        PetFirebase.addNewPet(newPet);
    }

    public interface Callback<T> {
        void onComplete(LiveData<Pet> pet);
        void onCancel();
    }
}
