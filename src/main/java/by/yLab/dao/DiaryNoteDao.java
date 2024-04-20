package by.yLab.dao;

import by.yLab.entity.Exercise;
import by.yLab.entity.DiaryNote;
import by.yLab.entity.User;
import by.yLab.util.JdbcConnector;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Выполнение запросов к данным дневника
 */
public class DiaryNoteDao {

    private static final String ADD_NOTE_EXERCISE_SQL = """
            INSERT INTO diary_note(user_id, exercise_id, count, exercise_date_time, exercise_date)
            VALUES(?,?,?,?,?)
            """;

    private static final String DELETE_NOTE_EXERCISE_SQL = """
            DELETE FROM diary_note
            WHERE user_id=?
            AND exercise_id=?
            AND exercise_date=?
            """;

    private static final String DELETE_DIARY_SQL = """
            DELETE FROM diary_note
            WHERE user_id=?
            """;

    private static final String GET_ALL_NOTE_EXERCISES_SQL = """
            SELECT *
            FROM diary_note
            WHERE user_id=?
            ORDER BY exercise_date;
            """;

    private static final String GET_DATE_NOTE_EXERCISES_SQL = """
            SELECT *
            FROM diary_note
            WHERE user_id=?
            AND exercise_date=?
            ORDER BY exercise_date;
            """;

    private static final String GET_DATE_NOTE_EXERCISE_SQL = """
            SELECT *
            FROM diary_note
            WHERE user_id=?
            AND exercise_id=?
            AND exercise_date=?
            """;


    private static final DiaryNoteDao INSTANCE = new DiaryNoteDao();

    private DiaryNoteDao() {
    }

    /**
     * Добавление актуальной тренировки в дневник
     *
     * @param exercise добавляемая в дневник тренировка
     * @param times    количество единиц выполнения
     */
    public void addNodeExercise(User user, Exercise exercise, int times) {
        try (Connection connection = JdbcConnector.getConnection()) {
            Optional<DiaryNote> exerciseFromDiary = getExerciseFromDiary(user, exercise, LocalDateTime.now());
            if (exerciseFromDiary.isPresent()) {
                times += exerciseFromDiary.get().getTimesCount();
            }
            LocalDateTime timeNow = LocalDateTime.now();
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_NOTE_EXERCISE_SQL);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, exercise.getId());
            preparedStatement.setInt(3, times);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(timeNow));
            preparedStatement.setDate(5, Date.valueOf(timeNow.toLocalDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавление неактуальной тренировки в дневник
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     добавляемая в дневник тренировка
     * @param times        количество единиц выполнения
     * @param exerciseTime дата и время проведенной тренировки
     */
    public void addNodeExercise(User user, Exercise exercise, int times, LocalDateTime exerciseTime) {
        try (Connection connection = JdbcConnector.getConnection()) {
            Optional<DiaryNote> exerciseFromDiary = getExerciseFromDiary(user, exercise, exerciseTime);
            if (exerciseFromDiary.isPresent()) {
                times += exerciseFromDiary.get().getTimesCount();
                deleteNoteExercise(user,exercise,exerciseTime.toLocalDate());
            }
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_NOTE_EXERCISE_SQL);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, exercise.getId());
            preparedStatement.setInt(3, times);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(exerciseTime));
            preparedStatement.setDate(5, Date.valueOf(exerciseTime.toLocalDate()));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Удаление записи тренировки из дневника
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     удаляемая из дневника тренировка
     * @param exerciseDate дата и время удаляемой тренировки
     */
    public void deleteNoteExercise(User user, Exercise exercise, LocalDate exerciseDate) {
        try (Connection connection = JdbcConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_NOTE_EXERCISE_SQL);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, exercise.getId());
            preparedStatement.setDate(3, Date.valueOf(exerciseDate));
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Предоставление полного списка тренировок в дневнике
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок
     */
    public List<DiaryNote> getAllNoteExercises(User user) {
        List<DiaryNote> noteDiaryList = new ArrayList<>();
        try (Connection connection = JdbcConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_NOTE_EXERCISES_SQL);
            preparedStatement.setLong(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                noteDiaryList.add(buildNoteDiary(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteDiaryList;
    }

    /**
     * Предоставление выбранного по дате списка тренировок в дневнике
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок
     */
    public List<DiaryNote> getDateNoteExercises(User user, LocalDate date) {
        List<DiaryNote> noteDiaryList = new ArrayList<>();
        try (Connection connection = JdbcConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_DATE_NOTE_EXERCISES_SQL);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setDate(2, Date.valueOf(date));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                noteDiaryList.add(buildNoteDiary(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteDiaryList;
    }

    /**
     * Предоставление списка тренировок за текуще сутоки
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок за текуще сутоки
     */
    public List<DiaryNote> getToday(User user) {
        return getDateNoteExercises(user, LocalDate.now());
    }

    /**
     * Удаление из дневника всех записей пользователя
     *
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteDiary(User user) {
        try (Connection connection = JdbcConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DIARY_SQL);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Проверка наличия записи тренировки в дневнике
     *
     * @param user         проверяемый аккаунт пользователя
     * @param exercise     проверяемый тип тренировки
     * @param exerciseTime время проверяемой тренировки
     * @return наличие записи тренировки в дневнике
     */
    public boolean isExerciseInDiary(User user, Exercise exercise, LocalDateTime exerciseTime) {
        return getExerciseFromDiary(user, exercise, exerciseTime).isPresent();
    }

    /**
     * Получение информации о записи тренировки в дневнике
     *
     * @param user         проверяемый аккаунт пользователя
     * @param exercise     проверяемый тип тренировки
     * @param exerciseTime время проверяемой тренировки
     * @return запись тренировки в дневнике
     */
    public Optional<DiaryNote> getExerciseFromDiary(User user, Exercise exercise, LocalDateTime exerciseTime) {
        Optional<DiaryNote> noteDiaryOptional = Optional.empty();
        try (Connection connection = JdbcConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_DATE_NOTE_EXERCISE_SQL);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setLong(2, exercise.getId());
            preparedStatement.setDate(3, Date.valueOf(exerciseTime.toLocalDate()));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                noteDiaryOptional = Optional.of(buildNoteDiary(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noteDiaryOptional;
    }

    private DiaryNote buildNoteDiary(ResultSet resultSet) throws SQLException {
        return new DiaryNote(resultSet.getLong(2),
                        resultSet.getLong(3),
                resultSet.getDate(6).toLocalDate().atTime(0,0),
                resultSet.getInt(4));
    }

    public static DiaryNoteDao getInstance() {
        return INSTANCE;
    }
}
