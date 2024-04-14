package by.y_lab.p5controller;


import by.y_lab.p1entity.User;
import by.y_lab.p4service.DiaryService;
import by.y_lab.p4service.ExerciseService;
import by.y_lab.p4service.UserService;

import java.util.HashSet;
import java.util.Optional;

/**
 * Работа с правами администратора
 */
public class AdminController {
    private static final AdminController INSTANCE = new AdminController();

    private final UserService userService = UserService.getInstance();
    private final DiaryService diaryService = DiaryService.getInstance();
    private final ExerciseService exerciseService = ExerciseService.getInstance();
    public static final String ADMIN_PASSWORD = "iAmAdmin";


    private AdminController() {
    }

    /**
     * Проверка пароля доступа к правам  администратора
     * @param password введенный пароль
     * @return правильность пароля
     */
    public boolean checkPassword(String password) {
        return password.equals(ADMIN_PASSWORD);
    }

    /**
     * Предоставление администратору списка всех пользователей
     * @return список всех пользователей
     */
    public HashSet<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Предоставление администратору данных пользователя
     * @param lastName фамилия искомого пользователя
     * @param email почта искомого пользователя
     * @return аккаунт искомого пользователя
     */
    public Optional<User> getUser(String lastName, String email) {
        return userService.findUser(lastName, email);
    }

    /**
     * Удаление аккаунта пользователя
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteUser(User user) {
        diaryService.deleteDiary(user);
        exerciseService.deleteExercises(user);
        userService.getAllUsers().remove(user);
    }

    public static AdminController getInstance() {
        return INSTANCE;
    }


}
