package fontys.demo.Domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CreateWorkoutPlanResponse {

        private WorkoutPlan workoutPlan;
        private boolean error;
        private String errorMessage;
}
