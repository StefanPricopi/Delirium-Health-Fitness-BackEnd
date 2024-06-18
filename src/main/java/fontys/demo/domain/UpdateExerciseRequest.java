package fontys.demo.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateExerciseRequest {

        @NotBlank
        private String name;

        private String description;

        @Positive
        private int durationInMinutes;

        @NotBlank
        private String muscleGroup;
}
