package fontys.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanResponse {
    private Long id;
    private String name;
    private String description;
    private int durationInDays;
}
