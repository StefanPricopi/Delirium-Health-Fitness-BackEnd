package fontys.demo.Persistence.impl;

import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutplanJPARepository extends JpaRepository<WorkoutPlanEntity, Long> {
    @Query("SELECT w.user.id, COUNT(w) FROM WorkoutPlanEntity w WHERE w.user.id IS NOT NULL GROUP BY w.user.id")
    List<Object[]> countWorkoutsByUser();
    List<WorkoutPlanEntity> findByUserId(Long userId);


}
