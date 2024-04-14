import by.y_lab.p0util.FormatDateTime;
import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;
import by.y_lab.p3dto.ExerciseDto;
import by.y_lab.p3dto.UserDto;
import by.y_lab.p4service.DiaryService;
import by.y_lab.p5controller.DiaryController;
import by.y_lab.p5controller.ExerciseController;
import by.y_lab.p5controller.LoginController;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;


import java.time.LocalDate;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DiaryTest {

    private static final String testUserFirstname = "first";
    private static final String testUserLastname = "last";
    private static final String testUserBirthday = "11.11.2020";
    private static final String testUserEmail = "@.";
    private static final String testExerciseName = "run";
    private static final int testExerciseBurnCalories = 5;
    private final LoginController loginController = LoginController.getInstance();
    private final DiaryService diaryService = DiaryService.getInstance();
    private final DiaryController diaryController = DiaryController.getInstance();
    private final ExerciseController exerciseController = ExerciseController.getInstance();
    private static User user;
    private ExerciseDto exerciseDto = new ExerciseDto("a", 5);

    @BeforeAll
    void init() {
        user = loginController.createUser(
                new UserDto(testUserFirstname, testUserLastname, testUserBirthday, testUserEmail)).get();
        exerciseDto = new ExerciseDto(testExerciseName, testExerciseBurnCalories);
    }

    @Test
    void addingExerciseInDiary() {
        Exercise exercise = exerciseController
                .createExercise(user, exerciseDto.getExerciseName(), exerciseDto.getCaloriesBurnInHour());
        diaryController.addExercise(user, exercise, 2);
        assertEquals(1, diaryService.getDiaryList(user).size(),
                "размер списка тренировок не увеличен на единицу после попытки добавления");
    }

    @Test
    void showTrainingTimeSliceTest() {
        Exercise exercise = exerciseController
                .createExercise(user, exerciseDto.getExerciseName(), exerciseDto.getCaloriesBurnInHour());
        diaryController.addExercise(user, exercise, 2);
        List<TreeSet<NoteExerciseInDiary>> diaryInDays = diaryController
                .getDiaryTimeSlice(LocalDate.now().minusDays(5).format(FormatDateTime.reformDate()) + "/"
                                   + LocalDate.now().plusDays(3).format(FormatDateTime.reformDate()), user);
        assertEquals(1, diaryInDays.get(0).size(), "тренировка не обнаружена при поиске в разрезе времени");
    }
}
