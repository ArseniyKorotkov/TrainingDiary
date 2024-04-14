package by.y_lab.p5controller;

import by.y_lab.p0util.FormatDateTime;
import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;
import by.y_lab.p4service.DiaryService;

import static by.y_lab.p0util.SelectionItems.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Работа с дневником
 */
public class DiaryController {

    private static final DiaryController INSTANCE = new DiaryController();

    private final DiaryService diaryService = DiaryService.getInstance();

    private DiaryController() {
    }

    /**
     * Добавление актуальной тренировки в дневник
     *
     * @param user     аккаунт-владелец дневника
     * @param exercise добавляемая в дневник тренировка
     * @param times    количество единиц выполнения
     */
    public void addExercise(User user, Exercise exercise, int times) {
        diaryService.addExercise(user, exercise, times);
    }

    /**
     * Добавление неактуальной тренировки в дневник
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     добавляемая в дневник тренировка
     * @param times        количество единиц выполнения
     * @param exerciseTime дата и время проведенной тренировки
     * @return проверка временной корректности запрошенной даты
     */
    public boolean addMissingExercise(User user, Exercise exercise, int times, LocalDateTime exerciseTime) {
        return diaryService.addExercise(user, exercise, times, exerciseTime);
    }

    /**
     * Удаление неактуальной тренировки из дневника
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     удаляемая из дневника тренировка
     * @param exerciseDate дата и время удаляемой тренировки
     */
    public void deleteExercise(User user, Exercise exercise, LocalDate exerciseDate) {
        diaryService.deleteExercise(user, exercise, exerciseDate);
    }

    /**
     * Предоставление списка тренировок в отрезке времени
     *
     * @param daysString сведения о запрошенном пользователем отрезке времени
     * @param user       аккаунт-владелец дневника
     * @return список тренировок
     */
    public List<TreeSet<NoteExerciseInDiary>> getDiaryTimeSlice(String daysString, User user) {
        String[] days = daysString.split(REGEX);
        LocalDate startQuestDayDiary = LocalDate.parse(days[0], FormatDateTime.reformDate());
        LocalDate endQuestDayDiary = LocalDate.parse(days[1], FormatDateTime.reformDate());

        if (endQuestDayDiary.isAfter(LocalDate.now())) {
            endQuestDayDiary = LocalDate.now();
        }

        if (user.getRegistrationDate().isAfter(startQuestDayDiary)) {
            startQuestDayDiary = user.getRegistrationDate();
        }

        int startQuestDayInList = (int) ChronoUnit.DAYS.between(user.getRegistrationDate(), startQuestDayDiary);
        int daysInQuestion = (int) ChronoUnit.DAYS.between(startQuestDayDiary, endQuestDayDiary) + 1;

        List<TreeSet<NoteExerciseInDiary>> diaryInDaysList = diaryService.getDiaryList(user);

        List<TreeSet<NoteExerciseInDiary>> exercisesTimeSlice = new ArrayList<>();

        if (!diaryInDaysList.isEmpty()) {
            for (int i = startQuestDayInList; i < daysInQuestion; i++) {
                exercisesTimeSlice.add(diaryInDaysList.get(i));
            }
        }

        return exercisesTimeSlice;

    }

    /**
     * Суммирование сожженных калорий по списку тренировок
     *
     * @param diaryExercises список тренировок
     * @return сумма сожженных калорий
     */
    public int getBurnCalories(List<TreeSet<NoteExerciseInDiary>> diaryExercises) {
        int burnCalories = 0;
        for (TreeSet<NoteExerciseInDiary> day : diaryExercises) {
            burnCalories += day
                    .stream()
                    .map(note -> note.getExercise().getCaloriesBurnInHour() * note.getTimesCount())
                    .mapToInt(i -> i)
                    .sum();
        }
        return burnCalories;
    }

    /**
     * Предоставление списка тренировок за текуще сутоки
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок за текуще сутоки
     */
    public TreeSet<NoteExerciseInDiary> getLastDay(User user) {
        return diaryService.getLastDay(user);
    }

    public boolean isExerciseInDiary(User user, Exercise exercise, LocalDateTime exerciseTime) {
        return diaryService.isExerciseInDiary(user, exercise, exerciseTime);
    }

    public static DiaryController getInstance() {
        return INSTANCE;
    }
}
