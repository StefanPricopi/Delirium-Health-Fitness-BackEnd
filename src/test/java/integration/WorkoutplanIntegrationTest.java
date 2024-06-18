package integration;

import fontys.demo.domain.*;
import fontys.demo.Main;
import fontys.demo.persistence.entity.ExerciseEntity;
import fontys.demo.persistence.entity.UserEntity;
import fontys.demo.persistence.entity.WorkoutPlanEntity;
import fontys.demo.business.WorkoutPlanService;
import fontys.demo.persistence.impl.WorkoutplanJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
 class WorkoutplanIntegrationTest {

    @Autowired
    private WorkoutPlanService workoutPlanService;

    @Autowired
    private WorkoutplanJPARepository workoutplanJPARepository;

    @BeforeEach
    void setUp() {
        workoutplanJPARepository.deleteAll();
        workoutplanJPARepository.flush();
    }



    @Test
     void testUpdateWorkoutPlan() {
        // Given
        UserEntity user = new UserEntity(); // Assume UserEntity is properly constructed
        user.setId(1L); // Set user id or save and retrieve to ensure consistency
        WorkoutPlanEntity existingPlan = new WorkoutPlanEntity(null, "Plan D", "Old Description", 30, Collections.emptyList(), user);
        WorkoutPlanEntity savedPlan = workoutplanJPARepository.save(existingPlan);

        // Assuming we have a method to convert ExerciseEntity to Exercise
        List<Exercise> exercises = existingPlan.getExercises().stream()
                .map(this::convertToDomainExercise)
                .collect(Collectors.toList());

        UpdateWorkoutPlanRequest updateRequest = new UpdateWorkoutPlanRequest(
                savedPlan.getId(), "Updated Plan D", "Updated Description", 35, exercises);

        // When
        workoutPlanService.updateWorkoutPlan(savedPlan.getId(), updateRequest,1L);

        // Then
        Optional<WorkoutPlanEntity> updatedPlan = workoutplanJPARepository.findById(savedPlan.getId());
        assertTrue(updatedPlan.isPresent());
        assertEquals("Updated Plan D", updatedPlan.get().getName());
        assertEquals("Updated Description", updatedPlan.get().getDescription());
    }

    @Test
     void testDeleteWorkoutPlan() {
        // Given
        UserEntity user = new UserEntity(); // Assume UserEntity is properly constructed
        user.setId(1L); // Set user id or save and retrieve to ensure consistency
        WorkoutPlanEntity workoutPlan = new WorkoutPlanEntity(null, "Plan E", "Description E", 50, Collections.emptyList(), user);
        WorkoutPlanEntity savedPlan = workoutplanJPARepository.save(workoutPlan);

        // When
        boolean result = workoutPlanService.deleteWorkoutPlan(savedPlan.getId(),1L);

        // Then
        assertTrue(result);
        assertFalse(workoutplanJPARepository.existsById(savedPlan.getId()));
    }

    // Helper method to convert ExerciseEntity to Exercise domain object
    private Exercise convertToDomainExercise(ExerciseEntity exerciseEntity) {
        return new Exercise(exerciseEntity.getId(), exerciseEntity.getName(), exerciseEntity.getDescription());
    }

}