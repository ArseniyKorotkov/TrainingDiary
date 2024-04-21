package by.yLab.inOut;

import static by.yLab.util.SelectionItems.*;

import by.yLab.entity.Exercise;
import by.yLab.entity.NoteDiary;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Страница запроса тренировок в прошедший промежуток времени отсортированные по дате
 */
public class ShowDiaryTimeSlicePage extends Page {

    public static final String DAYS_FORMAT = "Enter days format dd.mm.yyyy/dd.mm.yyyy";
    public static final String DATE_FORMAT = "enter date exercise format dd.mm.yyyy hh:mm";
    public static final String BURNED_CALORIES = "In this days burned %s calories";
    private static final String NO_EXERCISES = "No exercises. Create, please!";
    private static final String SELECT_FROM = "Select from:";
    private static final String EXERCISE_IS_DELETED = "exercise is deleted from diary";

    /**
     * Запрос временного промежутка
     * @return ответ пользователя
     */
    public static String askTimeSlice() {
        System.out.println(DAYS_FORMAT);
        return SCANNER.nextLine();
    }

    /**
     * Запрос названия типа тренировки для удаления дневника
     * @param exerciseSet  список тренировок пользователя
     * @return название удаляемого из дневника типа тренировки
     */
    public static Optional<String> selectExerciseToDelete(Set<Exercise> exerciseSet) {
        if(exerciseSet.isEmpty()) {
            System.out.println(NO_EXERCISES);
            System.out.println();
            return Optional.empty();
        }
        System.out.println(SELECT_FROM);
        for (Exercise exercise : exerciseSet) {
            System.out.println(exercise.getExerciseName());
        }
        String exerciseName = SCANNER.nextLine();
        return Optional.of(exerciseName);
    }

    /**
     * Демонстрация списка тренировок в отрезок времени
     * @param timeSliceList список тренировок в указанный отрезок времени
     * @param caloriesInDayBurned количество потраченных калорий в указанный отрезок времени
     */
    public static void showTrainingDays(List<NoteDiary> timeSliceList, int caloriesInDayBurned) {
        timeSliceList.forEach(System.out::println);
        System.out.printf((BURNED_CALORIES) + "%n", caloriesInDayBurned);
        System.out.println();
    }

    /**
     * Вывод меню работы с дневником
     * @return  ответ пользователя
     */
    public static String diaryMenu() {
        System.out.println(ADD);
        System.out.println(DELETE);
        System.out.println(EXIT);
        return SCANNER.nextLine();
    }

    /**
     * Запрос даты для записи неактуальной тренировки
     * @return  ответ пользователя
     */
    public static String askDate() {
        System.out.println(DATE_FORMAT);
        return SCANNER.nextLine();
    }

    /**
     * Демонстрация положительного ответа
     */
    public static void agreeMessage() {
        System.out.println(EXERCISE_IS_DELETED);
        System.out.println();
    }

    /**
     * Демонстрация отрицательного ответа
     */
    public static void refuseMessage() {
        System.out.println(NO_EXERCISES);
        System.out.println();
    }
}
