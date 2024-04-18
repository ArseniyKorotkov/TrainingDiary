package by.yLab.service;

import by.yLab.entity.Exercise;
import by.yLab.entity.User;
import by.yLab.dao.ExerciseDao;

import java.util.Optional;
import java.util.Set;

/**
 * Обработка запросов к данным типов тренировок
 */
public class ExerciseService {

    private static final ExerciseService INSTANCE = new ExerciseService();
    private final ExerciseDao exerciseDao = ExerciseDao.getInstance();

    private ExerciseService() {
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
        return exerciseDao.createExercise(user, exercisesName, caloriesBurnInHour);
    }

    /**
     * Поиск типа тренировки в точке сохранения по его названию
     *
     * @param user         аккаунт-владелец типа тренировки
     * @param exerciseName название типа тренировки
     * @return тип тренировки
     */
    public Optional<Exercise> getExerciseToName(User user, String exerciseName) {
        return exerciseDao.getExerciseToName(user, exerciseName);
    }

    /**
     * Проверка уникальности типа тренировки
     *
     * @param user     аккаунт, для которого будет проверена уникальность типа тренировки
     * @param exercise проверяемый тип тренировки
     * @return наличие уникальности типа тренировки
     */
    public boolean isExerciseNew(User user, Exercise exercise) {
        return exerciseDao.isExerciseNew(user, exercise);
    }

    /**
     * Информация о созданных типах тренировок
     *
     * @param user аккаунт-владелец списка типов тренировок
     * @return список типов тренировок
     */
    public Set<Exercise> getUserExercises(User user) {
        return exerciseDao.getUserExercises(user);
    }

    /**
     * Удаление типов тренировок пользователя
     *
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteExercises(User user) {
        exerciseDao.deleteExercises(user);
    }

    public static ExerciseService getInstance() {
        return INSTANCE;
    }
}
