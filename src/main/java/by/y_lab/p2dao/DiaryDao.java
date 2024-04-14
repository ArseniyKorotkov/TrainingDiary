package by.y_lab.p2dao;

import by.y_lab.p1entity.Diary;
import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

/**
 * Выполнение запросов к данным дневника
 */
public class DiaryDao {

    private static final DiaryDao INSTANCE = new DiaryDao();
    private final HashMap<User, Diary> diaryHashMap = new HashMap<>();

    private DiaryDao() {
    }

    /**
     * Создание дневника для нового пользователя
     *
     * @param user новый пользователь
     */
    public void createDiary(User user) {
        Diary diary = new Diary();
        diaryHashMap.put(user, diary);
    }

    /**
     * Добавление актуальной тренировки в дневник
     *
     * @param user     аккаунт-владелец дневника
     * @param exercise добавляемая в дневник тренировка
     * @param times    количество единиц выполнения
     */
    public void addExercise(User user, Exercise exercise, int times) {
        diaryHashMap.get(user).addExercise(exercise, times);
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
        return diaryHashMap.get(user).addExercise(user, exercise, times, exerciseTime);
    }

    /**
     * Удаление неактуальной тренировки из дневника
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     удаляемая из дневника тренировка
     * @param exerciseDate дата и время удаляемой тренировки
     */
    public void deleteExercise(User user, Exercise exercise, LocalDate exerciseDate) {
        diaryHashMap.get(user).deleteExercise(user, exercise, exerciseDate);
    }

    /**
     * Предоставление полного списка тренировок в дневнике
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок
     */
    public List<TreeSet<NoteExerciseInDiary>> getDiaryList(User user) {
        return diaryHashMap.get(user).getDiaryList();
    }

    /**
     * Предоставление списка тренировок за текуще сутоки
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок за текуще сутоки
     */
    public TreeSet<NoteExerciseInDiary> getLastDay(User user) {
        return diaryHashMap.get(user).getLastDay();
    }

    /**
     * Удаление дневника пользователя
     *
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteDiary(User user) {
        diaryHashMap.remove(user);
    }

    public boolean isExerciseInDiary(User user, Exercise exercise, LocalDateTime exerciseTime) {
        return diaryHashMap.get(user).isExerciseInDiary(user, exercise, exerciseTime);
    }

    public static DiaryDao getInstance() {
        return INSTANCE;
    }
}
