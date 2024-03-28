package fontys.demo.business.Exceptions;

public class WorkoutPlanNotFoundException extends RuntimeException {
    public WorkoutPlanNotFoundException(String message) {
        super(message);
    }
}
