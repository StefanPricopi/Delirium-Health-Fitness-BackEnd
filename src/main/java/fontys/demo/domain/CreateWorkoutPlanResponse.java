package fontys.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateWorkoutPlanResponse {

        private WorkoutPlan workoutPlan;
        private boolean error;
        private String errorMessage;
}
