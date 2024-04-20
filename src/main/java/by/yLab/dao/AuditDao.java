package by.yLab.dao;

import by.yLab.util.Action;
import by.yLab.entity.Audit;
import by.yLab.entity.User;
import by.yLab.util.JdbcConnector;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditDao {

    private static final AuditDao INSTANCE = new AuditDao();

    private static final String ADD_ACTION = """
            INSERT INTO audit(user_id, action_date_time, action_date, action_name)
            VALUES(?,?,?,?)
            """;
    private static final String GET_ACTIONS_FROM_USER = """
            SELECT *
            FROM audit
            WHERE user_id=?
            """;



    private AuditDao() {
    }

    /**
     * Добавление записи аудита
     *
     * @param user   совершивший действие пользователь
     * @param action действие пользователя
     */
    public void addAction(User user, Action action) {
        try(Connection connection = JdbcConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_ACTION);
            preparedStatement.setLong(1,user.getId());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setDate(3, Date.valueOf(LocalDate.now()));
            preparedStatement.setString(4, action.name());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Запрос на список действий пользователя
     *
     * @param user запрашиваемый пользователь
     * @return список действий пользователя
     */
    public List<Audit> getAuditUser(User user) {
        List<Audit> auditList = new ArrayList<>();
        try(Connection connection = JdbcConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ACTIONS_FROM_USER);
            preparedStatement.setLong(1,user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                auditList.add(new Audit(resultSet.getLong(2),
                        Action.valueOf(resultSet.getString(5)),
                        resultSet.getTimestamp(3).toLocalDateTime()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return auditList;
    }

    public static AuditDao getInstance() {
        return INSTANCE;
    }
}
