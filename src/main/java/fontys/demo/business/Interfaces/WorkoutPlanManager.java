package fontys.demo.business.Interfaces;

import fontys.demo.Domain.*;


public interface WorkoutPlanManager {
     GetWorkoutPlanResponse getWorkoutPlans();
     CreateWorkoutPlanResponse createWorkoutPlan(CreateWorkoutPlanRequest request);
     public void updateWorkoutPlan(long workoutPlanId, UpdateWorkoutPlanRequest request);
     public boolean deleteWorkoutPlan(long workoutPlanId);
     public GetWorkoutPlanResponse getWorkoutPlanById(long workoutPlanId);
}
