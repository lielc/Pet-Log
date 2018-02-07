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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pet pet = (Pet) o;

        if (!petId.equals(pet.petId)) return false;
        if (petName != null ? !petName.equals(pet.petName) : pet.petName != null) return false;
        return petImageUrl != null ? petImageUrl.equals(pet.petImageUrl) : pet.petImageUrl == null;
    }

    @Override
    public int hashCode() {
        int result = petId.hashCode();
        result = 31 * result + (petName != null ? petName.hashCode() : 0);
        result = 31 * result + (petImageUrl != null ? petImageUrl.hashCode() : 0);
        return result;
    }
}
