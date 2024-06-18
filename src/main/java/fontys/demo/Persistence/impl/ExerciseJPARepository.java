package fontys.demo.Persistence.impl;

import fontys.demo.Persistence.Entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseJPARepository extends JpaRepository<ExerciseEntity, Long> {
}
