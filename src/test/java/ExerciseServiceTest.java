import fontys.demo.Domain.CreateExerciseRequest;
import fontys.demo.Domain.CreateExerciseResponse;
import fontys.demo.Domain.Exercise;
import fontys.demo.Domain.UpdateExerciseRequest;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.impl.ExerciseJPARepository;
import fontys.demo.business.ExerciseService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceTest {

    @Mock
    private ExerciseJPARepository exerciseRepositoryMock;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
    public void testGetExercise() {
        // Arrange
        long exerciseId = 1L;
        ExerciseEntity mockEntity = new ExerciseEntity();
        mockEntity.setId(exerciseId);
        mockEntity.setName("Test Exercise");
        when(exerciseRepositoryMock.findById(exerciseId)).thenReturn(Optional.of(mockEntity));

        // Act
        Exercise result = exerciseService.getExercise(exerciseId);

        // Assert
        assertNotNull(result);
        assertEquals(mockEntity.getName(), result.getName());
    }

    @Test
    public void testGetExercises() {
        // Arrange
        List<ExerciseEntity> mockEntities = new ArrayList<>();
        mockEntities.add(new ExerciseEntity("Test Exercise 1", "Description 1", 30, "Legs"));
        mockEntities.add(new ExerciseEntity("Test Exercise 2", "Description 2", 45, "Arms"));
        when(exerciseRepositoryMock.findAll()).thenReturn(mockEntities);

        // Act
        List<Exercise> results = exerciseService.getExercises();

        // Assert
        assertNotNull(results);
        assertEquals(mockEntities.size(), results.size());
        assertEquals(mockEntities.get(0).getName(), results.get(0).getName());
        assertEquals(mockEntities.get(1).getName(), results.get(1).getName());
    }

    @Test
    public void testCreateExercise() {
        // Arrange
        CreateExerciseRequest request = new CreateExerciseRequest("Test Exercise", "Description", 30, "Legs");
        ExerciseEntity savedExerciseEntity = new ExerciseEntity(1L, request.getName(), request.getDescription(), request.getDurationInMinutes(), request.getMuscleGroup());
        when(exerciseRepositoryMock.save(any(ExerciseEntity.class))).thenReturn(savedExerciseEntity);

        // Act
        CreateExerciseResponse response = exerciseService.createExercise(request);

        // Assert
        assertNotNull(response);
        assertNotNull(response.getExerciseId());
    }

    @Test
    public void testUpdateExercise() {
        // Arrange
        long exerciseId = 1L;
        UpdateExerciseRequest request = new UpdateExerciseRequest("Updated Exercise", "Updated Description", 45, "Arms");
        ExerciseEntity existingEntity = new ExerciseEntity(exerciseId, "Old Exercise", "Old Description", 30, "Legs");
        when(exerciseRepositoryMock.findById(exerciseId)).thenReturn(Optional.of(existingEntity));

        // Act
        exerciseService.updateExercise(exerciseId, request);

        // Assert
        assertEquals(request.getName(), existingEntity.getName());
        assertEquals(request.getDescription(), existingEntity.getDescription());
        assertEquals(request.getDurationInMinutes(), existingEntity.getDurationInMinutes());
        assertEquals(request.getMuscleGroup(), existingEntity.getMuscleGroup());
        verify(exerciseRepositoryMock).save(existingEntity);
    }

    @Test
    public void testDeleteExercise() {
        // Arrange
        long exerciseId = 1L;

        // Act
        exerciseService.deleteExercise(exerciseId);

        // Assert
        verify(exerciseRepositoryMock).deleteById(exerciseId);
    }
}
