package by.yLab.dao;

import by.yLab.entity.Exercise;
import by.yLab.entity.User;
import by.yLab.util.JdbcConnector;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Выполнение запросов к данным типов тренировок
 */
public class ExerciseDao {

    private static final String CREATE_EXERCISE_SQL = """
            INSERT INTO exercise(user_id, exercise_name, calories)
            VALUES(?, ?, ?);
            """;

    private static final String GET_EXERCISE_TO_NAME_SQL = """
            SELECT *
            FROM exercise
            WHERE user_id=?
            AND exercise_name=?;
            """;

    private static final String GET_EXERCISE_TO_ID_SQL = """
            SELECT *
            FROM exercise
            WHERE user_id=?
            AND id=?;
            """;

    private static final String GET_USER_EXERCISES_SQL = """
            SELECT *
            FROM exercise
            WHERE user_id=?
            """;

    private static final String DELETE_USER_EXERCISES_SQL = """
            DELETE FROM exercise
            WHERE user_id=?
            """;

    private static final ExerciseDao INSTANCE = new ExerciseDao();

    private JdbcConnector connector = new JdbcConnector();

    private HashMap<User, Set<Exercise>> exercises = new HashMap<>();

    private ExerciseDao() {
    }

    /**
     * Создание тренировки нового типа
     *
     * @param user               аккаунт, для которого будет создан тип тренировки
     * @param exercisesName      название типа тренировки
     * @param caloriesBurnInHour количество сжигаемых калорий в единицу выполнения тренировки
     * @return тренировку нового типа
     */
    public Exercise createExercise(User user, String exercisesName, int caloriesBurnInHour) {
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement createExerciseStatement = connection.prepareStatement(CREATE_EXERCISE_SQL);
                createExerciseStatement.setLong(1, user.getId());
                createExerciseStatement.setString(2, exercisesName);
                createExerciseStatement.setInt(3, caloriesBurnInHour);
                createExerciseStatement.executeUpdate();
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Exercise(exercisesName, caloriesBurnInHour);
    }

    /**
     * Поиск типа тренировки в точке сохранения по его названию
     *
     * @param user         аккаунт-владелец типа тренировки
     * @param exerciseName название типа тренировки
     * @return тип тренировки
     */
    public Optional<Exercise> getExerciseToName(User user, String exerciseName) {
        Optional<Exercise> exerciseOptional = Optional.empty();
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_EXERCISE_TO_NAME_SQL);
                preparedStatement.setLong(1, user.getId());
                preparedStatement.setString(2, exerciseName);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    exerciseOptional = Optional.of(buildExercise(resultSet));
                }
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseOptional;
    }

    /**
     * Поиск типа тренировки в точке сохранения по его названию
     *
     * @param userId     id аккаунт-владельца типа тренировки
     * @param exerciseId id названия типа тренировки
     * @return тип тренировки
     */
    public Optional<Exercise> getExerciseToId(long userId, long exerciseId) {
        Optional<Exercise> exerciseOptional = Optional.empty();
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_EXERCISE_TO_ID_SQL);
                preparedStatement.setLong(1, userId);
                preparedStatement.setLong(2, exerciseId);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    exerciseOptional = Optional.of(buildExercise(resultSet));
                }
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseOptional;
    }

    /**
     * Проверка уникальности типа тренировки
     *
     * @param user     аккаунт, для которого будет проверена уникальность типа тренировки
     * @param exercise проверяемый тип тренировки
     * @return наличие типа тренировки в списке созданных тренировок
     */
    public boolean isExerciseNew(User user, Exercise exercise) {
        return getExerciseToName(user, exercise.getExerciseName()).isEmpty();
    }

    /**
     * Информация о созданных типах тренировок
     *
     * @param user аккаунт-владелец списка типов тренировок
     * @return список типов тренировок
     */
    public Set<Exercise> getUserExercises(User user) {
        Set<Exercise> exerciseSet = new HashSet<>();
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_EXERCISES_SQL);
                preparedStatement.setLong(1, user.getId());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    exerciseSet.add(buildExercise(resultSet));
                }
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exerciseSet;
    }

    /**
     * Удаление типов тренировок пользователя
     *
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteExercises(User user) {
        try {
            if (connector.getConnection().isPresent()) {
                Connection connection = connector.getConnection().get();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER_EXERCISES_SQL);
                preparedStatement.setLong(1, user.getId());
                preparedStatement.executeUpdate();
            } else {
                throw new SQLException();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Exercise buildExercise(ResultSet resultSet) throws SQLException {
        return new Exercise(resultSet.getLong(1),
                resultSet.getString(3),
                resultSet.getInt(4));
    }

    public static ExerciseDao getInstance() {
        return INSTANCE;
    }


}
