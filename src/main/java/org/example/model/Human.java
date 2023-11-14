package org.example.model;

import java.util.UUID;

public class Human {

    private String id;
    private String name;
    private AnimalType allergy;

    private AnimalType preference;

    public Human() {
        this.id = UUID.randomUUID().toString();
    }

    public Human(String name) {
        this();
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Human setName(String name) {
        this.name = name;
        return this;
    }

    public AnimalType getAllergy() {
        return allergy;
    }

    public Human setAllergy(AnimalType allergy) {
        this.allergy = allergy;
        return this;
    }

    public AnimalType getPreference() {
        return preference;
    }

    public Human setPreference(AnimalType preference) {
        this.preference = preference;
        return this;
    }
}
