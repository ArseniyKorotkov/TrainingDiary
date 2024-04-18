package by.yLab.dao;

import by.yLab.entity.User;

import java.util.HashSet;
import java.util.Optional;

/**
 * Выполнение запросов к данным пользователей
 */
public class UserDao {

    private static final UserDao INSTANCE = new UserDao();

    private final HashSet<User> USERS = new HashSet<>();

    private UserDao() {
    }

    /**
     * Проверка наличия аккаунта у пользователя.
     *
     * @param user аккаунт пользователя
     * @return сведения о наличии аккаунта пользователя в списке зарегистрированных пользователей
     */
    public boolean isUserRegistered(User user) {
        return USERS.stream()
                .filter(user1 -> user.getLastName().equals(user1.getLastName()))
                .anyMatch(user1 -> user.getEmail().equals(user1.getEmail()));
    }

    /**
     * Сохранение аккаунта пользователя.
     *
     * @param user сведения, аккаунт пользователя
     */
    public void addUser(User user) {
        USERS.add(user);
    }

    public HashSet<User> getUsers() {
        return USERS;
    }

    /**
     * Поиск аккаунта пользователя в списке зарегистрированных пользователей.
     *
     * @param lastName фамилия введенная пользователем
     * @param email    почта  введенная пользователем
     * @return аккаунт пользователя из точки хранения аккаунтов
     */
    public Optional<User> findUser(String lastName, String email) {

        return USERS.stream()
                .filter(user1 -> lastName.equals(user1.getLastName()))
                .filter(user1 -> email.equals(user1.getEmail()))
                .findFirst();

    }

    /**
     * Удаление аккаунта пользователя из списка
     *
     * @param user удаляемый аккаунт
     */
    public void deleteUser(User user) {
        USERS.remove(user);
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }
}
