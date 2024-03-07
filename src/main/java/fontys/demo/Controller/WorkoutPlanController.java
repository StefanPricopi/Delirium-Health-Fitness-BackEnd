package fontys.demo.Controller;

import fontys.demo.business.WorkoutPlanService;
import fontys.demo.Domain.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/workout-plans")
@AllArgsConstructor
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @GetMapping
    public ResponseEntity<GetWorkoutPlanResponse> getWorkoutPlans() {
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlans();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateWorkoutPlanResponse> createWorkoutPlan(@RequestBody @Valid CreateWorkoutPlanRequest request) {
        CreateWorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateWorkoutPlan(@PathVariable("id") long workoutPlanId, @RequestBody @Valid UpdateWorkoutPlanRequest request) {
        workoutPlanService.updateWorkoutPlan(workoutPlanId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable("id") long workoutPlanId) {
        workoutPlanService.deleteWorkoutPlan(workoutPlanId);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<GetWorkoutPlanResponse> getWorkoutPlanById(@PathVariable("id") long workoutPlanId) {
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(workoutPlanId);
        return ResponseEntity.ok(response);
    }
}
