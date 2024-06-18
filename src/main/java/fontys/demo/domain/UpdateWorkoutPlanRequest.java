package fontys.demo.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateWorkoutPlanRequest {

    private  long id;
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @Positive
    @NumberFormat
    private int durationInDays;
    private List<Exercise> exercises = new ArrayList<>();
}
