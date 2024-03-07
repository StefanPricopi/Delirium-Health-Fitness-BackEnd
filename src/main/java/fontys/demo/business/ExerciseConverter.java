package fontys.demo.business;

import fontys.demo.Domain.Exercise;
import fontys.demo.Persistence.Entity.ExerciseEntity;

import java.util.List;
import java.util.stream.Collectors;

public class ExerciseConverter {
    public static Exercise convert(ExerciseEntity exerciseEntity) {
        return Exercise.builder()
                .id(exerciseEntity.getId())
                .name(exerciseEntity.getName())
                .description(exerciseEntity.getDescription())
                .durationInMinutes(exerciseEntity.getDurationInMinutes())
                .muscleGroup(exerciseEntity.getMuscleGroup())
                .build();
    }

    public static ExerciseEntity convert(Exercise exercise) {
        return ExerciseEntity.builder()
                .id(exercise.getId())
                .name(exercise.getName())
                .description(exercise.getDescription())
                .durationInMinutes(exercise.getDurationInMinutes())
                .muscleGroup(exercise.getMuscleGroup())
                .build();
    }

    public static List<Exercise> convertEntityList(List<ExerciseEntity> exerciseEntities) {
        return exerciseEntities.stream()
                .map(ExerciseConverter::convert)
                .collect(Collectors.toList());
    }

    public static List<ExerciseEntity> convertList(List<Exercise> exercises) {
        return exercises.stream()
                .map(ExerciseConverter::convert)
                .collect(Collectors.toList());
    }
}
