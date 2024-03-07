package fontys.demo.Domain;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateExerciseRequest {
    @NotBlank
    private String name;

    private String description;

    @Positive
    private int durationInMinutes;

    @NotBlank
    private String muscleGroup;

    // Constructors, getters, and setters...
}
