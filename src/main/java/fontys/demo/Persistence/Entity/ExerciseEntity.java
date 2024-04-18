package fontys.demo.Persistence.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "exercise")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "duration_in_minutes")
    private int durationInMinutes;

    @Column(name = "muscle_group")
    private String muscleGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_plan_id")
    private WorkoutPlanEntity workoutPlan;

    public ExerciseEntity(String name, String description, int durationInMinutes, String muscleGroup) {
        this.name = name;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.muscleGroup = muscleGroup;
    }
    public ExerciseEntity(Long id, String name, String description, int durationInMinutes, String muscleGroup) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationInMinutes = durationInMinutes;
        this.muscleGroup = muscleGroup;
    }
}
