package fontys.demo.Persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanEntity {
    private Long id;
    private String name;
    private String description;
    private int durationInDays;
    private List<ExerciseEntity> exercises;
}
