package fontys.demo.persistence.impl;

import fontys.demo.persistence.entity.ExerciseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseJPARepository extends JpaRepository<ExerciseEntity, Long> {
}
