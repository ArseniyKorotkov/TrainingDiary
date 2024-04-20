package by.yLab.inOut;

import by.yLab.entity.DiaryNote;

import java.util.List;

/**
 * Страница запроса тренировок в текущие сутки отсортированные по дате
 */
public class ShowDiaryPage extends Page {

    public static final String BURNED_CALORIES = "In day burned %s calories";
    public static final String MORE_INFORMATION_QUEST = "Do you want more information? Y/N";

    /**
     * Демонстрация списка тренировок в текущие сутки
     * с запросом на дополнительные действия с дневником(добавление неактуальных тренировок, удаление тренировок, просмотр дневника за указанный промежуток времени)
     * @param lastDay список тренировок за текущие сутки
     * @param caloriesInDayBurned количество потраченных калорий в текущие сутки
     * @return ответ пользователя
     */
    public static String showDiary(List<DiaryNote> lastDay, int caloriesInDayBurned) {
        for (DiaryNote noteExerciseInDiary : lastDay) {
            System.out.println(noteExerciseInDiary);
        }
        System.out.printf((BURNED_CALORIES) + "%n", caloriesInDayBurned);
        System.out.println(MORE_INFORMATION_QUEST);
        return SCANNER.nextLine();
    }
}
