package fontys.demo.business;

import fontys.demo.Domain.*;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.Persistence.impl.ExerciseJPARepository;
import fontys.demo.Persistence.impl.WorkoutplanJPARepository;
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

    private final WorkoutplanJPARepository workoutPlanRepository;
    private final ExerciseJPARepository exerciseRepository;
    private final ExerciseService exerciseService;


    @Override
    public GetWorkoutPlanResponse getWorkoutPlans() {
        List<WorkoutPlanEntity> workoutPlanEntities = workoutPlanRepository.findAll();
        List<WorkoutPlan> workoutPlans = workoutPlanEntities.stream()
                .map(this::mapToWorkoutPlan)
                .collect(Collectors.toList());

        return GetWorkoutPlanResponse.builder()
                .workoutPlans(workoutPlans)
                .build();
    }
    private WorkoutPlan mapToWorkoutPlan(WorkoutPlanEntity workoutPlanEntity) {
        WorkoutPlan workoutPlan = WorkoutPlanConverter.convertWorkoutPlanEntityToWorkoutPlan(workoutPlanEntity);
        List<ExerciseEntity> exerciseEntities = exerciseRepository.findByWorkoutPlanId(workoutPlanEntity.getId());
        List<Exercise> exercises = exerciseEntities.stream()
                .map(ExerciseConverter::convert)
                .collect(Collectors.toList());
        workoutPlan.setExercises(exercises);

        // Log the exercises for debugging
        System.out.println("Exercises for workout plan " + workoutPlanEntity.getId() + ": " + exercises);

        return workoutPlan;
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
        // Find the existing workout plan entity by ID
        WorkoutPlanEntity existingWorkoutPlanEntity = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new WorkoutPlanNotFoundException("Workout plan not found with ID: " + workoutPlanId));

        // Update the fields with values from the request
        existingWorkoutPlanEntity.setName(request.getName());
        existingWorkoutPlanEntity.setDescription(request.getDescription());
        existingWorkoutPlanEntity.setDurationInDays(request.getDurationInDays());

        // Save the updated entity
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
        WorkoutPlanEntity workoutPlanEntity = workoutPlanRepository.findById(workoutPlanId)
                .orElse(null);
        if (workoutPlanEntity == null) {
            return GetWorkoutPlanResponse.builder()
                    .error(true)
                    .errorMessage("Workout plan not found")
                    .build();
        }

        WorkoutPlan workoutPlan = mapToWorkoutPlan(workoutPlanEntity);

        return GetWorkoutPlanResponse.builder()
                .workoutPlans(Collections.singletonList(workoutPlan))
                .build();
    }
}
