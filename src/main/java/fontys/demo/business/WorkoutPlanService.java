package fontys.demo.business;

import fontys.demo.Domain.*;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.Entity.UserEntity;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.Persistence.impl.ExerciseJPARepository;
import fontys.demo.Persistence.impl.UserJPARepository;
import fontys.demo.Persistence.impl.WorkoutplanJPARepository;
import fontys.demo.business.Exceptions.ExerciseNotFoundException;
import fontys.demo.business.Exceptions.WorkoutPlanNotFoundException;
import fontys.demo.business.Interfaces.WorkoutPlanManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class WorkoutPlanService implements WorkoutPlanManager {

    private final WorkoutplanJPARepository workoutPlanRepository;
    private final ExerciseJPARepository exerciseRepository;
    private final UserJPARepository userRepository;

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
        if (request.getName() == null) {
            throw new RuntimeException("Workout plan name cannot be null");
        }

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkoutPlanEntity workoutPlanEntity = WorkoutPlanConverter.convertCreateWorkoutPlanRequestToEntity(request);
        workoutPlanEntity.setUser(user); // Set the user

        workoutPlanEntity = workoutPlanRepository.save(workoutPlanEntity);
        WorkoutPlan workoutPlan = WorkoutPlanConverter.convertWorkoutPlanEntityToWorkoutPlan(workoutPlanEntity);

        return CreateWorkoutPlanResponse.builder()
                .workoutPlan(workoutPlan)
                .build();
    }

    @Override
    public void updateWorkoutPlan(long workoutPlanId, UpdateWorkoutPlanRequest request) {
        WorkoutPlanEntity existingWorkoutPlanEntity = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new WorkoutPlanNotFoundException("Workout plan not found with ID: " + workoutPlanId));

        if (request.getExercises() == null) {
            throw new RuntimeException("Exercises cannot be null");
        }

        existingWorkoutPlanEntity.setName(request.getName());
        existingWorkoutPlanEntity.setDescription(request.getDescription());
        existingWorkoutPlanEntity.setDurationInDays(request.getDurationInDays());

        List<ExerciseEntity> updatedExercises = request.getExercises().stream()
                .map(requestExercise -> {
                    if (requestExercise.getId() != null) {
                        // Exercise exists, update it
                        ExerciseEntity existingExercise = exerciseRepository.findById(requestExercise.getId())
                                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found with ID: " + requestExercise.getId()));
                        existingExercise.setName(requestExercise.getName());
                        existingExercise.setDescription(requestExercise.getDescription());
                        existingExercise.setDurationInMinutes(requestExercise.getDurationInMinutes());
                        existingExercise.setMuscleGroup(requestExercise.getMuscleGroup());
                        return existingExercise;
                    } else {
                        // New exercise, create it
                        ExerciseEntity newExercise = new ExerciseEntity();
                        newExercise.setName(requestExercise.getName());
                        newExercise.setDescription(requestExercise.getDescription());
                        newExercise.setDurationInMinutes(requestExercise.getDurationInMinutes());
                        newExercise.setMuscleGroup(requestExercise.getMuscleGroup());
                        newExercise.setWorkoutPlan(existingWorkoutPlanEntity);
                        return newExercise;
                    }
                })
                .collect(Collectors.toList());

        existingWorkoutPlanEntity.setExercises(updatedExercises);
        workoutPlanRepository.save(existingWorkoutPlanEntity);
    }

    @Override
    public boolean deleteWorkoutPlan(long workoutPlanId) {
        if (!workoutPlanRepository.existsById(workoutPlanId)) {
            throw new WorkoutPlanNotFoundException("Workout plan not found with ID: " + workoutPlanId);
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

    public List<WorkoutCountDTO> countWorkoutsByUser() {
        List<Object[]> results = workoutPlanRepository.countWorkoutsByUser();
        List<WorkoutCountDTO> workoutCountDTOs = new ArrayList<>();
        for (Object[] result : results) {
            if (result[0] != null && result[1] != null) {
                Long userId = ((Number) result[0]).longValue();
                Long workoutCount = ((Number) result[1]).longValue();
                workoutCountDTOs.add(new WorkoutCountDTO(userId, workoutCount));
            }
        }
        return workoutCountDTOs;
    }

    @Transactional
    public List<GetWorkoutPlansByPTResponse> getWorkoutsByPT(Long ptId) {
        return workoutPlanRepository.findAllByUserId(ptId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private GetWorkoutPlansByPTResponse convertToResponse(WorkoutPlanEntity workoutPlan) {
        return new GetWorkoutPlansByPTResponse(
                workoutPlan.getId(),
                workoutPlan.getName(),
                workoutPlan.getDescription(),
                workoutPlan.getDurationInDays(),
                workoutPlan.getExercises().stream()
                        .map(this::convertExerciseToResponse)
                        .collect(Collectors.toList())
        );
    }



    private GetExerciseResponse convertExerciseToResponse(ExerciseEntity exercise) {
        return new GetExerciseResponse(
                exercise.getId(),
                exercise.getName(),
                exercise.getDescription(),
                exercise.getDurationInMinutes(),
                exercise.getMuscleGroup()
        );
    }

    private GetWorkoutPlanResponse convertToGetWorkoutPlanResponse(WorkoutPlanEntity workoutPlanEntity) {
        WorkoutPlan workoutPlan = mapToWorkoutPlan(workoutPlanEntity);
        return GetWorkoutPlanResponse.builder()
                .workoutPlans(List.of(workoutPlan))
                .build();
    }



}
