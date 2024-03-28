package fontys.demo.Persistence.impl;

import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.Persistence.WorkoutPlanRepository;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class FakeWorkoutPlanRepositoryImpl implements WorkoutPlanRepository {
    private static long NEXT_ID = 1;

    private final List<WorkoutPlanEntity> savedWorkoutPlans;

    public FakeWorkoutPlanRepositoryImpl() {
        this.savedWorkoutPlans = new ArrayList<>();
    }

    @Override
    public boolean existsById(Long id) {
        return this.savedWorkoutPlans.stream().anyMatch(workoutPlanEntity -> workoutPlanEntity.getId().equals(id));
    }

    @Override
    public WorkoutPlanEntity findById(Long id) {
        return this.savedWorkoutPlans.stream().filter(workoutPlanEntity -> workoutPlanEntity.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public WorkoutPlanEntity save(WorkoutPlanEntity workoutPlanEntity) {
        workoutPlanEntity.setId(NEXT_ID);
        NEXT_ID++;
        this.savedWorkoutPlans.add(workoutPlanEntity);
        return workoutPlanEntity;
    }


    @Override
    public List<WorkoutPlanEntity> findAll() {
        return Collections.unmodifiableList(savedWorkoutPlans);
    }

    @Override
    public void deleteById(Long id) {
        this.savedWorkoutPlans.removeIf(workoutPlanEntity -> workoutPlanEntity.getId().equals(id));
    }

    @Override
    public int count() {
        return this.savedWorkoutPlans.size();
    }
}

