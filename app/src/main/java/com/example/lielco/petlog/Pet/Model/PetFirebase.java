package com.example.lielco.petlog.Pet.Model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;

import com.example.lielco.petlog.Pet.Pet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
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

    public interface ImageCallback<T> {
        void onComplete(T data);
        void onFailure();
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

        dbRef.child("permissions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            if (petList.size() == userPetsIds.size()){
                                Log.d("TAG", "sending back pet list");
                                callback.onComplete(petList);
                            }
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
        dbRef.child(petId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Pet.class) != null) {
                    Pet pet = dataSnapshot.getValue(Pet.class);
                    Log.d("TAG","Received pet from DB " + pet.getPetName());
                    callback.onComplete(pet);
                }
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
    }

    public static void savePetImage(final Bitmap imageBitmap, String userId, final ImageCallback callback) {
        // Get a unique identifier for the image name
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        String uniqueKey = dbRef.push().getKey();
        StorageReference petImagesRef = FirebaseStorage.getInstance().getReference("images").child(userId).child("image_"+uniqueKey+".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();

        UploadTask upload = petImagesRef.putBytes(data);
        upload.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG","Error uploading image to FB storage. message: " + e.getMessage());
                callback.onFailure();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri imageUrl = taskSnapshot.getDownloadUrl();
                Log.d("TAG","Successfully uploaded image to Firebase Storage");
                callback.onComplete(imageUrl.toString());
            }
        });
    }

    public static void getPetImage(String imageUrl, final ImageCallback callback) {
        final long ONE_MEGABYTE = 1024 * 1024;
        StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
        ref.getBytes(3*ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                callback.onComplete(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "getPetImage: FAILED. exception: " + e.getMessage());
                callback.onFailure();
            }
        });
    }

    public static void updatePet(Pet pet, final VoidCallback callback){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("pets").child(pet.getPetId()).setValue(pet.toHashMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onComplete();
                }
                else {
                    Log.d("TAG", "updatePet failed: " + task.getException().getMessage());
                    callback.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public static void deletePet (final String petId, final VoidCallback callback) {
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        //dbRef.child("permissions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(petId).removeValue();

        // Checking if anyone else has permission to this pet
        dbRef.child("permissions").orderByChild(petId).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("TAG","deletePet: permissions deleted");
                int childrenSum = 0;
                for (DataSnapshot snap : dataSnapshot.getChildren()){
                    childrenSum++;
                }

                // If there's only one child, the user is the only owner
                if (childrenSum == 1) {
                    deletePetFromDb(petId, new VoidCallback() {
                        @Override
                        public void onComplete() {
                            dbRef.child("permissions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(petId).removeValue();
                            Log.d("TAG","deleted pet from DB");
                            callback.onComplete();
                        }

                        @Override
                        public void onFailure(String error) {
                            callback.onFailure(error);
                        }
                    });
                }
                // or else there're other users owning the pet, remove permissions only
                else {
                    dbRef.child("permissions").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(petId).removeValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private static void deletePetFromDb (String petId, final VoidCallback callback) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.child("pets").child(petId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onComplete();
                }
                else {
                    Log.d("TAG","error delting pet from DB - " + task.getException().getMessage());
                    callback.onFailure(task.getException().getMessage());
                }
            }
        });
    }
}
