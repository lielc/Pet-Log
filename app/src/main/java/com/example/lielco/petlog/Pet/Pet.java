package com.example.lielco.petlog.Pet;

import java.util.HashMap;

public class Pet {
    String petId;
    String petName;
    String petImageUrl;

    public Pet(){}

    public Pet(String petName, String petImageUrl) {
        this.petName = petName;
        this.petImageUrl = petImageUrl;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId (String id) {
        this.petId = id;
    }

    public String getPetName() {
        return petName;
    }

    public void setPetName(String petName) {
        this.petName = petName;
    }

    public String getPetImageUrl() {
        return petImageUrl;
    }

    public void setPetImageUrl(String petImageUrl) {
        this.petImageUrl = petImageUrl;
    }

    public HashMap<String,Object> toHashMap() {
        HashMap<String, Object> petHash = new HashMap<>();
        petHash.put("petName", petName);
        petHash.put("petImageUrl", petImageUrl);
        petHash.put("petId", petId);
        return petHash;
    }
}
