package by.y_lab.p4service;

import by.y_lab.p1entity.User;
import by.y_lab.p2dao.UserDao;

import java.util.HashSet;
import java.util.Optional;

/**
 * Обработка запросов к данным пользователей
 */
public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final UserDao userDao = UserDao.getInstance();

    /**
     * Проверка наличия аккаунта у пользователя
     * @param user аккаунт пользователя
     * @return сведения о наличии аккаунта пользователя в списке зарегистрированных пользователей
     */
    public boolean isUserInBase(User user) {
        return userDao.isUserRegistered(user);
    }

    /**
     * Возвращение пользователя к ранее созданному аккаунту
     * @param lastName фамилия введенная пользователем
     * @param email почта  введенная пользователем
     * @return аккаунт пользователя из точки хранения аккаунтов
     */
    public Optional<User> findUser(String lastName, String email) {
        return userDao.findUser(lastName, email);
    }

    /**
     * Сохранение аккаунта пользователя.
     * @param user сведения, аккаунт пользователя
     */
    public void addUser(User user) {
        userDao.addUser(user);
    }

    /**
     * Предоставление списка всех пользователей
     * @return список всех пользователей
     */
    public HashSet<User> getAllUsers() {
        return userDao.getUsers();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
