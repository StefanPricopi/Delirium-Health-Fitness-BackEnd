package fontys.demo.Domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Exercise {
    private Long id;
    private String name;
    private String description;
    private int durationInMinutes;
    private String muscleGroup;

    public Exercise(Long id, String name, String description) {
    }
}
