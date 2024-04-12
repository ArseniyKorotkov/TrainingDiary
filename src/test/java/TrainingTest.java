import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.User;
import by.y_lab.p5controller.SelectController;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TrainingTest {

    private final SelectController selectController = SelectController.getInstance();


    @Test
     void addingExerciseInDiary() {
        User user = new User("a", "b", LocalDate.of(1111, 11, 11), "@.", LocalDate.now());
        selectController.createExercise(new Exercise("a", 5), user);
        selectController.addExerciseInDiary("a", 2, user);
        assertEquals(1, user.getDiary().getDiaryList().size());
    }

    @Test
    void creatingExercise() {
        User user = new User("a", "b", LocalDate.of(1111, 11, 11), "@.", LocalDate.now());
        selectController.createExercise(new Exercise("a", 5), user);
        assertEquals(1, user.getExercises().size());
    }

}
