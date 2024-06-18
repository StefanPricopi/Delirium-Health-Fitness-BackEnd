package fontys.demo.business;

import fontys.demo.business.exceptions.ExerciseNotFoundException;
import fontys.demo.business.interfaces.ExerciseManager;
import fontys.demo.domain.CreateExerciseRequest;
import fontys.demo.domain.CreateExerciseResponse;
import fontys.demo.domain.Exercise;
import fontys.demo.domain.UpdateExerciseRequest;
import fontys.demo.persistence.entity.ExerciseEntity;
import fontys.demo.persistence.impl.ExerciseJPARepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ExerciseService implements ExerciseManager {

    private static final String EXERCISE_NOT_FOUND_MSG = "Exercise not found with ID: ";

    private final ExerciseJPARepository exerciseRepository;

    @Override
    public Exercise getExercise(Long id) {
        Optional<ExerciseEntity> optionalExerciseEntity = exerciseRepository.findById(id);
        return optionalExerciseEntity.map(ExerciseConverter::convert)
                .orElseThrow(() -> new ExerciseNotFoundException(EXERCISE_NOT_FOUND_MSG + id));
    }

    @Override
    public List<Exercise> getExercises() {
        List<ExerciseEntity> exerciseEntities = exerciseRepository.findAll();
        return exerciseEntities.stream()
                .map(ExerciseConverter::convert)
                .toList();
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
                .orElseThrow(() -> new ExerciseNotFoundException(EXERCISE_NOT_FOUND_MSG + id));

        exerciseEntity.setName(request.getName());
        exerciseEntity.setDescription(request.getDescription());
        exerciseEntity.setDurationInMinutes(request.getDurationInMinutes());
        exerciseEntity.setMuscleGroup(request.getMuscleGroup());

        exerciseRepository.save(exerciseEntity);
    }

    @Override
    public void deleteExercise(Long id) {
        if (!exerciseRepository.existsById(id)) {
            throw new ExerciseNotFoundException(EXERCISE_NOT_FOUND_MSG + id);
        }
        exerciseRepository.deleteById(id);
    }

}
