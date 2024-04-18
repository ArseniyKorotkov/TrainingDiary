package by.yLab.controller;

import by.yLab.util.Action;
import by.yLab.util.FormatDateTime;
import by.yLab.entity.Audit;
import by.yLab.entity.User;
import by.yLab.service.AuditService;
import by.yLab.service.NoteDiaryService;
import by.yLab.service.ExerciseService;
import by.yLab.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    private static final String ADMIN_PASSWORD = "iAmAdmin";
    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";
    private static final Action TEST_ACTION = Action.AUTHORIZATION;

    @InjectMocks
    private AdminController adminController;
    @Mock
    private UserService userService;
    @Mock
    private NoteDiaryService diaryService;
    @Mock
    private ExerciseService exerciseService;
    @Mock
    private AuditService auditService;

    private final User user = new User(TEST_USER_FIRSTNAME,
            TEST_USER_LASTNAME,
            LocalDate.parse(TEST_USER_BIRTHDAY, FormatDateTime.reformDate()),
            TEST_USER_EMAIL,
            LocalDate.now().minusDays(5));

    @Test
    void checkPassword() {
        assertTrue(adminController.checkPassword(ADMIN_PASSWORD), "не дан доступ пользователю с правильным паролем");
        assertFalse(adminController.checkPassword(""), "дан доступ пользователю с не правильным паролем");
    }

    @Test
    void getAllUsers() {
        HashSet<User> users = new HashSet<>();
        doReturn(users).when(userService).getAllUsers();
        HashSet<User> allUsers = adminController.getAllUsers();
        verify(userService, times(1)).getAllUsers();
        assertEquals(users, allUsers, "возвращен не правильный список пользователей");
    }

    @Test
    void getUser() {
        Optional<User> user = Optional.of(this.user);
        doReturn(user).when(userService).findUser(TEST_USER_LASTNAME, TEST_USER_EMAIL);
        Optional<User> userOptional = adminController.getUser(TEST_USER_LASTNAME, TEST_USER_EMAIL);
        assertTrue(userOptional.isPresent());
        verify(userService, times(1)).findUser(TEST_USER_LASTNAME, TEST_USER_EMAIL);
        assertEquals(user, userOptional, "возвращен не правильный пользователь");
    }

    @Test
    void deleteUser() {
        adminController.deleteUser(user);
        verify(diaryService, times(1)).deleteDiary(user);
        verify(exerciseService, times(1)).deleteExercises(user);
        verify(userService, times(1)).deleteUser(user);
    }

    @Test
    void addAction() {
        adminController.addAction(user, TEST_ACTION);
        verify(auditService, times(1)).addAction(user, TEST_ACTION);
    }

    @Test
    void getAuditUser() {
        List<Audit> testAudit = new ArrayList<>();
        doReturn(testAudit).when(auditService).getAuditUser(user);
        List<Audit> auditUser = adminController.getAuditUser(user);
        verify(auditService, times(1)).getAuditUser(user);
        assertEquals(testAudit, auditUser, "возвращен не правильный пользователь");
    }

}
