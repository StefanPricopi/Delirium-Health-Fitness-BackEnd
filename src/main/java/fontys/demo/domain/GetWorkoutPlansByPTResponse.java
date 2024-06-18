package fontys.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetWorkoutPlansByPTResponse {

    private Long id;
    private String name;
    private String description;
    private int durationInDays;
    private List<GetExerciseResponse> exercises;
}
