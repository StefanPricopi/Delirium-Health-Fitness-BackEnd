package fontys.demo.persistence.impl;

import fontys.demo.persistence.entity.WorkoutPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutplanJPARepository extends JpaRepository<WorkoutPlanEntity, Long> {
    @Query("SELECT w.user.id, COUNT(w) FROM WorkoutPlanEntity w WHERE w.user.id IS NOT NULL GROUP BY w.user.id")
    List<Object[]> countWorkoutsByUser();

    List<WorkoutPlanEntity> findAllByUserId(Long userId);
}



