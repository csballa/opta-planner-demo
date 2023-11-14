package org.example.solver;

import org.example.model.Animal;
import org.example.model.Human;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.toSet;

public class AnimalAssignmentConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard constraints
                mustHaveAnOwner(constraintFactory),
                mustNotHaveThreeOrMoreAnimals(constraintFactory),
                ownerMustNotBeAllergic(constraintFactory),
                shouldNotExceedTwoAnimals(constraintFactory),
                animalTypeShouldBePreferred(constraintFactory)
                // Soft constraints are only implemented in the optaplanner-quickstarts code
        };
    }

    private Constraint mustHaveAnOwner(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                .forEachIncludingNullVars(Animal.class)
                .filter((a) -> a.getOwner() == null)
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Must have an owner");
    }

    private Constraint mustNotHaveThreeOrMoreAnimals(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachIncludingNullVars(Animal.class)
                .groupBy(Animal::getOwner, toSet(a-> a))
                .filter((owner, animals) -> animals.size() > 2)
                .penalize(HardSoftScore.ONE_HARD, (o, a) -> 1)
                .asConstraint("There mustn't be more than two animals.");
    }

    private Constraint ownerMustNotBeAllergic(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                .forEach(Animal.class)
                .filter(a-> a.getType().equals(a.getOwner().getAllergy()))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Owner mustn't be allergic.");
    }

    private Constraint shouldNotExceedTwoAnimals(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                .forEach(Animal.class)
                .join(Animal.class,
                        Joiners.equal(Animal::getOwner),
                        Joiners.lessThan(Animal::getId))
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Animals should be distributed evenly.");
    }

    private Constraint shouldNotExceedTwoAnimalsWeighted(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                .forEach(Animal.class)
                .groupBy(Animal::getOwner, count())
                .filter((owner, count) -> count >= 2)
                .penalize(HardSoftScore.ONE_SOFT, (owner, count) -> count * 2)
                .asConstraint("Penalize by owned animals.");
    }

    private Constraint animalTypeShouldBePreferred(ConstraintFactory constraintFactory) {
        // A room can accommodate at most one lesson at the same time.
        return constraintFactory
                .forEach(Animal.class)
                .join(Human.class)
                .filter((animal, human) -> !animal.getType().equals(human.getPreference()))
                .penalize(HardSoftScore.ONE_SOFT, (animal, human) -> 2)
                .asConstraint("Animal should be preferred.");
    }
}
