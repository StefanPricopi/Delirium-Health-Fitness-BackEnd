package fontys.demo.Domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class GetWorkoutPlanResponse {

    private List<WorkoutPlan> workoutPlans;
    private boolean error;
    private String errorMessage;
}
