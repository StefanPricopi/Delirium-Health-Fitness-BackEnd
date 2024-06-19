package fontys.demo.business;

import fontys.demo.business.exceptions.ExerciseNotFoundException;
import fontys.demo.business.exceptions.UnauthorizedAccessException;
import fontys.demo.business.exceptions.WorkoutPlanNotFoundException;
import fontys.demo.business.interfaces.WorkoutPlanManager;
import fontys.demo.domain.*;
import fontys.demo.domain.userDomain.User;
import fontys.demo.persistence.entity.ExerciseEntity;
import fontys.demo.persistence.entity.UserEntity;
import fontys.demo.persistence.entity.WorkoutPlanEntity;
import fontys.demo.persistence.impl.ExerciseJPARepository;
import fontys.demo.persistence.impl.UserJPARepository;
import fontys.demo.persistence.impl.WorkoutplanJPARepository;
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
                .map(this::mapToWorkoutPlanWithUser)
                .toList();

        return GetWorkoutPlanResponse.builder()
                .workoutPlans(workoutPlans)
                .build();
    }




    @Override
    public CreateWorkoutPlanResponse createWorkoutPlan(CreateWorkoutPlanRequest request) {
        if (request.getName() == null) {
            throw new RuntimeException("Workout plan name cannot be null");
        }

        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        WorkoutPlanEntity workoutPlanEntity = WorkoutPlanConverter.convertCreateWorkoutPlanRequestToEntity(request);
        workoutPlanEntity.setUser(user);

        workoutPlanEntity = workoutPlanRepository.save(workoutPlanEntity);
        WorkoutPlan workoutPlan = WorkoutPlanConverter.convertWorkoutPlanEntityToWorkoutPlan(workoutPlanEntity);

        return CreateWorkoutPlanResponse.builder()
                .workoutPlan(workoutPlan)
                .build();
    }

    public void updateWorkoutPlan(long workoutPlanId, UpdateWorkoutPlanRequest request, Long userId) {
        WorkoutPlanEntity existingWorkoutPlanEntity = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new WorkoutPlanNotFoundException("Workout plan not found with ID: " + workoutPlanId));

        // Check if the user is the owner of the workout plan
        if (!existingWorkoutPlanEntity.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to modify this workout plan.");
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
                        return getExerciseEntity(requestExercise, existingWorkoutPlanEntity);
                    }
                })
                .collect(Collectors.toCollection(ArrayList::new)); // Ensure mutable collection

        existingWorkoutPlanEntity.setExercises(updatedExercises);
        workoutPlanRepository.save(existingWorkoutPlanEntity);
    }


    private static ExerciseEntity getExerciseEntity(Exercise requestExercise, WorkoutPlanEntity existingWorkoutPlanEntity) {
        ExerciseEntity newExercise = new ExerciseEntity();
        newExercise.setName(requestExercise.getName());
        newExercise.setDescription(requestExercise.getDescription());
        newExercise.setDurationInMinutes(requestExercise.getDurationInMinutes());
        newExercise.setMuscleGroup(requestExercise.getMuscleGroup());
        newExercise.setWorkoutPlan(existingWorkoutPlanEntity);
        return newExercise;
    }


    @Override
    public boolean deleteWorkoutPlan(long workoutPlanId, Long userId) {
        WorkoutPlanEntity workoutPlanEntity = workoutPlanRepository.findById(workoutPlanId)
                .orElseThrow(() -> new WorkoutPlanNotFoundException("Workout plan not found with ID: " + workoutPlanId));

        // Check if the user is the owner of the workout plan
        if (!workoutPlanEntity.getUser().getId().equals(userId)) {
            throw new UnauthorizedAccessException("You do not have permission to delete this workout plan.");
        }

        workoutPlanRepository.delete(workoutPlanEntity);
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
                .toList();
    }


    private GetWorkoutPlansByPTResponse convertToResponse(WorkoutPlanEntity workoutPlan) {
        return new GetWorkoutPlansByPTResponse(
                workoutPlan.getId(),
                workoutPlan.getName(),
                workoutPlan.getDescription(),
                workoutPlan.getDurationInDays(),
                workoutPlan.getExercises().stream()
                        .map(this::convertExerciseToResponse)
                        .toList()
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
    public WorkoutPlan mapToWorkoutPlanWithUser(WorkoutPlanEntity entity) {
        WorkoutPlan workoutPlan = mapToWorkoutPlan(entity);
        if (entity.getUser() != null) {
            User user = mapToUser(entity.getUser());
            workoutPlan.setUser(user);
        }
        return workoutPlan;
    }

    public WorkoutPlan mapToWorkoutPlan(WorkoutPlanEntity entity) {
        return WorkoutPlan.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .durationInDays(entity.getDurationInDays())
                .exercises(entity.getExercises().stream()
                        .map(this::mapToExercise)
                        .toList())
                .build();
    }


    public Exercise mapToExercise(ExerciseEntity entity) {
        return Exercise.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .durationInMinutes(entity.getDurationInMinutes())
                .muscleGroup(entity.getMuscleGroup())
                .build();
    }

    public User mapToUser(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .roles(entity.getRoles())
                .build();
    }
}
