package by.y_lab.inOut;

import static by.y_lab.p0util.SelectionItems.*;

import by.y_lab.p1entity.NoteExerciseInDiary;

import java.util.List;
import java.util.TreeSet;

/**
 * Страница запроса тренировок в прошедший промежуток времени отсортированные по дате
 */
public class ShowDiaryTimeSlicePage extends Page {

    public static final String DAYS_FORMAT = "Enter days format dd.mm.yyyy/dd.mm.yyyy";
    public static final String DATE_FORMAT = "enter date exercise format dd.mm.yyyy hh:mm";
    public static final String BURNED_CALORIES = "In this days burned %s calories";

    /**
     * Запрос временного промежутка
     * @return ответ пользователя
     */
    public static String askTimeSlice() {
        System.out.println(DAYS_FORMAT);
        return SCANNER.nextLine();
    }

    /**
     * Демонстрация списка тренировок в отрезок времени
     * @param timeSliceList список тренировок в указанный отрезок времени
     * @param caloriesInDayBurned количество потраченных калорий в указанный отрезок времени
     */
    public static void showTrainingDays(List<TreeSet<NoteExerciseInDiary>> timeSliceList, int caloriesInDayBurned) {
        for (TreeSet<NoteExerciseInDiary> oneTrainingDay : timeSliceList) {
            oneTrainingDay.forEach(System.out::println);
        }
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
}
