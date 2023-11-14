package org.example.solver;

import org.example.model.Animal;
import org.example.model.Human;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class AnimalAssignment {
//    @ValueRangeProvider(id = "availableAnimals")
    @PlanningEntityCollectionProperty
    List<Animal> animals;
    @ValueRangeProvider(id = "availableHumans")
    @ProblemFactCollectionProperty
    List<Human> humans;

    @PlanningScore
    private HardSoftScore score;


    public List<Animal> getAnimals() {
        return animals;
    }

    public AnimalAssignment setAnimals(List<Animal> animals) {
        this.animals = animals;
        return this;
    }

    public List<Human> getHumans() {
        return humans;
    }

    public AnimalAssignment setHumans(List<Human> humans) {
        this.humans = humans;
        return this;
    }

    public HardSoftScore getScore() {
        return score;
    }

    public AnimalAssignment setScore(HardSoftScore score) {
        this.score = score;
        return this;
    }
}
