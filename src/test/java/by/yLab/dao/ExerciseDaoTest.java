package by.yLab.dao;

import by.yLab.util.FormatDateTime;
import by.yLab.entity.Exercise;
import by.yLab.entity.User;
import by.yLab.util.JdbcConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseDaoTest {

    private static final int TEST_USER_ID = 1;
    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";
    private static final String TEST_FIRST_EXERCISE_NAME = "run";
    private static final int TEST_FIRST_EXERCISE_BURN_CALORIES = 5;
    private static final String TEST_SECOND_EXERCISE_NAME = "gym";
    private static final int TEST_SECOND_EXERCISE_BURN_CALORIES = 10;

    private final Exercise firstExercise = new Exercise(TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
    private final Exercise secondExercise = new Exercise(TEST_SECOND_EXERCISE_NAME, TEST_SECOND_EXERCISE_BURN_CALORIES);

    private final User user = new User(TEST_USER_ID,
            TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    @InjectMocks
    private ExerciseDao exerciseDao;
    private UserDao userDao = UserDao.getInstance();

    @BeforeEach
    void prepareDataBase() {
        JdbcConnector.updateBase();
        userDao.addUser(user);
    }

    @Test
    void createExercise() {
        exerciseDao.createExercise(user, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        assertEquals(1, exerciseDao.getUserExercises(user).size(), "тип тренировки не создан");
        exerciseDao.createExercise(user, TEST_SECOND_EXERCISE_NAME, TEST_SECOND_EXERCISE_BURN_CALORIES);
        assertEquals(2, exerciseDao.getUserExercises(user).size(), "тип тренировки не создан");
    }

    @Test
    void getExerciseToName() {
        exerciseDao.createExercise(user, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        Optional<Exercise> exerciseToName = exerciseDao.getExerciseToName(user, TEST_FIRST_EXERCISE_NAME);
        assertTrue(exerciseToName.isPresent(), "тип тренировки не найден по имени");
        assertEquals(firstExercise, exerciseToName.get());
    }

    @Test
    void isExerciseNew() {
        exerciseDao.createExercise(user, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        assertFalse(exerciseDao.isExerciseNew(user, firstExercise), "созданная ранее тренировка принята за новую");
        assertTrue(exerciseDao.isExerciseNew(user, secondExercise), "новая тренировка принята за созданную ранее");
    }

    @Test
    void getUserExercises() {
        assertEquals(new HashSet<Exercise>(), exerciseDao.getUserExercises(user),
                "не создан список типов тренировок пользователя");
    }

    @Test
    void deleteExercises() {
        exerciseDao.createExercise(user, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        exerciseDao.createExercise(user, TEST_SECOND_EXERCISE_NAME, TEST_SECOND_EXERCISE_BURN_CALORIES);
        exerciseDao.deleteExercises(user);
        assertEquals(0, exerciseDao.getUserExercises(user).size(),
                "удалены не все типы тренировок пользователя");
    }
}
