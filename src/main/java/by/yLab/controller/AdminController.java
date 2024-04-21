package by.yLab.controller;


import by.yLab.util.Action;
import by.yLab.entity.Audit;
import by.yLab.entity.User;
import by.yLab.service.AuditService;
import by.yLab.service.DiaryNoteService;
import by.yLab.service.ExerciseService;
import by.yLab.service.UserService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Работа с правами администратора
 */
public class AdminController {
    private static final AdminController INSTANCE = new AdminController();

    private UserService userService = UserService.getInstance();
    private DiaryNoteService diaryService = DiaryNoteService.getInstance();
    private ExerciseService exerciseService = ExerciseService.getInstance();
    private AuditService auditService = AuditService.getInstance();
    public static final String ADMIN_PASSWORD = "iAmAdmin";


    private AdminController() {
    }

    /**
     * Проверка пароля доступа к правам администратора
     *
     * @param password введенный пароль
     * @return правильность пароля
     */
    public boolean checkPassword(String password) {
        return password.equals(ADMIN_PASSWORD);
    }

    /**
     * Предоставление администратору списка всех пользователей
     *
     * @return список всех пользователей
     */
    public HashSet<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Предоставление администратору данных пользователя
     *
     * @param lastName фамилия искомого пользователя
     * @param email    почта искомого пользователя
     * @return аккаунт искомого пользователя
     */
    public Optional<User> getUser(String lastName, String email) {
        return userService.findUser(lastName, email);
    }

    /**
     * Удаление аккаунта пользователя
     *
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteUser(User user) {
        diaryService.deleteDiary(user);
        exerciseService.deleteExercises(user);
        userService.deleteUser(user);
    }

    /**
     * Добавление записи аудита
     *
     * @param user   совершивший действие пользователь
     * @param action действие пользователя
     */
    public void addAction(User user, Action action) {
        auditService.addAction(user, action);
    }

    /**
     * Запрос на список действий пользователя
     *
     * @param user запрашиваемый пользователь
     * @return список действий пользователя
     */
    public List<Audit> getAuditUser(User user) {
        return auditService.getAuditUser(user);
    }

    public static AdminController getInstance() {
        return INSTANCE;
    }


}
