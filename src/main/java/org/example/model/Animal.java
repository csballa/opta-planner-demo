package org.example.model;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.UUID;

@PlanningEntity
public class Animal {
    @PlanningId
    String id;
    String name;
    AnimalType type;
    @PlanningVariable(valueRangeProviderRefs = {"availableHumans"}, nullable = true)
    Human owner;

    public Animal() {
        this.id = UUID.randomUUID().toString();
    }

    public Animal(String name, AnimalType type) {
        this();
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Animal setName(String name) {
        this.name = name;
        return this;
    }

    public AnimalType getType() {
        return type;
    }

    public Animal setType(AnimalType type) {
        this.type = type;
        return this;
    }

    public Human getOwner() {
        return owner;
    }

    public Animal setOwner(Human owner) {
        this.owner = owner;
        return this;
    }
}
