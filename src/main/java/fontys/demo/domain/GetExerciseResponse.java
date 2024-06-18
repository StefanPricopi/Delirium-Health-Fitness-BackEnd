package fontys.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetExerciseResponse {
    private Long id;
    private String name;
    private String description;
    private int durationInMinutes;
    private String muscleGroup;
}
