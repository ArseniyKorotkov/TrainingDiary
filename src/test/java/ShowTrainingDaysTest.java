import by.y_lab.p0util.FormatDate;
import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;
import by.y_lab.p5controller.SelectController;
import by.y_lab.p5controller.ShowDiaryController;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

public class ShowTrainingDaysTest {

    private final ShowDiaryController showDiaryController = ShowDiaryController.getInstance();
    private final SelectController selectController = SelectController.getInstance();

    @Test
    void showTrainingDaysTest() {
        User user = new User("a", "b", LocalDate.of(1111, 11, 11), "@.", LocalDate.now());
        selectController.createExercise(new Exercise("a", 5), user);
        selectController.addExerciseInDiary("a", 2, user);
        List<TreeSet<NoteExerciseInDiary>> diaryInDays = showDiaryController
                .getDiaryInDays(LocalDate.now().minusDays(5).format(FormatDate.reform()) + "/"
                                + LocalDate.now().plusDays(3).format(FormatDate.reform()), user);
        assertEquals(2, diaryInDays.get(0).size());
    }
}
