package com.example.lielco.petlog.Pet.Model;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;

import com.example.lielco.petlog.Pet.Pet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    public interface VoidCallback{
        void onComplete();
        void onFailure(String error);
    }

    public static void getAllPets(final Callback<List<Pet>> callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("");
        final ArrayList<String> userPetsIds = new ArrayList<>();
        final List<Pet> petList = new LinkedList<Pet>();

        dbRef.child("permissions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userPetsIds.clear();
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    userPetsIds.add(snap.getKey());
                    Log.d("TAG", "userPets id: " + snap.getKey());
                }
                Log.d("TAG", "userPets has " + userPetsIds.size() + " pets");
                petList.clear();
                for (int i = 0; i < userPetsIds.size(); i++) {
                    dbRef.child("pets").child(userPetsIds.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            petList.add(dataSnapshot.getValue(Pet.class));
                            Log.d("TAG", "added pet to list: " + dataSnapshot.getValue(Pet.class).getPetName());
                            Log.d("TAG", "sending back pet list");
                            callback.onComplete(petList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("TAG", "Error getting datasbapshot: " + databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "Error getting all pets. error: " + databaseError.getMessage());
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
                Log.d("TAG","Received pet from DB " + pet.getPetName());
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
        DatabaseReference dbRef = database.getReference();
        String petId = dbRef.push().getKey();
        newPet.setPetId(petId);
        dbRef.child("pets").child(petId).setValue(newPet.toHashMap());
        dbRef.child("permissions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(petId).setValue(true);
    }

    public static void addPetPermissions(String userId, String petId, final VoidCallback callback) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("permissions");
        dbRef.child(userId).child(petId).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    callback.onComplete();
                }
                else {
                    Log.d("TAG","Failed putting permissions. " + task.getException().getMessage());
                    callback.onFailure(task.getException().getMessage());
                }
            }
        });
    };
}
