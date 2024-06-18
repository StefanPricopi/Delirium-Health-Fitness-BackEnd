package fontys.demo.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "workout_plan")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkoutPlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "duration_in_days")
    private int durationInDays;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExerciseEntity> exercises;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public WorkoutPlanEntity(String name, String description, int durationInDays) {
        this.name = name;
        this.description = description;
        this.durationInDays = durationInDays;
    }


    public WorkoutPlanEntity(Long id, String name, String description, int durationInDays) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationInDays = durationInDays;
    }
}
