import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fontys.demo.business.ExerciseService;
import fontys.demo.business.WorkoutPlanService;
import fontys.demo.Persistence.impl.WorkoutplanJPARepository;
import fontys.demo.Persistence.impl.ExerciseJPARepository;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.Domain.GetWorkoutPlanResponse;
import fontys.demo.Domain.CreateWorkoutPlanRequest;
import fontys.demo.Domain.UpdateWorkoutPlanRequest;
import fontys.demo.Domain.WorkoutPlan;

@ExtendWith(MockitoExtension.class)
public class WorkoutPlanServiceTest {

    @Mock
    private WorkoutplanJPARepository workoutplanRepositoryMock;

    @Mock
    private ExerciseJPARepository exerciseRepositoryMock;

    @Mock
    private ExerciseService exerciseServiceMock;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    @Test
    public void testGetWorkoutPlans() {
        // Arrange
        when(workoutplanRepositoryMock.findAll()).thenReturn(Collections.emptyList());

        // Act
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlans();

        // Assert
        assertNotNull(response);
        assertTrue(response.getWorkoutPlans().isEmpty());
    }

    @Test
    public void testCreateWorkoutPlan() {
        // Arrange
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest();
        request.setName("Test Name");
        request.setDescription("Test Description");
        request.setDurationInDays(10);

        // Mock the behavior of workoutPlanRepository.save()
        WorkoutPlanEntity savedWorkoutPlanEntity = new WorkoutPlanEntity();
        savedWorkoutPlanEntity.setId(1L);
        savedWorkoutPlanEntity.setName(request.getName());
        savedWorkoutPlanEntity.setDescription(request.getDescription());
        savedWorkoutPlanEntity.setDurationInDays(request.getDurationInDays());
        when(workoutplanRepositoryMock.save(any(WorkoutPlanEntity.class)))
                .thenReturn(savedWorkoutPlanEntity);

        // Act
        WorkoutPlan createdWorkoutPlan = workoutPlanService.createWorkoutPlan(request).getWorkoutPlan();

        // Assert
        assertNotNull(createdWorkoutPlan);
        assertEquals(request.getName(), createdWorkoutPlan.getName());
        assertEquals(request.getDescription(), createdWorkoutPlan.getDescription());
        assertEquals(request.getDurationInDays(), createdWorkoutPlan.getDurationInDays());
    }

    @Test
    public void testCreateWorkoutPlan_WithNullName() {
        // Arrange
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest();
        request.setName(null);
        request.setDescription("Test Description");
        request.setDurationInDays(10);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.createWorkoutPlan(request);
        });
        assertEquals("Workout plan name cannot be null", thrown.getMessage());
    }

    @Test
    public void testUpdateWorkoutPlan() {
        // Arrange
        long workoutPlanId = 1L;
        UpdateWorkoutPlanRequest request = new UpdateWorkoutPlanRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Description");
        request.setDurationInDays(15);
        request.setExercises(Collections.emptyList()); // Set empty list to avoid NullPointerException

        // Mock an existing workout plan entity
        WorkoutPlanEntity existingWorkoutPlanEntity = new WorkoutPlanEntity();
        existingWorkoutPlanEntity.setId(workoutPlanId);
        existingWorkoutPlanEntity.setName("Original Name");
        existingWorkoutPlanEntity.setDescription("Original Description");
        existingWorkoutPlanEntity.setDurationInDays(10);

        // Mock the behavior of workoutPlanRepository.findById()
        when(workoutplanRepositoryMock.findById(workoutPlanId))
                .thenReturn(Optional.of(existingWorkoutPlanEntity));

        // Act
        workoutPlanService.updateWorkoutPlan(workoutPlanId, request);

        // Assert
        assertEquals(request.getName(), existingWorkoutPlanEntity.getName());
        assertEquals(request.getDescription(), existingWorkoutPlanEntity.getDescription());
        assertEquals(request.getDurationInDays(), existingWorkoutPlanEntity.getDurationInDays());
    }

    @Test
    public void testUpdateWorkoutPlan_NotFound() {
        // Arrange
        long workoutPlanId = 1L;
        UpdateWorkoutPlanRequest request = new UpdateWorkoutPlanRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Description");
        request.setDurationInDays(15);
        request.setExercises(Collections.emptyList()); // Set empty list to avoid NullPointerException

        // Mock the behavior of workoutPlanRepository.findById()
        when(workoutplanRepositoryMock.findById(workoutPlanId))
                .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.updateWorkoutPlan(workoutPlanId, request);
        });
        assertEquals("Workout plan not found with ID: " + workoutPlanId, thrown.getMessage());
    }

    @Test
    public void testDeleteWorkoutPlan() {
        // Arrange
        long workoutPlanId = 1L;
        when(workoutplanRepositoryMock.existsById(workoutPlanId)).thenReturn(true);

        // Act
        boolean result = workoutPlanService.deleteWorkoutPlan(workoutPlanId);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testGetWorkoutPlanById_Success() {
        // Arrange
        long workoutPlanId = 1L;
        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setId(workoutPlanId);
        workoutPlanEntity.setName("Test Workout Plan");
        when(workoutplanRepositoryMock.findById(workoutPlanId)).thenReturn(Optional.of(workoutPlanEntity));

        // Act
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(workoutPlanId);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getWorkoutPlans().size());
        assertEquals(workoutPlanId, response.getWorkoutPlans().get(0).getId());
        assertEquals("Test Workout Plan", response.getWorkoutPlans().get(0).getName());
    }

    @Test
    public void testGetWorkoutPlanById_NotFound() {
        // Arrange
        long workoutPlanId = 1L;
        when(workoutplanRepositoryMock.findById(workoutPlanId)).thenReturn(Optional.empty());

        // Act
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(workoutPlanId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isError());
        assertEquals("Workout plan not found", response.getErrorMessage());
    }

    // New Test: Test for updating a workout plan with no exercises provided
    @Test
    public void testUpdateWorkoutPlan_NoExercises() {
        // Arrange
        long workoutPlanId = 1L;
        UpdateWorkoutPlanRequest request = new UpdateWorkoutPlanRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Description");
        request.setDurationInDays(15);
        request.setExercises(null); // No exercises provided

        // Mock an existing workout plan entity
        WorkoutPlanEntity existingWorkoutPlanEntity = new WorkoutPlanEntity();
        existingWorkoutPlanEntity.setId(workoutPlanId);
        existingWorkoutPlanEntity.setName("Original Name");
        existingWorkoutPlanEntity.setDescription("Original Description");
        existingWorkoutPlanEntity.setDurationInDays(10);

        // Mock the behavior of workoutPlanRepository.findById()
        when(workoutplanRepositoryMock.findById(workoutPlanId))
                .thenReturn(Optional.of(existingWorkoutPlanEntity));

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.updateWorkoutPlan(workoutPlanId, request);
        });
        assertEquals("Exercises cannot be null", thrown.getMessage());
    }
}
