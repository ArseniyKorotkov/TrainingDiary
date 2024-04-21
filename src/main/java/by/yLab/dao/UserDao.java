package by.yLab.dao;

import by.yLab.entity.User;
import by.yLab.util.JdbcConnector;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;

/**
 * Выполнение запросов к данным пользователей
 */
public class UserDao {

    private static final String ADD_USER_SQL = """
            INSERT INTO user_account(firstname, lastname, birthday, email, registration)
            VALUES (?,?,?,?,?);
            """;

    private static final String GET_USERS_SQL = """
            SELECT *
            FROM user_account;
            """;

    private static final String FIND_USER_BY_LASTNAME_EMAIL_SQL = """
          SELECT *
          FROM user_account
          WHERE lastname=?
          AND email=?;
          """;
    private static final String DELETE_USER_SQL = """
          DELETE FROM user_account
          WHERE lastname=?
          AND email=?;
          """;

    private static final UserDao INSTANCE = new UserDao();

    private JdbcConnector connector = new JdbcConnector();

    private UserDao() {
    }

    /**
     * Проверка наличия аккаунта у пользователя.
     *
     * @param user аккаунт пользователя
     * @return сведения о наличии аккаунта пользователя в списке зарегистрированных пользователей
     */
    public boolean isUserRegistered(User user) {
        return findUser(user.getLastname(), user.getEmail()).isPresent();
    }

    /**
     * Сохранение аккаунта пользователя.
     *
     * @param user сведения, аккаунт пользователя
     */
    public void addUser(User user) {
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement addUserStatement = connection.prepareStatement(ADD_USER_SQL);

                addUserStatement.setString(1, user.getFirstname());
                addUserStatement.setString(2, user.getLastname());
                addUserStatement.setDate(3, Date.valueOf(user.getBirthday()));
                addUserStatement.setString(4, user.getEmail());
                addUserStatement.setDate(5, Date.valueOf(user.getRegistrationDate()));

                addUserStatement.executeUpdate();
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public HashSet<User> getUsers() {
        HashSet<User> users = new HashSet<>();
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement getUsersStatement = connection.prepareStatement(GET_USERS_SQL);
                ResultSet resultSet = getUsersStatement.executeQuery();
                while (resultSet.next()) {
                    users.add(buildUser(resultSet));
                }
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Поиск аккаунта пользователя в списке зарегистрированных пользователей.
     *
     * @param lastName фамилия введенная пользователем
     * @param email    почта  введенная пользователем
     * @return аккаунт пользователя из точки хранения аккаунтов
     */
    public Optional<User> findUser(String lastName, String email) {
        Optional<User> userOptional = Optional.empty();
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement findUserStatement = connection.prepareStatement(FIND_USER_BY_LASTNAME_EMAIL_SQL);
                findUserStatement.setString(1, lastName);
                findUserStatement.setString(2, email);
                ResultSet resultSet = findUserStatement.executeQuery();
                while (resultSet.next()) {
                    userOptional = Optional.of(buildUser(resultSet));
                }
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userOptional;
    }

    /**
     * Удаление аккаунта пользователя из списка
     *
     * @param user удаляемый аккаунт
     */
    public void deleteUser(User user) {
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement deleteUserStatement = connection.prepareStatement(DELETE_USER_SQL);
                deleteUserStatement.setString(1, user.getLastname());
                deleteUserStatement.setString(2, user.getEmail());
                deleteUserStatement.executeUpdate();
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getString(3),
                resultSet.getDate(4).toLocalDate(),
                resultSet.getString(5),
                resultSet.getDate(6).toLocalDate());
    }
}
