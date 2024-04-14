package by.y_lab.p5controller;

import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.User;
import by.y_lab.p4service.ExerciseService;

import java.util.Optional;
import java.util.Set;

/**
 * Работа с типами тренировок
 */
public class ExerciseController {

    private static final ExerciseController INSTANCE = new ExerciseController();

    private final ExerciseService exerciseService = ExerciseService.getInstance();

    private ExerciseController() {
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
        return exerciseService.createExercise(user, exercisesName, caloriesBurnInHour);
    }

    /**
     * Проверка уникальности типа тренировки
     *
     * @param user     аккаунт, для которого будет проверена уникальность типа тренировки
     * @param exercise проверяемый тип тренировки
     * @return наличие уникальности типа тренировки
     */
    public boolean isExerciseNew(User user, Exercise exercise) {
        return exerciseService.isExerciseNew(user, exercise);
    }

    /**
     * Информация о созданных типах тренировок
     *
     * @param user аккаунт-владелец списка типов тренировок
     * @return список типов тренировок
     */
    public Set<Exercise> getUserExercises(User user) {
        return exerciseService.getUserExercises(user);
    }

    /**
     * Поиск типа тренировки в точке сохранения по его названию
     *
     * @param user         аккаунт-владелец типа тренировки
     * @param exerciseName название типа тренировки
     * @return тип тренировки
     */
    public Optional<Exercise> getExerciseToName(User user, String exerciseName) {
        return exerciseService.getExerciseToName(user, exerciseName);
    }

    public static ExerciseController getInstance() {
        return INSTANCE;
    }
}
