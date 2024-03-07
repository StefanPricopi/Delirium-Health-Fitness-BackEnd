package fontys.demo.Persistence;

import fontys.demo.Persistence.Entity.ExerciseEntity;

import java.util.List;

public interface ExerciseRepository {
     boolean existsById(Long id);
     ExerciseEntity findById(Long id);
     ExerciseEntity save(ExerciseEntity exercise);
     List<ExerciseEntity> findAll();
     void deleteById(Long id);
     int count();
}
