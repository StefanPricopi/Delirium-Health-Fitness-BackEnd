package fontys.demo.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateExerciseResponse {
    private Long id;
    private String name;
    private String description;
    private int durationInMinutes;
    private String muscleGroup;
}
