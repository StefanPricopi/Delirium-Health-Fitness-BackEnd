package fontys.demo.business.exceptions;

public class WorkoutPlanNotFoundException extends RuntimeException {
    public WorkoutPlanNotFoundException(String message) {
        super(message);
    }
}
