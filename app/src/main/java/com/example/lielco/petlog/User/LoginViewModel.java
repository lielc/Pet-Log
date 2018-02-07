package com.example.lielco.petlog.User;

import android.arch.lifecycle.ViewModel;
import android.telecom.Call;
import android.util.Log;

import com.example.lielco.petlog.User.Model.UserRepository;

/**
 * Created by Liel on 07/02/2018.
 */

public class LoginViewModel extends ViewModel {
    public void addNewUser(User user, final Callback callback) {
        UserRepository.getInstance().addNewUser(user, new UserRepository.Callback() {
            @Override
            public void onSuccess() {
                callback.onSuccess();
            }

            @Override
            public void onFailure(String error) {
                Log.d("TAG","Error inserting new user into DB. error: " + error );
                callback.onFailure();
            }
        });
    }

    public void getUserIdByMail (String userEmail, final getUserIdCallback callback)
    {
        UserRepository.getInstance().getUserIdByMail(userEmail, new UserRepository.ResultsCallback() {
            @Override
            public void onSuccess(Object data) {
                callback.onSuccess(data.toString());
            }

            @Override
            public void onFailure(String error) {
                callback.onFailure();
            }
        });
    }

    public interface Callback {
        void onSuccess();
        void onFailure();
    }

    public interface getUserIdCallback {
        void onSuccess(String userId);
        void onFailure();
    }
}
