package service;

import fontys.demo.domain.CreateExerciseRequest;
import fontys.demo.domain.CreateExerciseResponse;
import fontys.demo.domain.Exercise;
import fontys.demo.domain.UpdateExerciseRequest;
import fontys.demo.persistence.entity.ExerciseEntity;
import fontys.demo.persistence.impl.ExerciseJPARepository;
import fontys.demo.business.ExerciseService;
import fontys.demo.business.exceptions.ExerciseNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class ExerciseServiceTest {

    @Mock
    private ExerciseJPARepository exerciseRepositoryMock;

    @InjectMocks
    private ExerciseService exerciseService;

    @Test
     void testGetExercise() {
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
     void testGetExercise_NotFound() {
        // Arrange
        long exerciseId = 1L;
        when(exerciseRepositoryMock.findById(exerciseId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ExerciseNotFoundException.class, () -> exerciseService.getExercise(exerciseId));
        assertEquals("Exercise not found with ID: " + exerciseId, exception.getMessage());
    }

    @Test
     void testGetExercises() {
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
     void testGetExercises_Empty() {
        // Arrange
        when(exerciseRepositoryMock.findAll()).thenReturn(new ArrayList<>());

        // Act
        List<Exercise> results = exerciseService.getExercises();

        // Assert
        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
     void testCreateExercise() {
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
     void testCreateExercise_NullName() {
        // Arrange
        CreateExerciseRequest request = new CreateExerciseRequest(null, "Description", 30, "Legs");

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () -> exerciseService.createExercise(request));
        assertEquals("Exercise name cannot be null", exception.getMessage());
    }

    @Test
     void testUpdateExercise() {
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
     void testUpdateExercise_NotFound() {
        // Arrange
        long exerciseId = 1L;
        UpdateExerciseRequest request = new UpdateExerciseRequest("Updated Exercise", "Updated Description", 45, "Arms");
        when(exerciseRepositoryMock.findById(exerciseId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ExerciseNotFoundException.class, () -> exerciseService.updateExercise(exerciseId, request));
        assertEquals("Exercise not found with ID: " + exerciseId, exception.getMessage());
    }

    @Test
     void testDeleteExercise() {
        // Arrange
        long exerciseId = 1L;
        when(exerciseRepositoryMock.existsById(exerciseId)).thenReturn(true);

        // Act
        exerciseService.deleteExercise(exerciseId);

        // Assert
        verify(exerciseRepositoryMock).deleteById(exerciseId);
    }

    @Test
     void testDeleteExercise_NotFound() {
        // Arrange
        long exerciseId = 1L;
        when(exerciseRepositoryMock.existsById(exerciseId)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(ExerciseNotFoundException.class, () -> exerciseService.deleteExercise(exerciseId));
        assertEquals("Exercise not found with ID: " + exerciseId, exception.getMessage());
    }
}
