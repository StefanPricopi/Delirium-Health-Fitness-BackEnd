package fontys.demo.Persistence;

import fontys.demo.Persistence.Entity.WorkoutPlanEntity;

import java.util.List;

public interface WorkoutPlanRepository {
     boolean existsById(Long id);
     WorkoutPlanEntity findById(Long id);
     WorkoutPlanEntity save(WorkoutPlanEntity workoutPlan);
     List<WorkoutPlanEntity> findAll();
     void deleteById(Long id);
     int count();
}
