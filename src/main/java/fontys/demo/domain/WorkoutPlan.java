package fontys.demo.domain;

import fontys.demo.domain.userDomain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WorkoutPlan {
    private Long id;
    private String name;
    private String description;
    private int durationInDays;
    private User user;
    private List<Exercise> exercises;
}
