package com.example.lielco.petlog.Pet.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;

import com.example.lielco.petlog.Pet.Pet;
import com.example.lielco.petlog.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Liel on 27/01/2018.
 */

public class PetRepository {
    //public static final PetRepository instance = new PetRepository();
    public static PetRepository petRepo;
    FirebaseAuth fbAuth;

    private Integer[] mThumbIds = {
            R.drawable.cat_001,
            R.drawable.dog_001,
            R.drawable.dog_002,
            R.drawable.rabbit_001};

    MutableLiveData<List<Pet>> petListLD;

    public PetRepository() {
        fbAuth = FirebaseAuth.getInstance();
        fbAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                petListLD.setValue(new LinkedList<Pet>());
                Log.d("TAG","user auth status changed.");
            }
        });
    }

    public synchronized static PetRepository getInstance() {
        if (petRepo == null) {
            petRepo = new PetRepository();
        }
        return petRepo;
    }

    public MutableLiveData<List<Pet>> getAllPets() {
        synchronized (this){
            //Log.d("TAG","getAllPets: pet list is: " + petListLD.getValue().toString());
            if (petListLD == null) {
                petListLD = new MutableLiveData<List<Pet>>();
            }

            PetFirebase.getAllPets(new PetFirebase.Callback<List<Pet>>() {
                @Override
                public void onComplete(List<Pet> data) {
                    if (data != null) {
                        petListLD.setValue(data);
                    }
                }
            });
        }
        return petListLD;
    }

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
        PetFirebase.addNewPet(newPet);
    }

    public void addPetPermissions(String userId, String petId, final VoidCallback callback){
        PetFirebase.addPetPermissions(userId, petId, new PetFirebase.VoidCallback() {
            @Override
            public void onComplete() {
                callback.onComplete();
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure();
            }
        });
    }

    public interface Callback<T> {
        void onComplete(LiveData<Pet> pet);
        void onCancel();
    }

    public interface VoidCallback    {
        void onComplete();
        void onFailure();
    }
}
