package fontys.demo.business.interfaces;

import fontys.demo.domain.CreateExerciseRequest;
import fontys.demo.domain.CreateExerciseResponse;
import fontys.demo.domain.Exercise;
import fontys.demo.domain.UpdateExerciseRequest;

import java.util.List;

public interface ExerciseManager {
     Exercise getExercise(Long id);
     List<Exercise> getExercises();
     CreateExerciseResponse createExercise(CreateExerciseRequest request);
     void updateExercise(Long id, UpdateExerciseRequest request);
     void deleteExercise(Long id);
}
