package by.yLab.dao;

import by.yLab.util.FormatDateTime;
import by.yLab.entity.Exercise;
import by.yLab.entity.DiaryNote;
import by.yLab.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class DiaryNoteDaoTest {

    @Container
    private final PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest");
    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";
    private static final long TEST_USER_ID = 1;
    private static final String TEST_FIRST_EXERCISE_NAME = "run";
    private static final int TEST_FIRST_EXERCISE_BURN_CALORIES = 5;
    private static final int TEST_FIRST_EXERCISE_TIMES = 3;
    private static final LocalDateTime TEST_FIRST_EXERCISE_DATE_TIME = LocalDateTime.now();
    private static final String TEST_SECOND_EXERCISE_NAME = "gym";
    private static final int TEST_SECOND_EXERCISE_BURN_CALORIES = 10;
    private static final int TEST_SECOND_EXERCISE_TIMES = 10;
    private static final LocalDateTime TEST_SECOND_EXERCISE_DATE_TIME = LocalDateTime.now().minusDays(2);

    private final User user = new User(TEST_USER_ID,
            TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    private final Exercise firstExercise = new Exercise(1,TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
    private final Exercise secondExercise = new Exercise(2, TEST_SECOND_EXERCISE_NAME, TEST_SECOND_EXERCISE_BURN_CALORIES);

    @InjectMocks
    private DiaryNoteDao noteDiaryDao;
    private UserDao userDao = UserDao.getInstance();
    private ExerciseDao exerciseDao = ExerciseDao.getInstance();

    @BeforeEach
    void prepareDataBase() {
        userDao.addUser(user);
        exerciseDao.createExercise(user, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        exerciseDao.createExercise(user, TEST_SECOND_EXERCISE_NAME, TEST_SECOND_EXERCISE_BURN_CALORIES);
    }

    @Test
    void addNodeExercise() {
        List<DiaryNote> allNoteExercises = noteDiaryDao.getAllNoteExercises(user);
        noteDiaryDao.addNodeExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES);
        assertEquals(allNoteExercises.size() + 1, noteDiaryDao.getAllNoteExercises(user).size(),
                "количество записей не увеличено при добавлении новой записи на текущие сутки");
    }

    @Test
    void addNodeExerciseDateTime() {
        List<DiaryNote> allNoteExercises = noteDiaryDao.getAllNoteExercises(user);
        noteDiaryDao.addNodeExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        assertEquals(allNoteExercises.size() + 1, noteDiaryDao.getAllNoteExercises(user).size());
        noteDiaryDao.addNodeExercise(user, secondExercise, TEST_SECOND_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        assertEquals(allNoteExercises.size() + 2, noteDiaryDao.getAllNoteExercises(user).size(),
                "количество записей не увеличено при добавлении новой записи на указанное время");
    }

    @Test
    void deleteNoteExercise() {
        noteDiaryDao.addNodeExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(user, secondExercise, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        List<DiaryNote> allNoteExercises = noteDiaryDao.getAllNoteExercises(user);
        noteDiaryDao.deleteNoteExercise(user, firstExercise, TEST_FIRST_EXERCISE_DATE_TIME.toLocalDate());
        assertEquals(allNoteExercises.size() - 1, noteDiaryDao.getAllNoteExercises(user).size(),
                "количество записей не уменьшено при удалении записи");
    }

    @Test
    void getAllNoteExercises() {
        noteDiaryDao.addNodeExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(user, secondExercise, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        assertEquals(2, noteDiaryDao.getAllNoteExercises(user).size(), "возвращены не все записи");
    }

    @Test
    void getDateNoteExercises() {
        noteDiaryDao.addNodeExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(user, secondExercise, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        assertEquals(1, noteDiaryDao.getDateNoteExercises(user, TEST_FIRST_EXERCISE_DATE_TIME.toLocalDate()).size(),
                "количество записей возвращено без учета даты");
        assertEquals(1, noteDiaryDao.getDateNoteExercises(user, TEST_SECOND_EXERCISE_DATE_TIME.toLocalDate()).size(),
                "количество записей возвращено без учета даты");
    }

    @Test
    void getToday() {
        noteDiaryDao.addNodeExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(user, secondExercise, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        assertEquals(1, noteDiaryDao.getToday(user).size(),
                "количество записей возвращено без учета даты сегодняшнего дня");
    }

    @Test
    void deleteDiary() {
        noteDiaryDao.addNodeExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(user, secondExercise, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        noteDiaryDao.deleteDiary(user);
        assertEquals(0, noteDiaryDao.getAllNoteExercises(user).size(),
                "удалены не все записи пользователя");
    }

    @Test
    void isExerciseInDiary() {
        assertFalse(noteDiaryDao.isExerciseInDiary(user, firstExercise, TEST_FIRST_EXERCISE_DATE_TIME),
                "не добавленный тип тренировки принят за добавленный");
        noteDiaryDao.addNodeExercise(user, firstExercise, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        assertTrue(noteDiaryDao.isExerciseInDiary(user, firstExercise, TEST_FIRST_EXERCISE_DATE_TIME),
                "добавленный тип тренировки принят за не добавленный");
    }
}
