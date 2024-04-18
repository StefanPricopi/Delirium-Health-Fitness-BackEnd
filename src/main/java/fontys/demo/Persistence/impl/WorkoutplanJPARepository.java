package fontys.demo.Persistence.impl;

import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutplanJPARepository extends JpaRepository<WorkoutPlanEntity, Long> {
}
