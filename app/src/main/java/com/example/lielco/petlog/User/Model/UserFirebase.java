package com.example.lielco.petlog.User.Model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.lielco.petlog.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Liel on 06/02/2018.
 */

public class UserFirebase {
    public UserFirebase() {}

    public interface ResultsCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }

    public interface Callback {
        void onSuccess();
        void onFailure(String error);
    }

    public static void addNewUser(User user, final Callback callback){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.child(user.getUserId()).setValue(user.toHashMap()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    callback.onSuccess();
                }
                else {
                    callback.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public static void getIdByEmail (String userEmail, final ResultsCallback callback) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("users");
        dbRef.orderByChild("userEmail").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = new User();
                for ( DataSnapshot singleSnap : dataSnapshot.getChildren())
                {
                    user = singleSnap.getValue(User.class);
                }

                Log.d("TAG","Recieved userId " + user.getUserId());
                callback.onSuccess(user.getUserId());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG","getEmailById: DB error: " + databaseError.getMessage());
                callback.onFailure("DB error");
            }
        });
    }
}
