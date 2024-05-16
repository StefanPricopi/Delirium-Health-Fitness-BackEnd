package fontys.demo.business;

import fontys.demo.Domain.*;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.impl.ExerciseJPARepository;
import fontys.demo.business.Exceptions.ExerciseNotFoundException;
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
        return optionalExerciseEntity.map(ExerciseConverter::convert)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found with ID: " + id));
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
        if (request.getName() == null) {
            throw new IllegalArgumentException("Exercise name cannot be null");
        }

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
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id)
                .orElseThrow(() -> new ExerciseNotFoundException("Exercise not found with ID: " + id));

        exerciseEntity.setName(request.getName());
        exerciseEntity.setDescription(request.getDescription());
        exerciseEntity.setDurationInMinutes(request.getDurationInMinutes());
        exerciseEntity.setMuscleGroup(request.getMuscleGroup());

        exerciseRepository.save(exerciseEntity);
    }

    @Override
    public void deleteExercise(Long id) {
        if (!exerciseRepository.existsById(id)) {
            throw new ExerciseNotFoundException("Exercise not found with ID: " + id);
        }
        exerciseRepository.deleteById(id);
    }

    public List<Exercise> getExercisesByWorkoutPlanId(Long workoutPlanId) {
        List<ExerciseEntity> exerciseEntities = exerciseRepository.findByWorkoutPlanId(workoutPlanId);
        return exerciseEntities.stream()
                .map(ExerciseConverter::convert)
                .collect(Collectors.toList());
    }
}
