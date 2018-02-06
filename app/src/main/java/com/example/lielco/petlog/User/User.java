package com.example.lielco.petlog.User;

import java.util.HashMap;

/**
 * Created by Liel on 06/02/2018.
 */

public class User {
    String userId;
    String userEmail;

    public User() {}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public HashMap<String,Object> toHashMap() {
        HashMap<String, Object> userHash = new HashMap<>();
        userHash.put("userId", userId);
        userHash.put("userEmail", userEmail);
        return userHash;
    }
}
