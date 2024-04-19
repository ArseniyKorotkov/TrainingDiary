package by.yLab.controller;

import by.yLab.service.ExerciseService;
import by.yLab.util.FormatDateTime;
import by.yLab.entity.Exercise;
import by.yLab.entity.NoteDiary;
import by.yLab.entity.User;
import by.yLab.service.NoteDiaryService;

import static by.yLab.util.SelectionItems.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Работа с дневником
 */
public class DiaryController {

    private static final DiaryController INSTANCE = new DiaryController();

    private NoteDiaryService diaryService = NoteDiaryService.getInstance();
    private ExerciseService exerciseService = ExerciseService.getInstance();

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
     */
    public void addMissingExercise(User user, Exercise exercise, int times, LocalDateTime exerciseTime) {
         diaryService.addExercise(user, exercise, times, exerciseTime);
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
    public List<NoteDiary> getDiaryTimeSlice(String daysString, User user) {
        String[] days = daysString.split(REGEX);
        LocalDate startQuestDayDiary = LocalDate.parse(days[0], FormatDateTime.reformDate());
        LocalDate endQuestDayDiary = LocalDate.parse(days[1], FormatDateTime.reformDate());

        int daysInQuestion = (int) ChronoUnit.DAYS.between(startQuestDayDiary, endQuestDayDiary) + 1;

        List<NoteDiary> diaryInDaysList = new ArrayList<>(diaryService.getDiaryList(user));

        for (int i = 0; i < daysInQuestion; i++) {
            diaryInDaysList.addAll(diaryService.getDateNoteExercises(user, startQuestDayDiary.plusDays(i)));
        }

        return diaryInDaysList;
    }

    /**
     * Суммирование сожженных калорий по списку тренировок
     *
     * @param diaryExercises список тренировок
     * @return сумма сожженных калорий
     */
    public int getBurnCalories(List<NoteDiary> diaryExercises) {
        return diaryExercises.stream()
                .filter(note -> exerciseService.getExerciseToId(note.getUserId(), note.getExerciseId()).isPresent())
                .mapToInt(note -> note.getTimesCount() *
                                  exerciseService.getExerciseToId(note.getUserId(), note.getExerciseId())
                                          .get().getCaloriesBurnInHour())
                .sum();
    }

    /**
     * Предоставление списка тренировок за текуще сутоки
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок за текуще сутоки
     */
    public List<NoteDiary> getLastDay(User user) {
        return diaryService.getLastDay(user);
    }

    /**
     * Запрос на наличие записи тренировки в указанный день
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     тип тренировки
     * @param exerciseTime дата тренировки
     * @return наличие записи тренировки в указанный день
     */
    public boolean isExerciseInDiary(User user, Exercise exercise, LocalDateTime exerciseTime) {
        return diaryService.isExerciseInDiary(user, exercise, exerciseTime);
    }

    public static DiaryController getInstance() {
        return INSTANCE;
    }
}
