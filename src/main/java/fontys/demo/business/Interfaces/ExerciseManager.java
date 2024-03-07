package fontys.demo.business.Interfaces;

import fontys.demo.Domain.CreateExerciseRequest;
import fontys.demo.Domain.CreateExerciseResponse;
import fontys.demo.Domain.Exercise;
import fontys.demo.Domain.UpdateExerciseRequest;

import java.util.List;

public interface ExerciseManager {
     Exercise getExercise(Long id);
     List<Exercise> getExercises();
     CreateExerciseResponse createExercise(CreateExerciseRequest request);
     void updateExercise(Long id, UpdateExerciseRequest request);
     void deleteExercise(Long id);
}
