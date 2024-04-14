package by.y_lab.inOut;

import by.y_lab.p1entity.Exercise;
import by.y_lab.p3dto.ExerciseDto;

/**
 * Страница создания типа тренировки
 */
public class CreateExercisePage extends Page {

    private static final String ENTER_NAME_NEW_EXERCISE = "Enter name new exercise:";
    private static final String ENTER_BURN_CALORIES_ON_ONE_TIME = "Enter burn calories on one time";
    private static final String IS_CREATED = " is created";
    private static final String IS_UPDATED = " is updated";

    /**
     * Запрос данных у пользователя для создания тренировки
     * @return dto контейнер с введенными данными
     */
    public static ExerciseDto createExercise() {
        System.out.println(ENTER_NAME_NEW_EXERCISE);
        String name = SCANNER.nextLine();
        System.out.println(ENTER_BURN_CALORIES_ON_ONE_TIME);
        int calories = Integer.parseInt(SCANNER.nextLine());
        return new ExerciseDto(name, calories);
    }

    /**
     * Демонстрация сообщения об успешном создании типа тренировки
     * @param exercise  создаваемый типа тренировки
     */
    public static void exerciseCreated(Exercise exercise) {
        System.out.println(exercise.getExerciseName() + IS_CREATED);
        System.out.println();
    }

    /**
     * Демонстрация сообщения об успешном обновлении типа тренировки
     * @param exercise обновляемый тип тренировки
     */
    public static void exerciseUpdated(Exercise exercise) {
        System.out.println(exercise.getExerciseName() + IS_UPDATED);
        System.out.println();
    }
}
