import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.User;
import by.y_lab.p3dto.ExerciseDto;
import by.y_lab.p3dto.UserDto;
import by.y_lab.p5controller.AdminController;
import by.y_lab.p5controller.ExerciseController;
import by.y_lab.p5controller.LoginController;
import org.junit.jupiter.api.*;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ExerciseTest {

    private static final String testUserFirstname = "first";
    private static final String testUserLastname = "last";
    private static final String testUserBirthday = "11.11.2020";
    private static final String testUserEmail = "@.";
    private static final String testExerciseName = "run";
    private static final int testExerciseBurnCalories = 5;
    private final ExerciseController exerciseController = ExerciseController.getInstance();
    private final LoginController loginController = LoginController.getInstance();
    private final AdminController adminController = AdminController.getInstance();
    private ExerciseDto exerciseDto;
    private User user;


    @BeforeEach
    void initTest() {
        exerciseDto = new ExerciseDto(testExerciseName, testExerciseBurnCalories);
        UserDto userDto = new UserDto(testUserFirstname, testUserLastname, testUserBirthday, testUserEmail);
        user = loginController.createUser(userDto).get();
    }

    @Test
    void createExerciseTest() {
        exerciseController.createExercise(user, exerciseDto.getExerciseName(), exerciseDto.getCaloriesBurnInHour());
        Set<Exercise> userExercises = exerciseController.getUserExercises(user);
        assertEquals(1, userExercises.size(), "созданный тип тренировки не внесен в список");
    }

    @Test
    void getExerciseToNameTest() {
        Exercise exercise = exerciseController
                .createExercise(user, exerciseDto.getExerciseName(), exerciseDto.getCaloriesBurnInHour());
        Optional<Exercise> exerciseToName = exerciseController.getExerciseToName(user, testExerciseName);
        assertEquals(exercise, exerciseToName.get(), "созданный тип тренировки не найден в списке по названию");
    }

    @Test
    void isExerciseNewTest() {
        Exercise exercise = new Exercise(exerciseDto.getExerciseName(), exerciseDto.getCaloriesBurnInHour());
        assertTrue(exerciseController.isExerciseNew(user, exercise), "новый тип тренировки указан как существующий");
        exercise = exerciseController
                .createExercise(user, exerciseDto.getExerciseName(), exerciseDto.getCaloriesBurnInHour());
        assertFalse(exerciseController.isExerciseNew(user, exercise), "существующий тип тренировки указан как новый");
    }

    @AfterEach
    void clean() {
        adminController.deleteUser(user);
    }
}
