package by.y_lab.p2dao;

import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Выполнение запросов к данным типов тренировок
 */
public class ExerciseDao {

    private final HashMap<User, Set<Exercise>> exercises = new HashMap<>();
    private static final ExerciseDao INSTANCE = new ExerciseDao();

    private ExerciseDao() {
    }

    /**
     * Создание тренировки нового типа
     * @param user аккаунт, для которого будет создан тип тренировки
     * @param exercisesName название типа тренировки
     * @param caloriesBurnInHour количество сжигаемых калорий в единицу выполнения тренировки
     * @return тренировку нового типа
     */
    public Exercise createExercise(User user, String exercisesName, int caloriesBurnInHour) {
        if (!exercises.containsKey(user)) {
            exercises.put(user, new HashSet<>());
        }
        Exercise exercise = new Exercise(exercisesName, caloriesBurnInHour);
        exercises.get(user).add(exercise);
        return exercise;
    }

    /**
     * Поиск типа тренировки в точке сохранения по его названию
     * @param user аккаунт-владелец типа тренировки
     * @param exerciseName название типа тренировки
     * @return тип тренировки
     */
    public Optional<Exercise> getExerciseToName(User user, String exerciseName) {
        Optional<Exercise> exerciseOptional;

        exerciseOptional = exercises.get(user)
                .stream()
                .filter(ex -> ex.getExerciseName().equals(exerciseName))
                .findFirst();

        return exerciseOptional;
    }

    /**
     * Проверка уникальности типа тренировки
     * @param user аккаунт, для которого будет проверена уникальность типа тренировки
     * @param exercise проверяемый тип тренировки
     * @return наличие типа тренировки в списке созданных тренировок
     */
    public boolean isExerciseNew(User user, Exercise exercise) {
        exercises.computeIfAbsent(user, exercises -> new HashSet<>());
        return !exercises.get(user).contains(exercise);
    }

    /**
     * Информация о созданных типах тренировок
     * @param user аккаунт-владелец списка типов тренировок
     * @return список типов тренировок
     */
    public Set<Exercise> getUserExercises(User user) {
        exercises.computeIfAbsent(user, exercises -> new HashSet<>());
        return exercises.get(user);
    }

    /**
     * Удаление типов тренировок пользователя
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteExercises(User user){
        exercises.remove(user);
    }


    public static ExerciseDao getInstance() {
        return INSTANCE;
    }


}
