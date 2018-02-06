package com.example.lielco.petlog.Pet.Model;

import android.provider.ContactsContract;
import android.telecom.Call;
import android.util.Log;

import com.example.lielco.petlog.Pet.Pet;
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

    public static void getAllPets(final Callback<List<Pet>> callback) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference dbRef = database.getReference("");
        final ArrayList<String> userPetsIds = new ArrayList<>();
        final List<Pet> petList = new LinkedList<Pet>();

        dbRef.child("permissions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                    userPetsIds.add(snap.getKey());
                    Log.d("TAG","userPets id: " + snap.getKey());
                }
                Log.d("TAG","userPets has " + userPetsIds.size() + " pets");
                for (int i = 0; i<userPetsIds.size();i++) {
                    dbRef.child("pets").child(userPetsIds.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            petList.add(dataSnapshot.getValue(Pet.class));
                            Log.d("TAG","added pet to list: " + dataSnapshot.getValue(Pet.class).getPetName());
                            Log.d("TAG","sending back pet list");
                            callback.onComplete(petList);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("TAG","Error getting datasbapshot: " +databaseError.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG","Error getting all pets. error: " + databaseError.getMessage());
                callback.onComplete(null);
            }
        });
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot snap : dataSnapshot.getChildren()) {
//                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("pets");
//                    dbRef.
//                    getPetById(snap.getKey(), new Callback<Pet>() {
//                        @Override
//                        public void onComplete(Pet data) {
//                            petList.add(data);
//                            Log.d("TAG","added pet to list " + data.getPetName());
//                            callback.onComplete(petList);
//                        }
//                    });
 //               }

//                Log.d("TAG","sending back pet list. size: " + petList.size());
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                callback.onComplete(null);
//            }
//        });
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
        }
