import fontys.demo.Domain.*;
import fontys.demo.Main;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.business.WorkoutPlanService;
import fontys.demo.Persistence.impl.WorkoutplanJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
public class WorkoutplanIntegrationTest {

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
    public void testGetWorkoutPlans() {
        // Given
        WorkoutPlanEntity workoutPlan1 = new WorkoutPlanEntity("Plan for beginers", "Ideal plan to get your journey started", 30);
        WorkoutPlanEntity workoutPlan2 = new WorkoutPlanEntity("Plan for advanced", "Wanna be next mr Olimp, greek gods envy you", 45);
        workoutplanJPARepository.save(workoutPlan1);
        workoutplanJPARepository.save(workoutPlan2);

        // When
        List<WorkoutPlan> foundWorkoutPlans = workoutPlanService.getWorkoutPlans().getWorkoutPlans();

        // Then
        assertEquals(2, foundWorkoutPlans.size());
        assertEquals("Plan for beginers", foundWorkoutPlans.get(0).getName());
        assertEquals("Ideal plan to get your journey started", foundWorkoutPlans.get(0).getDescription());
        assertEquals("Plan for advanced", foundWorkoutPlans.get(1).getName());
        assertEquals("Wanna be next mr Olimp, greek gods envy you", foundWorkoutPlans.get(1).getDescription());
    }
    @Test

    public void testCreateWorkoutPlan() {
        List<Exercise> exercises = Collections.emptyList();
        CreateWorkoutPlanRequest request = new CreateWorkoutPlanRequest("Plan C", "Description C", 60, exercises);

        // When
        CreateWorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(request);

        // Then
        assertNotNull(response.getWorkoutPlan());
        assertEquals("Plan C", response.getWorkoutPlan().getName());
        assertEquals("Description C", response.getWorkoutPlan().getDescription());
    }

    @Test
    public void testUpdateWorkoutPlan() {
        // Given
        WorkoutPlanEntity existingPlan = new WorkoutPlanEntity("Plan D", "Old Description", 30);
        WorkoutPlanEntity savedPlan = workoutplanJPARepository.save(existingPlan);
        List<Exercise> exercises = Collections.emptyList();
        UpdateWorkoutPlanRequest updateRequest = new UpdateWorkoutPlanRequest(
                savedPlan.getId(), "Updated Plan D", "Updated Description", 35, exercises);

        // When
        workoutPlanService.updateWorkoutPlan(savedPlan.getId(), updateRequest);

        // Then
        Optional<WorkoutPlanEntity> updatedPlan = workoutplanJPARepository.findById(savedPlan.getId());
        assertTrue(updatedPlan.isPresent());
        assertEquals("Updated Plan D", updatedPlan.get().getName());
        assertEquals("Updated Description", updatedPlan.get().getDescription());
    }
    @Test
    public void testDeleteWorkoutPlan() {
        // Given
        WorkoutPlanEntity workoutPlan = new WorkoutPlanEntity("Plan E", "Description E", 50);
        WorkoutPlanEntity savedPlan = workoutplanJPARepository.save(workoutPlan);

        // When
        boolean result = workoutPlanService.deleteWorkoutPlan(savedPlan.getId());

        // Then
        assertTrue(result);
        assertFalse(workoutplanJPARepository.existsById(savedPlan.getId()));
    }

}