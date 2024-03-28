package fontys.demo.business;

import fontys.demo.Domain.*;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.Persistence.WorkoutPlanRepository;
import fontys.demo.business.Exceptions.WorkoutPlanNotFoundException;
import fontys.demo.business.Interfaces.WorkoutPlanManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;



@Service
@AllArgsConstructor
public class WorkoutPlanService implements WorkoutPlanManager {

    private final WorkoutPlanRepository workoutPlanRepository;
    @Override
    public GetWorkoutPlanResponse getWorkoutPlans() {
        List<WorkoutPlanEntity> workoutPlanEntities = workoutPlanRepository.findAll();
        List<WorkoutPlan> workoutPlans = workoutPlanEntities.stream()
                .map(WorkoutPlanConverter::convertWorkoutPlanEntityToWorkoutPlan)
                .collect(Collectors.toList());

        return GetWorkoutPlanResponse.builder()
                .workoutPlans(workoutPlans)
                .build();
    }
    @Override
    public CreateWorkoutPlanResponse createWorkoutPlan(CreateWorkoutPlanRequest request) {
        WorkoutPlanEntity workoutPlanEntity = WorkoutPlanConverter.convertCreateWorkoutPlanRequestToEntity(request);
        workoutPlanEntity = workoutPlanRepository.save(workoutPlanEntity);
        WorkoutPlan workoutPlan = WorkoutPlanConverter.convertWorkoutPlanEntityToWorkoutPlan(workoutPlanEntity);

        return CreateWorkoutPlanResponse.builder()
                .workoutPlan(workoutPlan)
                .build();
    }

    @Override
    public void updateWorkoutPlan(long workoutPlanId, UpdateWorkoutPlanRequest request) {
        WorkoutPlanEntity existingWorkoutPlanEntity = workoutPlanRepository.findById(workoutPlanId);

        if (existingWorkoutPlanEntity == null) {
            throw new WorkoutPlanNotFoundException("Workout plan not found with ID: " + workoutPlanId);
        }

        existingWorkoutPlanEntity.setName(request.getName());
        existingWorkoutPlanEntity.setDescription(request.getDescription());
        existingWorkoutPlanEntity.setDurationInDays(request.getDurationInDays());

        workoutPlanRepository.save(existingWorkoutPlanEntity);
    }



    @Override
    public boolean deleteWorkoutPlan(long workoutPlanId) {
        if (!workoutPlanRepository.existsById(workoutPlanId)) {
            return false;
        }
        workoutPlanRepository.deleteById(workoutPlanId);
        return true;
    }


    @Override
    public GetWorkoutPlanResponse getWorkoutPlanById(long workoutPlanId) {
        WorkoutPlanEntity workoutPlanEntity = workoutPlanRepository.findById(workoutPlanId);
        if (workoutPlanEntity == null) {
            return GetWorkoutPlanResponse.builder()
                    .error(true)
                    .errorMessage("Workout plan not found")
                    .build();
        }

        WorkoutPlan workoutPlan = WorkoutPlanConverter.convertWorkoutPlanEntityToWorkoutPlan(workoutPlanEntity);
        return GetWorkoutPlanResponse.builder()
                .workoutPlans(Collections.singletonList(workoutPlan))
                .build();
    }




}
