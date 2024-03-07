package fontys.demo.Persistence.Entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExerciseEntity {

    private Long id;
    private String name;
    private String description;
    private int durationInMinutes;
    private String muscleGroup;
}
