package fontys.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetWorkoutPlanResponse {

    private List<WorkoutPlan> workoutPlans;
    private boolean error;
    private String errorMessage;
}
