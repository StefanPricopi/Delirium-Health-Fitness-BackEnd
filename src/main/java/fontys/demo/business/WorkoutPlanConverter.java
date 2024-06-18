package fontys.demo.business;

import fontys.demo.domain.CreateWorkoutPlanRequest;
import fontys.demo.domain.WorkoutPlan;
import fontys.demo.persistence.entity.WorkoutPlanEntity;

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

