
import fontys.demo.Main;
import fontys.demo.Persistence.Entity.ExerciseEntity;
import fontys.demo.Persistence.Entity.WorkoutPlanEntity;
import fontys.demo.Persistence.impl.ExerciseJPARepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)

public class ExerciseRepositoryIntegrationTest {

    @Autowired
    private ExerciseJPARepository exerciseRepository;

    @BeforeEach
    void setUp() {
        exerciseRepository.deleteAll();
        exerciseRepository.flush();
    }

    @Test
    public void testFindAll() {
        ExerciseEntity exercise1 = new ExerciseEntity("Exercise 3", "Description 3", 30, "Core");
        ExerciseEntity exercise2 = new ExerciseEntity("Exercise 4", "Description 4", 45, "Back");
        exerciseRepository.save(exercise1);
        exerciseRepository.save(exercise2);

        List<ExerciseEntity> allExercises = exerciseRepository.findAll();

        assertEquals(2, allExercises.size());
    }

    @Test
    public void testSaveExercise() {
        // Given
        ExerciseEntity exercise = new ExerciseEntity("Exercise 6", "Description 6", 45, "Yoga");

        // When
        ExerciseEntity savedExercise = exerciseRepository.save(exercise);

        // Then
        assertNotNull(savedExercise.getId());
        assertEquals("Exercise 6", savedExercise.getName());
    }
    @Test
    public void testDeleteExercise() {
        // Given
        ExerciseEntity exercise = new ExerciseEntity("Exercise 7", "Description 7", 20, "Stretching");
        ExerciseEntity savedExercise = exerciseRepository.save(exercise);

        // When
        exerciseRepository.delete(savedExercise);
        Optional<ExerciseEntity> deletedExercise = exerciseRepository.findById(savedExercise.getId());

        // Then
        assertFalse(deletedExercise.isPresent());
    }


}

