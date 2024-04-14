package by.y_lab.p4service;

import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;
import by.y_lab.p2dao.DiaryDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.TreeSet;

/**
 * Обработка запросов к дневнику пользователя
 */
public class DiaryService {

    private static final DiaryService INSTANCE = new DiaryService();
    private final DiaryDao diaryDao = DiaryDao.getInstance();

    private DiaryService() {
    }

    /**
     * Создание дневника для нового пользователя
     *
     * @param user новый пользователь
     */
    public void createDiary(User user) {
        diaryDao.createDiary(user);
    }

    /**
     * Добавление актуальной тренировки в дневник
     *
     * @param user     аккаунт-владелец дневника
     * @param exercise добавляемая в дневник тренировка
     * @param times    количество единиц выполнения
     */
    public void addExercise(User user, Exercise exercise, int times) {
        diaryDao.addExercise(user, exercise, times);
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
    public boolean addExercise(User user, Exercise exercise, int times, LocalDateTime exerciseTime) {
        return diaryDao.addExercise(user, exercise, times, exerciseTime);
    }

    /**
     * Удаление неактуальной тренировки из дневника
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     удаляемая из дневника тренировка
     * @param exerciseDate дата и время удаляемой тренировки
     */
    public void deleteExercise(User user, Exercise exercise, LocalDate exerciseDate) {
        diaryDao.deleteExercise(user, exercise, exerciseDate);
    }

    /**
     * Предоставление полного списка тренировок в дневнике
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок
     */
    public List<TreeSet<NoteExerciseInDiary>> getDiaryList(User user) {
        return diaryDao.getDiaryList(user);
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
    public TreeSet<NoteExerciseInDiary> getLastDay(User user) {
        return diaryDao.getLastDay(user);
    }

    public boolean isExerciseInDiary(User user, Exercise exercise, LocalDateTime exerciseTime) {
        return diaryDao.isExerciseInDiary(user, exercise, exerciseTime);
    }

    public static DiaryService getInstance() {
        return INSTANCE;
    }
}
