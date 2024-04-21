package by.yLab.controller;

import by.yLab.entity.User;
import by.yLab.dto.UserDto;
import by.yLab.service.UserService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginControllerTest {

    private static final String TEST_USER_FIRSTNAME = "first";
    private static final String TEST_USER_LASTNAME = "last";
    private static final String TEST_USER_BIRTHDAY = "11.11.2020";
    private static final String TEST_USER_EMAIL = "@.";

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UserService userService;

    private UserDto userDto;

    @BeforeAll
    void init() {
        userDto = new UserDto(TEST_USER_FIRSTNAME, TEST_USER_LASTNAME, TEST_USER_BIRTHDAY, TEST_USER_EMAIL);
    }

    @Test
    void createUserTest() {
        Optional<User> user = loginController.createUser(userDto);
        assertTrue(user.isPresent(), "не возвращен добавленный пользователь");
        verify(userService, times(1)).addUser(user.get());
    }

    @Test
    void checkAccount() {
        Optional<User> user = loginController.createUser(userDto);
        doReturn(user).when(userService).findUser(TEST_USER_LASTNAME, TEST_USER_EMAIL);
        assertTrue(loginController.checkAccount(userDto).isPresent(), "пользователь не найден по имени");
        verify(userService, times(1)).findUser(TEST_USER_LASTNAME, TEST_USER_EMAIL);
    }

    @Test
    void isUserInBase() {
        Optional<User> user = loginController.createUser(userDto);
        assertTrue(user.isPresent(), "не возвращен добавленный пользователь");
        verify(userService).isUserInBase(user.get());
        doReturn(true).when(userService).isUserInBase(user.get());
        assertTrue(loginController.isUserInBase(user.get()), "добавленный пользователь принят за не добавленного");
    }

}
