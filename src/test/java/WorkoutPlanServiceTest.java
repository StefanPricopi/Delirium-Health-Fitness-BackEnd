import fontys.demo.Domain.*;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.Persistence.impl.WorkoutplanJPARepository;
import fontys.demo.business.ExerciseService;
import fontys.demo.business.WorkoutPlanService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WorkoutPlanServiceTest {

    @Mock
    private WorkoutplanJPARepository workoutplanRepositoryMock;

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
        Mockito.when(workoutplanRepositoryMock.save(Mockito.any(WorkoutPlanEntity.class)))
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
    public void testUpdateWorkoutPlan() {
        // Arrange
        long workoutPlanId = 1L;
        UpdateWorkoutPlanRequest request = new UpdateWorkoutPlanRequest();
        request.setName("Updated Name");
        request.setDescription("Updated Description");
        request.setDurationInDays(15);

        // Mock an existing workout plan entity
        WorkoutPlanEntity existingWorkoutPlanEntity = new WorkoutPlanEntity();
        existingWorkoutPlanEntity.setId(workoutPlanId);
        existingWorkoutPlanEntity.setName("Original Name");
        existingWorkoutPlanEntity.setDescription("Original Description");
        existingWorkoutPlanEntity.setDurationInDays(10);

        // Mock the behavior of workoutPlanRepository.findById()
        Mockito.when(workoutplanRepositoryMock.findById(workoutPlanId))
                .thenReturn(Optional.of(existingWorkoutPlanEntity));

        // Act
        workoutPlanService.updateWorkoutPlan(workoutPlanId, request);

        // Assert
        assertEquals(request.getName(), existingWorkoutPlanEntity.getName());
        assertEquals(request.getDescription(), existingWorkoutPlanEntity.getDescription());
        assertEquals(request.getDurationInDays(), existingWorkoutPlanEntity.getDurationInDays());
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
        when(exerciseServiceMock.getExercisesByWorkoutPlanId(workoutPlanId)).thenReturn(Collections.emptyList());

        // Act
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(workoutPlanId);

        // Assert
        assertNotNull(response);
        assertFalse(response.isError());
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
}
