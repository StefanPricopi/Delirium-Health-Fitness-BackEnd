

import fontys.demo.Domain.Exercise;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.business.ExerciseConverter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExerciseConverterTest {

    @Test
    public void testConvertEntityToDomain() {
        // Arrange
        ExerciseEntity exerciseEntity = ExerciseEntity.builder()
                .id(1L)
                .name("Test Exercise")
                .description("Test Description")
                .durationInMinutes(30)
                .muscleGroup("Legs")
                .build();

        // Act
        Exercise exercise = ExerciseConverter.convert(exerciseEntity);

        // Assert
        assertNotNull(exercise);
        assertEquals(exerciseEntity.getId(), exercise.getId());
        assertEquals(exerciseEntity.getName(), exercise.getName());
        assertEquals(exerciseEntity.getDescription(), exercise.getDescription());
        assertEquals(exerciseEntity.getDurationInMinutes(), exercise.getDurationInMinutes());
        assertEquals(exerciseEntity.getMuscleGroup(), exercise.getMuscleGroup());
    }

    @Test
    public void testConvertDomainToEntity() {
        // Arrange
        Exercise exercise = Exercise.builder()
                .id(1L)
                .name("Test Exercise")
                .description("Test Description")
                .durationInMinutes(30)
                .muscleGroup("Legs")
                .build();

        // Act
        ExerciseEntity exerciseEntity = ExerciseConverter.convert(exercise);

        // Assert
        assertNotNull(exerciseEntity);
        assertEquals(exercise.getId(), exerciseEntity.getId());
        assertEquals(exercise.getName(), exerciseEntity.getName());
        assertEquals(exercise.getDescription(), exerciseEntity.getDescription());
        assertEquals(exercise.getDurationInMinutes(), exerciseEntity.getDurationInMinutes());
        assertEquals(exercise.getMuscleGroup(), exerciseEntity.getMuscleGroup());
    }

    @Test
    public void testConvertEntityList() {
        // Arrange
        ExerciseEntity exerciseEntity1 = ExerciseEntity.builder()
                .id(1L)
                .name("Test Exercise 1")
                .description("Test Description 1")
                .durationInMinutes(30)
                .muscleGroup("Legs")
                .build();

        ExerciseEntity exerciseEntity2 = ExerciseEntity.builder()
                .id(2L)
                .name("Test Exercise 2")
                .description("Test Description 2")
                .durationInMinutes(45)
                .muscleGroup("Arms")
                .build();

        List<ExerciseEntity> exerciseEntities = Arrays.asList(exerciseEntity1, exerciseEntity2);

        // Act
        List<Exercise> exercises = ExerciseConverter.convertEntityList(exerciseEntities);

        // Assert
        assertNotNull(exercises);
        assertEquals(2, exercises.size());
        assertEquals(exerciseEntity1.getId(), exercises.get(0).getId());
        assertEquals(exerciseEntity1.getName(), exercises.get(0).getName());
        assertEquals(exerciseEntity1.getDescription(), exercises.get(0).getDescription());
        assertEquals(exerciseEntity1.getDurationInMinutes(), exercises.get(0).getDurationInMinutes());
        assertEquals(exerciseEntity1.getMuscleGroup(), exercises.get(0).getMuscleGroup());
        assertEquals(exerciseEntity2.getId(), exercises.get(1).getId());
        assertEquals(exerciseEntity2.getName(), exercises.get(1).getName());
        assertEquals(exerciseEntity2.getDescription(), exercises.get(1).getDescription());
        assertEquals(exerciseEntity2.getDurationInMinutes(), exercises.get(1).getDurationInMinutes());
        assertEquals(exerciseEntity2.getMuscleGroup(), exercises.get(1).getMuscleGroup());
    }

    @Test
    public void testConvertList() {
        // Arrange
        Exercise exercise1 = Exercise.builder()
                .id(1L)
                .name("Test Exercise 1")
                .description("Test Description 1")
                .durationInMinutes(30)
                .muscleGroup("Legs")
                .build();

        Exercise exercise2 = Exercise.builder()
                .id(2L)
                .name("Test Exercise 2")
                .description("Test Description 2")
                .durationInMinutes(45)
                .muscleGroup("Arms")
                .build();

        List<Exercise> exercises = Arrays.asList(exercise1, exercise2);

        // Act
        List<ExerciseEntity> exerciseEntities = ExerciseConverter.convertList(exercises);

        // Assert
        assertNotNull(exerciseEntities);
        assertEquals(2, exerciseEntities.size());
        assertEquals(exercise1.getId(), exerciseEntities.get(0).getId());
        assertEquals(exercise1.getName(), exerciseEntities.get(0).getName());
        assertEquals(exercise1.getDescription(), exerciseEntities.get(0).getDescription());
        assertEquals(exercise1.getDurationInMinutes(), exerciseEntities.get(0).getDurationInMinutes());
        assertEquals(exercise1.getMuscleGroup(), exerciseEntities.get(0).getMuscleGroup());
        assertEquals(exercise2.getId(), exerciseEntities.get(1).getId());
        assertEquals(exercise2.getName(), exerciseEntities.get(1).getName());
        assertEquals(exercise2.getDescription(), exerciseEntities.get(1).getDescription());
        assertEquals(exercise2.getDurationInMinutes(), exerciseEntities.get(1).getDurationInMinutes());
        assertEquals(exercise2.getMuscleGroup(), exerciseEntities.get(1).getMuscleGroup());
    }
}
