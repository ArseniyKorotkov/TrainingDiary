package by.yLab.dao;

import by.yLab.util.Action;
import by.yLab.util.FormatDateTime;
import by.yLab.entity.Audit;
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
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@ExtendWith(MockitoExtension.class)
public class AuditDaoTest {

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
    private static final long TEST_USER_ID = 1;
    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";
    private static final Action FIRST_ACTION = Action.AUTHORIZATION;
    private static final Action SECOND_ACTION = Action.REGISTRATION;

    private static final User USER = new User(
            TEST_USER_ID,
            TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    private static Connection connection;
    @InjectMocks
    private AuditDao auditDao;
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
    void addAction() {
        int startSize = auditDao.getAuditUser(USER).size();
        auditDao.addAction(USER, FIRST_ACTION);
        auditDao.addAction(USER, SECOND_ACTION);
        assertEquals(startSize + 2, auditDao.getAuditUser(USER).size(),
                "действия пользователя не добавлены");
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getAuditUser() {
        assertEquals(new ArrayList<Audit>(), auditDao.getAuditUser(USER),
                "не создан список действий пользователя");
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
