package fontys.demo.business.interfaces;

import fontys.demo.domain.*;


public interface WorkoutPlanManager {
     GetWorkoutPlanResponse getWorkoutPlans();
     CreateWorkoutPlanResponse createWorkoutPlan(CreateWorkoutPlanRequest request);
     public void updateWorkoutPlan(long workoutPlanId, UpdateWorkoutPlanRequest request, Long userId);
     public boolean deleteWorkoutPlan(long workoutPlanId, Long userId);
     public GetWorkoutPlanResponse getWorkoutPlanById(long workoutPlanId);
}
