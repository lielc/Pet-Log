package com.example.lielco.petlog.Pet;

public class Pet {
    String petId;
    String petName;
    String petImageUrl;

    public Pet(String petId, String petName, String petImageUrl) {
        this.petId = petId;
        this.petName = petName;
        this.petImageUrl = petImageUrl;
    }

    public String getPetName() {
        return petName;
    }
}
