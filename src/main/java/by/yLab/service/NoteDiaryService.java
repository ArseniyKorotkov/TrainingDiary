package by.yLab.service;

import by.yLab.entity.Exercise;
import by.yLab.entity.NoteDiary;
import by.yLab.entity.User;
import by.yLab.dao.NoteDiaryDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Обработка запросов к дневнику пользователя
 */
public class NoteDiaryService {

    private static final NoteDiaryService INSTANCE = new NoteDiaryService();
    private final NoteDiaryDao diaryDao = NoteDiaryDao.getInstance();

    private NoteDiaryService() {
    }

    /**
     * Добавление актуальной тренировки в дневник
     *
     * @param user     аккаунт-владелец дневника
     * @param exercise добавляемая в дневник тренировка
     * @param times    количество единиц выполнения
     */
    public void addExercise(User user, Exercise exercise, int times) {
        diaryDao.addNodeExercise(user, exercise, times);
    }

    /**
     * Добавление неактуальной тренировки в дневник
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     добавляемая в дневник тренировка
     * @param times        количество единиц выполнения
     * @param exerciseTime дата и время проведенной тренировки
     */
    public void addExercise(User user, Exercise exercise, int times, LocalDateTime exerciseTime) {
        diaryDao.addNodeExercise(user, exercise, times, exerciseTime);
    }

    /**
     * Удаление неактуальной тренировки из дневника
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     удаляемая из дневника тренировка
     * @param exerciseDate дата и время удаляемой тренировки
     */
    public void deleteExercise(User user, Exercise exercise, LocalDate exerciseDate) {
        diaryDao.deleteNoteExercise(user, exercise, exerciseDate);
    }

    /**
     * Предоставление списка тренировок в дневнике в указанный день
     *
     * @param user аккаунт-владелец дневника
     * @param date запрашиваемая дата
     * @return списка тренировок в дневнике в указанный день
     */
    public List<NoteDiary> getDateNoteExercises(User user, LocalDate date) {
        return diaryDao.getDateNoteExercises(user, date);
    }

    /**
     * Предоставление полного списка тренировок в дневнике
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок
     */
    public List<NoteDiary> getDiaryList(User user) {
        return diaryDao.getAllNoteExercises(user);
    }

    /**
     * Удаление дневника пользователя
     *
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteDiary(User user) {
        diaryDao.deleteDiary(user);
    }

    /**
     * Предоставление списка тренировок за текуще сутоки
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок за текуще сутоки
     */
    public List<NoteDiary> getLastDay(User user) {
        return diaryDao.getToday(user);
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
        return diaryDao.isExerciseInDiary(user, exercise, exerciseTime);
    }

    public static NoteDiaryService getInstance() {
        return INSTANCE;
    }
}
