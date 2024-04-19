package by.yLab.dao;

import by.yLab.util.Action;
import by.yLab.util.FormatDateTime;
import by.yLab.entity.Audit;
import by.yLab.entity.User;
import by.yLab.util.JdbcConnector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuditDaoTest {

    private static final long TEST_USER_ID = 1;
    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";
    private static final Action FIRST_ACTION = Action.AUTHORIZATION;
    private static final Action SECOND_ACTION = Action.REGISTRATION;

    private final User user = new User(
            TEST_USER_ID,
            TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    @InjectMocks
    private AuditDao auditDao;
    private UserDao userDao = UserDao.getInstance();

    @BeforeEach
    void prepareDataBase() {
        JdbcConnector.updateBase();
        userDao.addUser(user);
    }
    @Test
    void addAction() {
        auditDao.addAction(user, FIRST_ACTION);
        auditDao.addAction(user, SECOND_ACTION);
        assertEquals(2, auditDao.getAuditUser(user).size(), "действия пользователя не добавлены");
    }

    @Test
    void getAuditUser() {
        assertEquals(new ArrayList<Audit>(), auditDao.getAuditUser(user), "не создан список действий пользователя");
    }
}
