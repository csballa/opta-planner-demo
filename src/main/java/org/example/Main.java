package org.example;

import org.example.model.Animal;
import org.example.model.AnimalType;
import org.example.model.Human;
import org.example.solver.AnimalAssignment;
import org.example.solver.AnimalAssignmentConstraintProvider;
import org.example.solver.AnimalAssignmentScoreCalculator;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
//        solve(getEasySolver());
        solve(getConstraintSolver());
    }

    private static void solve(SolverFactory<AnimalAssignment> solverFactory) {
        Solver<AnimalAssignment> solver = solverFactory.buildSolver();
        AnimalAssignment unsolved = new AnimalAssignment();


        setupBaseData(unsolved);
        AnimalAssignment solved = solver.solve(unsolved);
        printResult(solved);
    }

    private static SolverFactory<AnimalAssignment> getEasySolver() {
        return SolverFactory.create(new SolverConfig()
                .withSolutionClass(AnimalAssignment.class)
                .withEntityClasses(Animal.class)
                .withEasyScoreCalculatorClass(AnimalAssignmentScoreCalculator.class)
                .withTerminationSpentLimit(Duration.ofSeconds(1)));
    }
    private static SolverFactory<AnimalAssignment> getConstraintSolver() {
        return SolverFactory.create(new SolverConfig()
                .withSolutionClass(AnimalAssignment.class)
                .withEntityClasses(Animal.class)
                .withConstraintProviderClass(AnimalAssignmentConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(1)));
    }




    private static void setupBaseData(AnimalAssignment unsolved) {
        unsolved.setAnimals(generateAnimals());
        unsolved.setHumans(generateHumans());
    }

    private static List<Human> generateHumans() {
        return List.of(
//                new Human("John"),
//                new Human("Mary").setPreference(AnimalType.CAT),
                new Human("Bob").setAllergy(AnimalType.DOG),
                new Human("Alice").setAllergy(AnimalType.CAT).setPreference(AnimalType.DOG)

        );
    }

    private static List<Animal> generateAnimals() {
        return List.of(
                new Animal("Fluffy", AnimalType.CAT),
                new Animal("Whiskers", AnimalType.CAT),
                new Animal("Fido", AnimalType.DOG),
                new Animal("Rex", AnimalType.DOG),
                new Animal("Spot", AnimalType.DOG),
                new Animal("Rover", AnimalType.DOG)
        );
    }

    private static void printResult(AnimalAssignment solved) {
        System.out.println("Solved: " + solved.getScore());
        HashMap<Human, Set<Animal>> petsByOwner = new HashMap<>();
        solved.getAnimals().forEach(a-> petsByOwner.computeIfAbsent(a.getOwner(), k -> new HashSet<>()).add(a));
        for (Map.Entry<Human, Set<Animal>> a : petsByOwner.entrySet()) {
            System.out.print((a.getKey() == null? " " : a.getKey().getName()) + " -> ");
            String collect = a.getValue().stream().map(Animal::getName).collect(Collectors.joining(", "));
            System.out.println(collect);
        }
    }
}