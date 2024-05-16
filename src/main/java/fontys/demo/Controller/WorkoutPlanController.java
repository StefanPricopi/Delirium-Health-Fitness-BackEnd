package fontys.demo.Controller;

import fontys.demo.business.WorkoutPlanService;
import fontys.demo.Domain.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/workout-plans")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @GetMapping
    public ResponseEntity<GetWorkoutPlanResponse> getWorkoutPlans() {
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlans();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PT')")
    public ResponseEntity<CreateWorkoutPlanResponse> createWorkoutPlan(@RequestBody @Valid CreateWorkoutPlanRequest request) {
        CreateWorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PT')")
    public ResponseEntity<Void> updateWorkoutPlan(@PathVariable("id") long workoutPlanId, @RequestBody @Valid UpdateWorkoutPlanRequest request) {
        workoutPlanService.updateWorkoutPlan(workoutPlanId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PT')")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable("id") long workoutPlanId) {
        boolean deleted = workoutPlanService.deleteWorkoutPlan(workoutPlanId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetWorkoutPlanResponse> getWorkoutPlanById(@PathVariable("id") long workoutPlanId) {
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(workoutPlanId);
        return ResponseEntity.ok(response);
    }
}

