package by.yLab.inOut;

import by.yLab.entity.Exercise;
import by.yLab.dto.NoteDiaryDto;

import java.util.Optional;
import java.util.Set;

/**
 * Страница добавления тренировки в дневник
 */
public class AddExerciseInDiaryPage extends Page {

    private static final String NO_EXERCISES = "No exercises. Create, please!";
    private static final String NO_SUCH_EXERCISE = "No such exercise. Create, please!";
    private static final String SELECT_FROM = "Select from:";
    private static final String HOW_MANY = "How many?";
    private static final String EXERCISE_IS_ADDED = "exercise is added";

    /**
     * Демонстрация списка тренировок пользователя для добавления в дневник
     * Получение выбора пользователя
     * Запрос на количество единиц выполнения выбранного типа тренировок
     *
     * @param exerciseSet список тренировок пользователя
     * @return dto контейнер с введенными данными
     */
    public static Optional<NoteDiaryDto> addExerciseInDiary(Set<Exercise> exerciseSet) {
        if (exerciseSet.isEmpty()) {
            System.out.println(NO_EXERCISES);
            System.out.println();
            return Optional.empty();
        }
        System.out.println(SELECT_FROM);
        for (Exercise exercise : exerciseSet) {
            System.out.println(exercise.getExerciseName());
        }
        String exerciseName = SCANNER.nextLine();
        System.out.println(HOW_MANY);
        int exerciseTimes = Integer.parseInt(SCANNER.nextLine());
        System.out.println();
        return Optional.of(new NoteDiaryDto(exerciseName, exerciseTimes));
    }

    /**
     * Демонстрация положительного ответа
     */
    public static void agreeMessage() {
        System.out.println(EXERCISE_IS_ADDED);
        System.out.println();
    }

    /**
     * Демонстрация отрицательного ответа по причине отсутствия созданных типов тренировки
     */
    public static void refuseEmptyExercisesMessage() {
        System.out.println(NO_EXERCISES);
        System.out.println();
    }

    /**
     * Демонстрация отрицательного ответа по причине отсутствия указанного типа тренировки
     */
    public static void refuseNoSuchExerciseMessage() {
        System.out.println(NO_SUCH_EXERCISE);
        System.out.println();
    }


}
