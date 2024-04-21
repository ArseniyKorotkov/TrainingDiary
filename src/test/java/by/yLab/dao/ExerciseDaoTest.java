package by.yLab.dao;

import by.yLab.util.FormatDateTime;
import by.yLab.entity.Exercise;
import by.yLab.entity.User;
import by.yLab.util.JdbcConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class ExerciseDaoTest {

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

    private static final User USER = new User(TEST_USER_ID,
            TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    private static Connection connection;
    @InjectMocks
    private ExerciseDao exerciseDao;
    @Mock
    private JdbcConnector jdbcConnector;

    @BeforeAll
    static void initDatabase() {
        try {
            connection = container.createConnection("");
            JdbcConnector.initDatabaseLiquibase(connection);
            connection.setAutoCommit(false);
            addUserInDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void initMock() {
        Mockito.doReturn(Optional.of(connection)).when(jdbcConnector).getConnection();
    }

    @Test
    void createExercise() {
        exerciseDao.createExercise(USER, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        int size = exerciseDao.getUserExercises(USER).size();
        assertEquals(1, exerciseDao.getUserExercises(USER).size(), "тип тренировки не создан");

        exerciseDao.createExercise(USER, TEST_SECOND_EXERCISE_NAME, TEST_SECOND_EXERCISE_BURN_CALORIES);
        assertEquals(2, exerciseDao.getUserExercises(USER).size(), "тип тренировки не создан");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getExerciseToName() {
        exerciseDao.createExercise(USER, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        Optional<Exercise> exerciseToName = exerciseDao.getExerciseToName(USER, TEST_FIRST_EXERCISE_NAME);
        assertTrue(exerciseToName.isPresent(), "тип тренировки не найден по имени");
        assertEquals(firstExercise, exerciseToName.get());
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void isExerciseNew() {
        exerciseDao.createExercise(USER, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        assertFalse(exerciseDao.isExerciseNew(USER, firstExercise),
                "созданная ранее тренировка принята за новую");
        assertTrue(exerciseDao.isExerciseNew(USER, secondExercise),
                "новая тренировка принята за созданную ранее");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getUserExercises() {
        assertEquals(new HashSet<Exercise>(), exerciseDao.getUserExercises(USER),
                "не создан список типов тренировок пользователя");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteExercises() {
        exerciseDao.createExercise(USER, TEST_FIRST_EXERCISE_NAME, TEST_FIRST_EXERCISE_BURN_CALORIES);
        exerciseDao.createExercise(USER, TEST_SECOND_EXERCISE_NAME, TEST_SECOND_EXERCISE_BURN_CALORIES);
        exerciseDao.deleteExercises(USER);
        assertEquals(0, exerciseDao.getUserExercises(USER).size(),
                "удалены не все типы тренировок пользователя");
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
}
