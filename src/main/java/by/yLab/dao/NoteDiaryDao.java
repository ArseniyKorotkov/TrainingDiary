package by.yLab.dao;

import by.yLab.entity.Exercise;
import by.yLab.entity.NoteDiary;
import by.yLab.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Выполнение запросов к данным дневника
 */
public class NoteDiaryDao {

    private static final NoteDiaryDao INSTANCE = new NoteDiaryDao();

    private Set<NoteDiary> diary = new HashSet<>();

    private NoteDiaryDao() {
    }

    /**
     * Добавление актуальной тренировки в дневник
     *
     * @param exercise добавляемая в дневник тренировка
     * @param times    количество единиц выполнения
     */
    public void addNodeExercise(User user, Exercise exercise, int times) {
        Optional<NoteDiary> noteDiary = diary.stream()
                .filter(note -> note.getUser().equals(user))
                .filter(note -> note.getExercise().equals(exercise))
                .filter(note -> note.getDateTime().toLocalDate().equals(LocalDate.now()))
                .findFirst();
        if (noteDiary.isPresent()) {
            times += noteDiary.get().getTimesCount();
            deleteNoteExercise(noteDiary.get());
        }
        diary.add(new NoteDiary(user, exercise, LocalDateTime.now(), times));
    }

    /**
     * Добавление неактуальной тренировки в дневник
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     добавляемая в дневник тренировка
     * @param times        количество единиц выполнения
     * @param exerciseTime дата и время проведенной тренировки
     */
    public void addNodeExercise(User user, Exercise exercise, int times, LocalDateTime exerciseTime) {
        Optional<NoteDiary> noteDiary = diary.stream()
                .filter(note -> note.getUser().equals(user))
                .filter(note -> note.getExercise().equals(exercise))
                .filter(note -> note.getDateTime().toLocalDate().equals(exerciseTime.toLocalDate()))
                .findFirst();
        if (noteDiary.isPresent()) {
            times += noteDiary.get().getTimesCount();
            deleteNoteExercise(noteDiary.get());
        }
        diary.add(new NoteDiary(user, exercise, exerciseTime, times));
    }

    /**
     * Удаление записи тренировки из дневника
     *
     * @param user         аккаунт-владелец дневника
     * @param exercise     удаляемая из дневника тренировка
     * @param exerciseDate дата и время удаляемой тренировки
     */
    public void deleteNoteExercise(User user, Exercise exercise, LocalDate exerciseDate) {
        Optional<NoteDiary> deleteExercise = diary.stream()
                .filter(note -> note.getUser().equals(user))
                .filter(note -> note.getDateTime().toLocalDate().equals(exerciseDate))
                .filter(note -> note.getExercise().equals(exercise))
                .findFirst();
        deleteExercise.ifPresent(diary::remove);
    }

    /**
     * Удаление записи тренировки из дневника.
     *
     * @param noteDiary удаляемая запись тренировки
     */
    public void deleteNoteExercise(NoteDiary noteDiary) {
        deleteNoteExercise(noteDiary.getUser(), noteDiary.getExercise(), noteDiary.getDateTime().toLocalDate());
    }

    /**
     * Предоставление полного списка тренировок в дневнике
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок
     */
    public List<NoteDiary> getAllNoteExercises(User user) {
        return diary.stream()
                .filter(note -> note.getUser().equals(user))
                .sorted(Comparator.comparing(NoteDiary::getDateTime))
                .toList();
    }

    /**
     * Предоставление выбранного по дате списка тренировок в дневнике
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок
     */
    public List<NoteDiary> getDateNoteExercises(User user, LocalDate date) {
        return diary.stream()
                .filter(note -> note.getUser().equals(user))
                .filter(noteDiary -> noteDiary.getDateTime().toLocalDate().equals(date))
                .sorted(Comparator.comparing(NoteDiary::getDateTime))
                .toList();
    }

    /**
     * Предоставление списка тренировок за текуще сутоки
     *
     * @param user аккаунт-владелец дневника
     * @return список тренировок за текуще сутоки
     */
    public List<NoteDiary> getToday(User user) {
        return diary.stream()
                .filter(note -> note.getUser().equals(user))
                .filter(note -> note.getDateTime().toLocalDate().equals(LocalDate.now()))
                .sorted(Comparator.comparing(NoteDiary::getDateTime))
                .toList();
    }

    /**
     * Удаление из дневника всех записей пользователя
     *
     * @param user удаляемый аккаунт пользователя
     */
    public void deleteDiary(User user) {
        diary.stream()
                .filter(noteDiary -> noteDiary.getUser().equals(user))
                .toList()
                .forEach(diary::remove);
    }

    /**
     * Проверка наличия записи тренировки в дневнике
     *
     * @param user проверяемый аккаунт пользователя
     * @param exercise проверяемый тип тренировки
     * @param exerciseTime время проверяемой тренировки
     * @return наличие записи тренировки в дневнике
     */
    public boolean isExerciseInDiary(User user, Exercise exercise, LocalDateTime exerciseTime) {
        return diary.stream()
                .filter(note -> note.getUser().equals(user))
                .filter(note -> note.getExercise().equals(exercise))
                .anyMatch(note -> note.getDateTime().equals(exerciseTime));
    }

    public static NoteDiaryDao getInstance() {
        return INSTANCE;
    }
}
