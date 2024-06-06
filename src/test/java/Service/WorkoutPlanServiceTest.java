package Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import fontys.demo.Domain.*;
import fontys.demo.Domain.UserDomain.User;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.Entity.UserEntity;
import fontys.demo.Persistence.impl.UserJPARepository;
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

@ExtendWith(MockitoExtension.class)
public class WorkoutPlanServiceTest {

    @Mock
    private WorkoutplanJPARepository workoutplanRepositoryMock;
    @Mock
    private UserJPARepository userRepositoryMock;
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
        request.setUserId(1L);

        // Mock the behavior of userRepository.findById()
        UserEntity user = new UserEntity(); // Assume UserEntity is properly constructed
        user.setId(1L); // Set user id or save and retrieve to ensure consistency
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.of(user));

        // Mock the behavior of workoutPlanRepository.save()
        WorkoutPlanEntity savedWorkoutPlanEntity = WorkoutPlanEntity.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .durationInDays(request.getDurationInDays())
                .exercises(Collections.emptyList())
                .user(user)
                .build();

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
        UserEntity user = new UserEntity(); // Assume UserEntity is properly constructed
        user.setId(1L); // Set user id or save and retrieve to ensure consistency

        WorkoutPlanEntity workoutPlanEntity = WorkoutPlanEntity.builder()
                .id(workoutPlanId)
                .name("Test Workout Plan")
                .description("Test Description")
                .durationInDays(10)
                .exercises(Collections.emptyList()) // Initialize exercises as an empty list
                .user(user)
                .build();

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
    @Test
    public void testDeleteWorkoutPlan_NotFound() {
        // Arrange
        long workoutPlanId = 1L;
        when(workoutplanRepositoryMock.existsById(workoutPlanId)).thenReturn(false);

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.deleteWorkoutPlan(workoutPlanId);
        });
        assertEquals("Workout plan not found with ID: " + workoutPlanId, thrown.getMessage());
    }
    @Test
    public void testCountWorkoutsByUser() {
        // Arrange
        Object[] result1 = {1L, 5L}; // userId, workoutCount
        Object[] result2 = {2L, 3L};
        List<Object[]> results = List.of(result1, result2);
        when(workoutplanRepositoryMock.countWorkoutsByUser()).thenReturn(results);

        // Act
        List<WorkoutCountDTO> workoutCountDTOs = workoutPlanService.countWorkoutsByUser();

        // Assert
        assertEquals(2, workoutCountDTOs.size());
        assertEquals(1L, workoutCountDTOs.get(0).getUserId());
        assertEquals(5L, workoutCountDTOs.get(0).getWorkoutCount());
        assertEquals(2L, workoutCountDTOs.get(1).getUserId());
        assertEquals(3L, workoutCountDTOs.get(1).getWorkoutCount());
    }
    @Test
    public void testGetWorkoutsByPT() {
        // Arrange
        Long ptId = 1L;
        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setId(1L);
        workoutPlanEntity.setName("Test Plan");
        workoutPlanEntity.setDescription("Test Description");
        workoutPlanEntity.setDurationInDays(30);
        workoutPlanEntity.setExercises(Collections.emptyList());

        when(workoutplanRepositoryMock.findAllByUserId(ptId)).thenReturn(Collections.singletonList(workoutPlanEntity));

        // Act
        List<GetWorkoutPlansByPTResponse> responses = workoutPlanService.getWorkoutsByPT(ptId);

        // Assert
        assertEquals(1, responses.size());
        GetWorkoutPlansByPTResponse response = responses.get(0);
        assertEquals(workoutPlanEntity.getId(), response.getId());
        assertEquals(workoutPlanEntity.getName(), response.getName());
        assertEquals(workoutPlanEntity.getDescription(), response.getDescription());
        assertEquals(workoutPlanEntity.getDurationInDays(), response.getDurationInDays());
        assertTrue(response.getExercises().isEmpty());
    }
    @Test
    public void testCreateWorkoutPlan_UserNotFound() {
        // Arrange
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest();
        request.setName("Test Name");
        request.setDescription("Test Description");
        request.setDurationInDays(10);
        request.setUserId(1L);

        // Mock the behavior of userRepository.findById()
        when(userRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            workoutPlanService.createWorkoutPlan(request);
        });
        assertEquals("User not found", thrown.getMessage());
    }
    @Test
    public void testMapToWorkoutPlanWithUser() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");
        userEntity.setEmail("testuser@example.com");
        userEntity.setRoles(Collections.singletonList("ROLE_USER").toString());

        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setId(1L);
        workoutPlanEntity.setName("Test Plan");
        workoutPlanEntity.setDescription("Test Description");
        workoutPlanEntity.setDurationInDays(30);
        workoutPlanEntity.setExercises(Collections.emptyList());
        workoutPlanEntity.setUser(userEntity);

        // Act
        WorkoutPlan workoutPlan = workoutPlanService.mapToWorkoutPlanWithUser(workoutPlanEntity);

        // Assert
        assertNotNull(workoutPlan);
        assertEquals(workoutPlanEntity.getId(), workoutPlan.getId());
        assertEquals(workoutPlanEntity.getName(), workoutPlan.getName());
        assertEquals(workoutPlanEntity.getDescription(), workoutPlan.getDescription());
        assertEquals(workoutPlanEntity.getDurationInDays(), workoutPlan.getDurationInDays());
        assertTrue(workoutPlan.getExercises().isEmpty());
        assertNotNull(workoutPlan.getUser());
        assertEquals(userEntity.getId(), workoutPlan.getUser().getId());
        assertEquals(userEntity.getUsername(), workoutPlan.getUser().getUsername());
        assertEquals(userEntity.getEmail(), workoutPlan.getUser().getEmail());
        assertEquals(userEntity.getRoles(), workoutPlan.getUser().getRoles());
    }
    @Test
    public void testMapToWorkoutPlan() {
        // Arrange
        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setId(1L);
        workoutPlanEntity.setName("Test Plan");
        workoutPlanEntity.setDescription("Test Description");
        workoutPlanEntity.setDurationInDays(30);

        ExerciseEntity exerciseEntity = new ExerciseEntity();
        exerciseEntity.setId(1L);
        exerciseEntity.setName("Test Exercise");
        exerciseEntity.setDescription("Test Exercise Description");
        exerciseEntity.setDurationInMinutes(60);
        exerciseEntity.setMuscleGroup("Legs");

        workoutPlanEntity.setExercises(Collections.singletonList(exerciseEntity));

        // Act
        WorkoutPlan workoutPlan = workoutPlanService.mapToWorkoutPlan(workoutPlanEntity);

        // Assert
        assertNotNull(workoutPlan);
        assertEquals(workoutPlanEntity.getId(), workoutPlan.getId());
        assertEquals(workoutPlanEntity.getName(), workoutPlan.getName());
        assertEquals(workoutPlanEntity.getDescription(), workoutPlan.getDescription());
        assertEquals(workoutPlanEntity.getDurationInDays(), workoutPlan.getDurationInDays());
        assertEquals(1, workoutPlan.getExercises().size());
        Exercise exercise = workoutPlan.getExercises().get(0);
        assertEquals(exerciseEntity.getId(), exercise.getId());
        assertEquals(exerciseEntity.getName(), exercise.getName());
        assertEquals(exerciseEntity.getDescription(), exercise.getDescription());
        assertEquals(exerciseEntity.getDurationInMinutes(), exercise.getDurationInMinutes());
        assertEquals(exerciseEntity.getMuscleGroup(), exercise.getMuscleGroup());
    }
    @Test
    public void testMapToExercise() {
        // Arrange
        ExerciseEntity exerciseEntity = new ExerciseEntity();
        exerciseEntity.setId(1L);
        exerciseEntity.setName("Test Exercise");
        exerciseEntity.setDescription("Test Exercise Description");
        exerciseEntity.setDurationInMinutes(60);
        exerciseEntity.setMuscleGroup("Legs");

        // Act
        Exercise exercise = workoutPlanService.mapToExercise(exerciseEntity);

        // Assert
        assertNotNull(exercise);
        assertEquals(exerciseEntity.getId(), exercise.getId());
        assertEquals(exerciseEntity.getName(), exercise.getName());
        assertEquals(exerciseEntity.getDescription(), exercise.getDescription());
        assertEquals(exerciseEntity.getDurationInMinutes(), exercise.getDurationInMinutes());
        assertEquals(exerciseEntity.getMuscleGroup(), exercise.getMuscleGroup());
    }
    @Test
    public void testMapToUser() {
        // Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUsername("testuser");
        userEntity.setEmail("testuser@example.com");
        userEntity.setRoles(Collections.singletonList("ROLE_USER").toString());

        // Act
        User user = workoutPlanService.mapToUser(userEntity);

        // Assert
        assertNotNull(user);
        assertEquals(userEntity.getId(), user.getId());
        assertEquals(userEntity.getUsername(), user.getUsername());
        assertEquals(userEntity.getEmail(), user.getEmail());
        assertEquals(userEntity.getRoles(), user.getRoles());
    }


}
