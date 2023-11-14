package org.example.solver;

import org.example.model.Animal;
import org.example.model.Human;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.calculator.EasyScoreCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnimalAssignmentScoreCalculator implements EasyScoreCalculator<AnimalAssignment, HardSoftScore> {

    @Override
    public HardSoftScore calculateScore(AnimalAssignment assignment) {
        List<Animal> animals = assignment.getAnimals();
        int hardScore = 0;
        int softScore = 0;
        Map<Human, List<Animal>> animalOwners = new HashMap<>();
        for (Animal a : animals) {
            Human aOwner = a.getOwner();
            if (aOwner == null) {
                hardScore -= 1;
            } else {
                //An owner should be no more than two animals, but even distribution is the best.
                  if (animalOwners.containsKey(aOwner)) {
                      List<Animal> pets = animalOwners.get(aOwner);
                      pets.add(a);
                      int ownedAnimals = pets.size();
                      if(ownedAnimals > 2) {
                          hardScore -= 1;
                      } else {
                          softScore -= ownedAnimals * 2;
                      }
                  } else {
                      animalOwners.computeIfAbsent(aOwner, k -> new ArrayList<>()).add(a);
                  }

                    //No allergy matching
                if (aOwner.getAllergy()!= null && aOwner.getAllergy().equals(a.getType())) {
                    hardScore--;
                }
                //Preference matching
                if (!a.getType().equals(aOwner.getPreference())) {
                    softScore--;
                }
            }
        }
        return HardSoftScore.of(hardScore, softScore);
    }

}
