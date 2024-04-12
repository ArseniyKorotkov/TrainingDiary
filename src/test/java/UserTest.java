import by.y_lab.p1entity.User;
import by.y_lab.p3dto.UserDto;
import by.y_lab.p5controller.EnterController;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserTest {

    private final EnterController enterController = EnterController.getInstance();

    @Test
    void createUser() {
        UserDto userDto = new UserDto("a", "b", "11.11.1111", "@.");
        Optional<User> user = enterController.createUser(userDto);
        assertTrue(enterController.isUserInBase(user.get()));
    }
}
