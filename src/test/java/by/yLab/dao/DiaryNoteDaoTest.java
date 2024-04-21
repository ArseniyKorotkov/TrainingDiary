package by.yLab.dao;

import by.yLab.util.FormatDateTime;
import by.yLab.entity.Exercise;
import by.yLab.entity.DiaryNote;
import by.yLab.entity.User;
import by.yLab.util.JdbcConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class DiaryNoteDaoTest {

    @Container
    private static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres")
            .withExposedPorts(5433, 5432)
            .withUsername("postgres")
            .withPassword("ArsySQL")
            .withDatabaseName("postgres");

    private static final String ADD_USER_SQL = """
            INSERT INTO user_account(firstname, lastname, birthday, email, registration)
            VALUES (?,?,?,?,?);
            """;
    private static final String DELETE_USER_SQL = """
            DELETE FROM user_account
            WHERE lastname=?
            AND email=?;
            """;
    private static final String CREATE_EXERCISE_SQL = """
            INSERT INTO exercise(user_id, exercise_name, calories)
            VALUES(?, ?, ?);
            """;
    private static final String DELETE_USER_EXERCISES_SQL = """
            DELETE FROM exercise
            WHERE user_id=?
            """;
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

    private static final User USER = new User(TEST_USER_ID,
            TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    private static final Exercise FIRST_EXERCISE = new Exercise(1, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
    private static final Exercise SECOND_EXERCISE = new Exercise(2, TEST_SECOND_EXERCISE_NAME, TEST_SECOND_EXERCISE_BURN_CALORIES);

    private static Connection connection;

    @InjectMocks
    private DiaryNoteDao noteDiaryDao;
    @Mock
    private JdbcConnector jdbcConnector;

    @BeforeAll
    static void initDatabase() {
        try {
            connection = container.createConnection("");
            JdbcConnector.initDatabaseLiquibase(connection);
            connection.setAutoCommit(false);

            addUserInDatabase();
            addExerciseInDatabase(FIRST_EXERCISE);
            addExerciseInDatabase(SECOND_EXERCISE);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void initMock() {
        Mockito.doReturn(Optional.of(connection)).when(jdbcConnector).getConnection();
    }

    @Test
    void addNodeExercise() {
        List<DiaryNote> allNoteExercises = noteDiaryDao.getAllNoteExercises(USER);
        noteDiaryDao.addNodeExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_TIMES);
        assertEquals(allNoteExercises.size() + 1, noteDiaryDao.getAllNoteExercises(USER).size(),
                "количество записей не увеличено при добавлении новой записи на текущие сутки");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void addNodeExerciseDateTime() {
        List<DiaryNote> allNoteExercises = noteDiaryDao.getAllNoteExercises(USER);
        noteDiaryDao.addNodeExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        assertEquals(allNoteExercises.size() + 1, noteDiaryDao.getAllNoteExercises(USER).size());
        noteDiaryDao.addNodeExercise(USER, SECOND_EXERCISE, TEST_SECOND_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        assertEquals(allNoteExercises.size() + 2, noteDiaryDao.getAllNoteExercises(USER).size(),
                "количество записей не увеличено при добавлении новой записи на указанное время");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteNoteExercise() {
        noteDiaryDao.addNodeExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(USER, SECOND_EXERCISE, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        List<DiaryNote> allNoteExercises = noteDiaryDao.getAllNoteExercises(USER);
        noteDiaryDao.deleteNoteExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_DATE_TIME.toLocalDate());
        assertEquals(allNoteExercises.size() - 1, noteDiaryDao.getAllNoteExercises(USER).size(),
                "количество записей не уменьшено при удалении записи");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAllNoteExercises() {
        noteDiaryDao.addNodeExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(USER, SECOND_EXERCISE, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        assertEquals(2, noteDiaryDao.getAllNoteExercises(USER).size(),
                "возвращены не все записи");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getDateNoteExercises() {
        noteDiaryDao.addNodeExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(USER, SECOND_EXERCISE, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        assertEquals(1, noteDiaryDao.getDateNoteExercises(USER, TEST_FIRST_EXERCISE_DATE_TIME.toLocalDate()).size(),
                "количество записей возвращено без учета даты");
        assertEquals(1, noteDiaryDao.getDateNoteExercises(USER, TEST_SECOND_EXERCISE_DATE_TIME.toLocalDate()).size(),
                "количество записей возвращено без учета даты");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getToday() {
        noteDiaryDao.addNodeExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(USER, SECOND_EXERCISE, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        assertEquals(1, noteDiaryDao.getToday(USER).size(),
                "количество записей возвращено без учета даты сегодняшнего дня");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteDiary() {
        noteDiaryDao.addNodeExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        noteDiaryDao.addNodeExercise(USER, SECOND_EXERCISE, TEST_SECOND_EXERCISE_TIMES, TEST_SECOND_EXERCISE_DATE_TIME);
        noteDiaryDao.deleteDiary(USER);
        assertEquals(0, noteDiaryDao.getAllNoteExercises(USER).size(),
                "удалены не все записи пользователя");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isExerciseInDiary() {
        assertFalse(noteDiaryDao.isExerciseInDiary(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_DATE_TIME),
                "не добавленный тип тренировки принят за добавленный");
        noteDiaryDao.addNodeExercise(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_TIMES, TEST_FIRST_EXERCISE_DATE_TIME);
        assertTrue(noteDiaryDao.isExerciseInDiary(USER, FIRST_EXERCISE, TEST_FIRST_EXERCISE_DATE_TIME),
                "добавленный тип тренировки принят за не добавленный");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @AfterAll
    static void cleanDatabase() {
        try {
            deleteUserFromDatabase();
            deleteExercisesFromDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addUserInDatabase() throws SQLException {
        PreparedStatement addUserStatement = connection.prepareStatement(ADD_USER_SQL);

        addUserStatement.setString(1, USER.getFirstname());
        addUserStatement.setString(2, USER.getLastname());
        addUserStatement.setDate(3, Date.valueOf(USER.getBirthday()));
        addUserStatement.setString(4, USER.getEmail());
        addUserStatement.setDate(5, Date.valueOf(USER.getRegistrationDate()));

        addUserStatement.executeUpdate();
        connection.commit();
    }

    private static void deleteUserFromDatabase() throws SQLException {
        PreparedStatement deleteUserStatement = connection.prepareStatement(DELETE_USER_SQL);
        deleteUserStatement.setString(1, USER.getLastname());
        deleteUserStatement.setString(2, USER.getEmail());
        deleteUserStatement.executeUpdate();
    }

    private static void addExerciseInDatabase(Exercise exercise) throws SQLException {
        PreparedStatement createExerciseStatement = connection.prepareStatement(CREATE_EXERCISE_SQL);
        createExerciseStatement.setLong(1, USER.getId());
        createExerciseStatement.setString(2, exercise.getExerciseName());
        createExerciseStatement.setInt(3, exercise.getCaloriesBurnInHour());
        createExerciseStatement.executeUpdate();
        connection.commit();
    }

    private static void deleteExercisesFromDatabase() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_EXERCISES_SQL);
        preparedStatement.setLong(1, USER.getId());
        preparedStatement.executeUpdate();
        connection.commit();
    }
}
