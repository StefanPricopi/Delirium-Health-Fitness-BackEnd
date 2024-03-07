package fontys.demo.business;

import fontys.demo.Domain.CreateWorkoutPlanRequest;
import fontys.demo.Domain.Exercise;
import fontys.demo.Domain.WorkoutPlan;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;

import java.util.List;
import java.util.stream.Collectors;

public class WorkoutPlanConverter {
    public static WorkoutPlanEntity convertCreateWorkoutPlanRequestToEntity(CreateWorkoutPlanRequest request) {
        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setName(request.getName());
        workoutPlanEntity.setDescription(request.getDescription());
        workoutPlanEntity.setDurationInDays(request.getDurationInDays());

        // Map exercises if needed
        if (request.getExercises() != null) {
            List<ExerciseEntity> exerciseEntities = request.getExercises().stream()
                    .map(ExerciseConverter::convert)
                    .collect(Collectors.toList());
            workoutPlanEntity.setExercises(exerciseEntities);
        }

        return workoutPlanEntity;
    }

    public static WorkoutPlan convertWorkoutPlanEntityToWorkoutPlan(WorkoutPlanEntity workoutPlanEntity) {
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setId(workoutPlanEntity.getId());
        workoutPlan.setName(workoutPlanEntity.getName());
        workoutPlan.setDescription(workoutPlanEntity.getDescription());
        workoutPlan.setDurationInDays(workoutPlanEntity.getDurationInDays());
        if (workoutPlanEntity.getExercises() != null) {
            List<Exercise> exercises = workoutPlanEntity.getExercises().stream()
                    .map(ExerciseConverter::convert)
                    .collect(Collectors.toList());
            workoutPlan.setExercises(exercises);
        }

        return workoutPlan;
    }


}

