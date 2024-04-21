package by.yLab.inOut;

import static by.yLab.util.SelectionItems.*;

import by.yLab.entity.Audit;
import by.yLab.entity.DiaryNote;
import by.yLab.entity.User;
import by.yLab.dto.UserDto;

import java.util.HashSet;
import java.util.List;

/**
 * Страница работы администратора
 */
public class AdminPage extends Page {

    private static final String ENTER_ADMIN_PASSWORD = "enter admin password";
    private static final String ADMIN_SETTINGS = "Admin settings:";
    private static final String PASSWORD_IS_WRONG = "Password is wrong!";
    private static final String NOT_FIND_TRY_AGAIN = "Not find. Try again";
    private static final String ENTER_USERS_LASTNAME = "Enter user`s lastname";
    private static final String ENTER_USERS_EMAIL = "Enter user`s email";

    /**
     * Запрос пароля
     *
     * @return актуальность пароля
     */
    public static String askAdminPassword() {
        System.out.println(ENTER_ADMIN_PASSWORD);
        return SCANNER.nextLine();
    }

    /**
     * Демонстрация списка всех пользователей, помимо текущего аккаунта
     * с последующим предложением дальнейших действий
     *
     * @param userAdmin текущий пользователь в статусе администратора
     * @param users     список всех пользователей
     * @return ответ пользователя
     */
    public static String showAllUsers(User userAdmin, HashSet<User> users) {
        for (User user : users) {
            if (user != userAdmin) {
                System.out.println(user.getLastname() + " " + user.getEmail());
            }
        }
        System.out.println(ADMIN_SETTINGS);
        System.out.println(SHOW_USERS_DATA);
        System.out.println(SHOW_AUDIT_USER_FILES);
        System.out.println(DELETE_USER);
        System.out.println(EXIT);
        return SCANNER.nextLine();
    }


    /**
     * Демонстрация отрицательного ответа на введенный неактуальный пароль
     */
    public static void refuseAnswer() {
        System.out.println(PASSWORD_IS_WRONG);
    }

    /**
     * Демонстрация отрицательного ответа на поиск пользователя по введенным данным
     */
    public static void noFindUserAnswer() {
        System.out.println(NOT_FIND_TRY_AGAIN);
        System.out.println();
    }

    /**
     * Запрос данных у пользователя поиска аккаунта пользователя по введенным администратором данным
     *
     * @return dto контейнер с введенными данными
     */
    public static UserDto getUserDto() {
        System.out.println(ENTER_USERS_LASTNAME);
        String lastname = SCANNER.nextLine();
        System.out.println(ENTER_USERS_EMAIL);
        String email = SCANNER.nextLine();
        return new UserDto(lastname, email);
    }

    /**
     * Демонстрация дневника указанного пользователя
     *
     * @param user  указанный пользователь
     * @param diary дневник указанного пользователя
     */
    public static void showUser(User user, List<DiaryNote> diary) {
        System.out.println(user);
        diary.forEach(System.out::println);
        System.out.println();
    }

    /**
     * Демонстрация действий указанного пользователя
     *
     * @param user указанный пользователь
     * @param auditUser файлы аудита указанного пользователя
     */
    public static void showUserAudit(User user, List<Audit> auditUser) {
        System.out.println(user + ": ");
        auditUser.forEach(System.out::println);

    }
}
