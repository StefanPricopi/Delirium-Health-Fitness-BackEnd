package fontys.demo.Controller;
import fontys.demo.Domain.*;
import fontys.demo.business.ExerciseService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/exercises")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ExerciseController {
    private final ExerciseService exerciseService;

    @GetMapping("/{id}")
    public ResponseEntity<GetExerciseResponse> getExercise(@PathVariable Long id) {
        Exercise exercise = exerciseService.getExercise(id);
        return ResponseEntity.ok(mapToResponse(exercise));
    }

    @GetMapping
    public ResponseEntity<List<GetExerciseResponse>> getAllExercises() {
        List<Exercise> exercises = exerciseService.getExercises();
        List<GetExerciseResponse> exerciseResponses = exercises.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(exerciseResponses);
    }


    @PostMapping
    public ResponseEntity<CreateExerciseResponse> createExercise(@RequestBody @Valid CreateExerciseRequest request) {
        CreateExerciseResponse response = exerciseService.createExercise(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateExercise(@PathVariable Long id, @RequestBody @Valid UpdateExerciseRequest request) {
        exerciseService.updateExercise(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        exerciseService.deleteExercise(id);
        return ResponseEntity.noContent().build();
    }

    private GetExerciseResponse mapToResponse(Exercise exercise) {
        return new GetExerciseResponse().builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                .durationInMinutes(exercise.getDurationInMinutes())
                .muscleGroup(exercise.getMuscleGroup())
                .build();
    }
}

