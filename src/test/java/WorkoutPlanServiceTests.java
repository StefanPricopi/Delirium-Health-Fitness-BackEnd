import fontys.demo.Domain.*;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.Persistence.WorkoutPlanRepository;
import fontys.demo.business.WorkoutPlanService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


public class WorkoutPlanServiceTests {
    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @InjectMocks
    private WorkoutPlanService workoutPlanService;

    public WorkoutPlanServiceTests() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetWorkoutPlans() {
        WorkoutPlanEntity workoutPlanEntity = new WorkoutPlanEntity();
        workoutPlanEntity.setId(1L);
        workoutPlanEntity.setName("Beginner Workout");
        workoutPlanEntity.setDescription("A workout plan designed for beginners.");
        workoutPlanEntity.setDurationInDays(30);

        when(workoutPlanRepository.findAll()).thenReturn(Collections.singletonList(workoutPlanEntity));

        var response = workoutPlanService.getWorkoutPlans();

        assertEquals(1, response.getWorkoutPlans().size());
        WorkoutPlan workoutPlan = response.getWorkoutPlans().get(0);
        assertEquals(1L, workoutPlan.getId());
        assertEquals("Beginner Workout", workoutPlan.getName());
        assertEquals("A workout plan designed for beginners.", workoutPlan.getDescription());
        assertEquals(30, workoutPlan.getDurationInDays());

        verify(workoutPlanRepository, times(1)).findAll();
    }

    @Test
    public void testCreateWorkoutPlan() {
        // Mock
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest();
        request.setName("Test Workout Plan");
        request.setDescription("Test Description");
        request.setDurationInDays(7);

        // Mock
        WorkoutPlanEntity mockEntity = new WorkoutPlanEntity();
        mockEntity.setId(1L);
        mockEntity.setName("Test Workout Plan");
        mockEntity.setDescription("Test Description");
        mockEntity.setDurationInDays(7);
        when(workoutPlanRepository.save(any(WorkoutPlanEntity.class))).thenReturn(mockEntity);

        // Invoke service method
        WorkoutPlanService service = new WorkoutPlanService(workoutPlanRepository);
        CreateWorkoutPlanResponse response = service.createWorkoutPlan(request);

        // Assertions
        assertNotNull(response);
        assertNotNull(response.getWorkoutPlan());
        assertEquals("Test Workout Plan", response.getWorkoutPlan().getName());
        assertEquals("Test Description", response.getWorkoutPlan().getDescription());
        assertEquals(7, response.getWorkoutPlan().getDurationInDays());

    }
    @Test
    public void testGetNonExistentWorkoutPlan() {
        // Mock
        when(workoutPlanRepository.existsById(1L)).thenReturn(false);

        // Invoke service method
        WorkoutPlanService service = new WorkoutPlanService(workoutPlanRepository);
        GetWorkoutPlanResponse response = service.getWorkoutPlanById(1L);

        // Assertions
        assertNotNull(response);
        assertNull(response.getWorkoutPlans());
    }
    @Test
    public void testCreateWorkoutPlanWithError() {
        // Mock
        when(workoutPlanRepository.save(any(WorkoutPlanEntity.class))).thenThrow(new RuntimeException("Error saving workout plan"));

        // Invoke service method
        WorkoutPlanService service = new WorkoutPlanService(workoutPlanRepository);
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest("Test Plan", "Test Description", 7, null);

        // Assertion
        assertThrows(RuntimeException.class, () -> {
            service.createWorkoutPlan(request);
        });
    }
    @Test
    public void testDeleteExistingWorkoutPlan() {
        // Mock
        when(workoutPlanRepository.existsById(1L)).thenReturn(true);

        // Invoke service method
        WorkoutPlanService service = new WorkoutPlanService(workoutPlanRepository);
        boolean deleted = service.deleteWorkoutPlan(1L);

        // Assertion
        assertTrue(deleted);
    }











}
