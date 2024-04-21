package by.yLab.dao;

import by.yLab.util.FormatDateTime;
import by.yLab.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserDaoTest {

    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";
    private static final String TEST_SECOND_USER_FIRSTNAME = "second first";
    private static final String TEST_SECOND_USER_LASTNAME = "second last";
    private static final String TEST_SECOND_USER_BIRTHDAY = "11.11.2022";
    private static final String TEST_SECOND_USER_EMAIL = "@q.";

    private final User user = new User(TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    private final User secondUser = new User(TEST_SECOND_USER_FIRSTNAME,
            TEST_SECOND_USER_LASTNAME,
            LocalDate.parse(TEST_SECOND_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_SECOND_USER_EMAIL,
            LocalDate.now().minusDays(8));

    @InjectMocks
    private UserDao userDao;

    @Test
    void isUserRegistered() {
        userDao.addUser(user);
        assertTrue(userDao.isUserRegistered(user),
                "зарегистрированный пользователь принят за не зарегистрированного");
        assertFalse(userDao.isUserRegistered(secondUser),
                " не зарегистрированный пользователь принят за зарегистрированного");
    }

    @Test
    void addUser() {
        assertEquals(0, userDao.getUsers().size(), "список пользователей изначально не пуст");
        userDao.addUser(user);
        assertEquals(1, userDao.getUsers().size(), "первый пользователь не добавлен в список");
        userDao.addUser(secondUser);
        assertEquals(2, userDao.getUsers().size(), "второй пользователь не добавлен в список");
    }

    @Test
    void getUsers() {
        assertEquals(new HashSet<User>(), userDao.getUsers(), "список пользователей не возвращен");
    }

    @Test
    void findUser() {
        userDao.addUser(user);
        Optional<User> userOptional = userDao.findUser(TEST_USER_LASTNAME, TEST_USER_EMAIL);
        assertTrue(userOptional.isPresent(), "пользователь не найден по фамилии и почте");
        assertEquals(user, userOptional.get(), "найден неверный пользователь по фамилии и почте");
        assertFalse(userDao.findUser(TEST_SECOND_USER_LASTNAME, TEST_USER_EMAIL).isPresent());
    }

    @Test
    void deleteUser() {
        userDao.addUser(user);
        userDao.addUser(secondUser);
        int testSize = userDao.getUsers().size();
        userDao.deleteUser(user);
        assertEquals(testSize - 1, userDao.getUsers().size(), "первый пользователь не удален из списка");
        userDao.deleteUser(secondUser);
        assertEquals(testSize - 2, userDao.getUsers().size(), "второй пользователь не удален из списка");
    }
}
