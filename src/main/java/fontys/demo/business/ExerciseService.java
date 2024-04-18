package fontys.demo.business;

import fontys.demo.Domain.*;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.impl.ExerciseJPARepository;
import fontys.demo.business.Interfaces.ExerciseManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class ExerciseService implements ExerciseManager {

    private final ExerciseJPARepository exerciseRepository;

    @Override
    public Exercise getExercise(Long id) {
        Optional<ExerciseEntity> optionalExerciseEntity = exerciseRepository.findById(id);
        return optionalExerciseEntity.map(ExerciseConverter::convert).orElse(null);
    }

    @Override
    public List<Exercise> getExercises() {
        List<ExerciseEntity> exerciseEntities = exerciseRepository.findAll();
        return exerciseEntities.stream()
                .map(ExerciseConverter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public CreateExerciseResponse createExercise(CreateExerciseRequest request) {
        ExerciseEntity newExercise = ExerciseEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .durationInMinutes(request.getDurationInMinutes())
                .muscleGroup(request.getMuscleGroup())
                .build();

        ExerciseEntity savedExercise = exerciseRepository.save(newExercise);

        return CreateExerciseResponse.builder()
                .exerciseId(savedExercise.getId())
                .build();
    }

    @Override
    public void updateExercise(Long id, UpdateExerciseRequest request) {
        Optional<ExerciseEntity> optionalExerciseEntity = exerciseRepository.findById(id);
        optionalExerciseEntity.ifPresent(exercise -> {
            exercise.setName(request.getName());
            exercise.setDescription(request.getDescription());
            exercise.setDurationInMinutes(request.getDurationInMinutes());
            exercise.setMuscleGroup(request.getMuscleGroup());
            exerciseRepository.save(exercise);
        });
    }

    @Override
    public void deleteExercise(Long id) {
        exerciseRepository.deleteById(id);
    }
    public List<Exercise> getExercisesByWorkoutPlanId(Long workoutPlanId) {
        List<ExerciseEntity> exerciseEntities = exerciseRepository.findByWorkoutPlanId(workoutPlanId);
        return exerciseEntities.stream()
                .map(ExerciseConverter::convert)
                .collect(Collectors.toList());
    }
}
