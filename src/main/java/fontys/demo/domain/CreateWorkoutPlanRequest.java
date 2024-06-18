package fontys.demo.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CreateWorkoutPlanRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Positive
    @NumberFormat
    private int durationInDays;
    private Long userId;

    private List<Exercise> exercises;
}
