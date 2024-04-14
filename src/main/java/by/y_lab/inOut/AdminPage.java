package by.y_lab.inOut;

import static by.y_lab.p0util.SelectionItems.*;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;
import by.y_lab.p3dto.UserDto;

import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

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
     * @return актуальность пароля
     */
    public static String askAdminPassword() {
        System.out.println(ENTER_ADMIN_PASSWORD);
        return SCANNER.nextLine();
    }

    /**
     * Демонстрация списка всех пользователей, помимо текущего аккаунта
     * с последующим предложением дальнейших действий
     * @param userAdmin текущий пользователь в статусе администратора
     * @param users список всех пользователей
     * @return ответ пользователя
     */
    public static String showAllUsers(User userAdmin, HashSet<User> users) {
        for (User user : users) {
            if (user != userAdmin) {
                System.out.println(user.getLastName() + " " + user.getEmail());
            }
        }
        System.out.println(ADMIN_SETTINGS);
        System.out.println(SHOW_USERS_DATA);
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
     * @param user указанный пользователь
     * @param diary дневник указанного пользователя
     */
    public static void showUser(User user, List<TreeSet<NoteExerciseInDiary>> diary) {
        System.out.println(user);
        for(TreeSet<NoteExerciseInDiary> day : diary) {
            day.forEach(System.out::println);
        }
        System.out.println();
    }
}
