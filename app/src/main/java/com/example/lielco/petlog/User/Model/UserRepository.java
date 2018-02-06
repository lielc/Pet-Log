package com.example.lielco.petlog.User.Model;

import android.arch.lifecycle.LiveData;
import android.telecom.Call;

import com.example.lielco.petlog.User.User;
import com.google.android.gms.common.UserRecoverableException;

/**
 * Created by Liel on 06/02/2018.
 */

public class UserRepository {
    public static UserRepository userRepo;

    public UserRepository(){}

    public synchronized static UserRepository getInstance() {
        if (userRepo == null) {
            userRepo = new UserRepository();
        }

        return userRepo;
    }

    public void addNewUser (User user, final Callback callback) {
        UserFirebase.addNewUser(user, new UserFirebase.Callback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure(error);
            }
        });
    }

    public void getUserIdByMail(String userEmail, final ResultsCallback callback){
        UserFirebase.getIdByEmail(userEmail, new UserFirebase.ResultsCallback() {
            @Override
            public void onSuccess(Object data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailure(String error) {
                onFailure(error);
            }
        });
    }

    public interface Callback<T> {
        void onSuccess();
        void onFailure(String error);
    }

    public interface ResultsCallback<T> {
        void onSuccess(T data);
        void onFailure(String error);
    }
}
