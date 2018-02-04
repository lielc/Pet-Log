package com.example.lielco.petlog.Pet.Model;

import android.telecom.Call;
import android.util.Log;

import com.example.lielco.petlog.Pet.Pet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Liel on 03/02/2018.
 */

public class PetFirebase {
    public PetFirebase() {
    }

    public interface Callback<T> {
        void onComplete(T data);
    }

    public static void getAllPets(final Callback<List<Pet>> callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("pets");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Pet> petList = new LinkedList<Pet>();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    Pet currPet = snap.getValue(Pet.class);
                    petList.add(currPet);
                }
                callback.onComplete(petList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onComplete(null);
            }
        });
    }

    public static void getPetById(String petId, final Callback<Pet> callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("pets");
        dbRef.child(petId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Pet pet = dataSnapshot.getValue(Pet.class);
                callback.onComplete(pet);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG","DB ERROR: " + databaseError.getMessage());
            }
        });
    }

    public static void addNewPet(Pet newPet){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference("pets");
        String petId = dbRef.push().getKey();
        newPet.setPetId(petId);
        dbRef.child(petId).setValue(newPet.toHashMap());
    }
}
