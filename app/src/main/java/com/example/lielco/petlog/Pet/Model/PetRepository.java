package com.example.lielco.petlog.Pet.Model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;
import android.webkit.URLUtil;

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

    public void savePetImage (final Context context, final Bitmap imageBitmap, String userId, final ImageCallback callback) {
        PetFirebase.savePetImage(imageBitmap, userId, new PetFirebase.ImageCallback() {
            @Override
            public void onComplete(Object data) {
                String filename = URLUtil.guessFileName(data.toString(),null,null);
                PetLocal.getInstance().saveImageToFile(context,imageBitmap,filename);
                callback.onComplete(data.toString());
            }

            @Override
            public void onFailure() {
                callback.onFailure();
            }
        });
    }

    public void getPetImage(final String imageUrl, final Context context, final GetImageCallback callback) {
        String fileName = URLUtil.guessFileName(imageUrl, null, null);
        final Bitmap imageBitmap = PetLocal.getInstance().loadImageFromFile(fileName);
        if (imageBitmap != null) {
            Log.d("TAG","Got pet image from local storage");
            callback.onSuccess(imageBitmap);
        } else {
            PetFirebase.getPetImage(imageUrl, new PetFirebase.ImageCallback() {
                @Override
                public void onComplete(Object data) {
                    String fileName = URLUtil.guessFileName(imageUrl, null, null);
                    Log.d("TAG","getImage from FB success " + fileName);
                    PetLocal.getInstance().saveImageToFile(context, (Bitmap) data,fileName);
                    callback.onSuccess((Bitmap) data);
                }

                @Override
                public void onFailure() {
                    Log.d("TAG","Failed getting pet image from FB");
                    callback.onFailure();
                }
            });
        }
    }

    public void updatePet(Pet pet, final VoidCallback callback){
        PetFirebase.updatePet(pet, new PetFirebase.VoidCallback() {
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

    public void deletePet(String petId, final VoidCallback callback){
        PetFirebase.deletePet(petId, new PetFirebase.VoidCallback() {
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

    public interface ImageCallback {
        void onComplete(String url);
        void onFailure();
    }

    public interface GetImageCallback {
        void onSuccess(Bitmap image);
        void onFailure();
    }

    public interface VoidCallback    {
        void onComplete();
        void onFailure();
    }
}
