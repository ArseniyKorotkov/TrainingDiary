import by.y_lab.p1entity.User;
import by.y_lab.p3dto.UserDto;
import by.y_lab.p5controller.AdminController;
import by.y_lab.p5controller.LoginController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    private static final String testUserFirstname = "first";
    private static final String testUserLastname = "last";
    private static final String testUserBirthday = "11.11.2020";
    private static final String testUserEmail = "@.";
    private final LoginController loginController = LoginController.getInstance();
    private final AdminController adminController = AdminController.getInstance();

    @Test
    void createUser() {
        UserDto userDto = new UserDto(testUserFirstname, testUserLastname, testUserBirthday, testUserEmail);
        Optional<User> user = loginController.createUser(userDto);
        assertTrue(loginController.isUserInBase(user.get()), "созданный пользователь не обнаружен в списке");
    }

    @Test
    void deleteUser() {
        UserDto userDto = new UserDto(testUserFirstname, testUserLastname, testUserBirthday, testUserEmail);
        Optional<User> user = loginController.createUser(userDto);
        int sizeBeforeDelete = adminController.getAllUsers().size();
        adminController.deleteUser(user.get());
        assertEquals(sizeBeforeDelete - 1, adminController.getAllUsers().size(),
                "количество пользователей не уменьшилось на единицу после одного удаления");
    }

    @Test
    void getUserToLastNameAndEmail() {
        UserDto userDto = new UserDto(testUserFirstname, testUserLastname, testUserBirthday, testUserEmail);
        Optional<User> user = loginController.createUser(userDto);
        Optional<User> testUser = adminController.getUser(testUserLastname, testUserEmail);
        assertEquals(user, testUser, "созданный пользователь не возвращен из списка");
    }

    @AfterEach
    void clean() {
        adminController.getAllUsers().clear();
    }
}
