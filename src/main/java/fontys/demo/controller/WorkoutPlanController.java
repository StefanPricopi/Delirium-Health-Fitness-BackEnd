package fontys.demo.controller;

import fontys.demo.business.UserDetailsImpl;
import fontys.demo.business.WorkoutPlanService;
import fontys.demo.domain.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/workout-plans")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class WorkoutPlanController {

    private final WorkoutPlanService workoutPlanService;

    @GetMapping
    public ResponseEntity<GetWorkoutPlanResponse> getWorkoutPlans() {
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlans();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-pt/{ptId}")
    public ResponseEntity<List<GetWorkoutPlansByPTResponse>> getWorkoutsByPT(@PathVariable Long ptId) {
        List<GetWorkoutPlansByPTResponse> responses = workoutPlanService.getWorkoutsByPT(ptId);
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_PT')")
    public ResponseEntity<CreateWorkoutPlanResponse> createWorkoutPlan(@RequestBody @Valid CreateWorkoutPlanRequest request) {
        CreateWorkoutPlanResponse response = workoutPlanService.createWorkoutPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PT')")
    public ResponseEntity<Void> updateWorkoutPlan(@PathVariable("id") long workoutPlanId,
                                                  @RequestBody @Valid UpdateWorkoutPlanRequest request,
                                                  Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        workoutPlanService.updateWorkoutPlan(workoutPlanId, request, userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PT')")
    public ResponseEntity<Void> deleteWorkoutPlan(@PathVariable("id") long workoutPlanId,
                                                  Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        Long userId = userDetails.getId();

        workoutPlanService.deleteWorkoutPlan(workoutPlanId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetWorkoutPlanResponse> getWorkoutPlanById(@PathVariable("id") long workoutPlanId) {
        GetWorkoutPlanResponse response = workoutPlanService.getWorkoutPlanById(workoutPlanId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/counts-by-user")
    @PreAuthorize("hasAuthority('ROLE_PT')")
    public ResponseEntity<List<WorkoutCountDTO>> getWorkoutCountsByUser() {
        List<WorkoutCountDTO> workoutCounts = workoutPlanService.countWorkoutsByUser();
        return ResponseEntity.ok(workoutCounts);
    }
}


