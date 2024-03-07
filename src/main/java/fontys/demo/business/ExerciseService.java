package fontys.demo.business;

import fontys.demo.Domain.*;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.ExerciseRepository;
import fontys.demo.business.Interfaces.ExerciseManager;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ExerciseService implements ExerciseManager {
    private final ExerciseRepository exerciseRepository;



    public Exercise getExercise(Long id) {
        ExerciseEntity exerciseEntity = exerciseRepository.findById(id);
        return ExerciseConverter.convert(exerciseEntity);

    }

    public List<Exercise> getExercises() {
        List<ExerciseEntity> exerciseEntities = exerciseRepository.findAll();
        return ExerciseConverter.convertEntityList(exerciseEntities);
        }

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

    public void updateExercise(Long id, UpdateExerciseRequest request) {
        ExerciseEntity exercise = exerciseRepository.findById(id) ;


        exercise.setName(request.getName());
        exercise.setDescription(request.getDescription());
        exercise.setDurationInMinutes(request.getDurationInMinutes());
        exercise.setMuscleGroup(request.getMuscleGroup());

        exerciseRepository.save(exercise);
    }

    public void deleteExercise(Long id) {
//        if (!exerciseRepository.existsById(id)) {
//            throw new ExerciseNotFoundException("Exercise with id " + id + " not found");
//    }
        exerciseRepository.deleteById(id);
    }
}
