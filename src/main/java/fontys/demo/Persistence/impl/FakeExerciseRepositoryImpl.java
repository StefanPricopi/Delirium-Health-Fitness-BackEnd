package fontys.demo.Persistence.impl;

import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.ExerciseRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class FakeExerciseRepositoryImpl implements ExerciseRepository {
    private static long NEXT_ID = 1;

    private final List<ExerciseEntity> savedExercises;

    public FakeExerciseRepositoryImpl() {
        this.savedExercises = new ArrayList<>();
    }

    @Override
    public boolean existsById(Long id) {
        return this.savedExercises.stream().anyMatch(exercise -> exercise.getId().equals(id));
    }

    @Override
    public ExerciseEntity findById(Long id) {
        return this.savedExercises.stream().filter(exercise -> exercise.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public ExerciseEntity save(ExerciseEntity exercise) {
        exercise.setId(NEXT_ID);
        NEXT_ID++;
        this.savedExercises.add(exercise);
        return exercise;
    }

    @Override
    public List<ExerciseEntity> findAll() {
        return Collections.unmodifiableList(savedExercises);
    }

    @Override
    public void deleteById(Long id) {
        this.savedExercises.removeIf(exercise -> exercise.getId().equals(id));
    }

    @Override
    public int count() {
        return this.savedExercises.size();
    }
}