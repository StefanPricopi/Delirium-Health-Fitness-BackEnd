package fontys.demo.business;

import fontys.demo.Domain.CreateWorkoutPlanRequest;
import fontys.demo.Domain.WorkoutPlan;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;

public class WorkoutPlanConverter {
    public static WorkoutPlanEntity convertCreateWorkoutPlanRequestToEntity(CreateWorkoutPlanRequest request) {
        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setName(request.getName());
        workoutPlanEntity.setDescription(request.getDescription());
        workoutPlanEntity.setDurationInDays(request.getDurationInDays());
        return workoutPlanEntity;
    }

    public static WorkoutPlan convertWorkoutPlanEntityToWorkoutPlan(WorkoutPlanEntity workoutPlanEntity) {
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.setId(workoutPlanEntity.getId());
        workoutPlan.setName(workoutPlanEntity.getName());
        workoutPlan.setDescription(workoutPlanEntity.getDescription());
        workoutPlan.setDurationInDays(workoutPlanEntity.getDurationInDays());

        return workoutPlan;
    }


}

