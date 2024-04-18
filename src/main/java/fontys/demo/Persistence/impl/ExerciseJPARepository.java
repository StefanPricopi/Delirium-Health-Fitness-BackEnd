package fontys.demo.Persistence.impl;

import fontys.demo.Persistence.Entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseJPARepository extends JpaRepository<ExerciseEntity, Long> {
    List<ExerciseEntity> findByWorkoutPlanId(Long workoutPlanId);
}
