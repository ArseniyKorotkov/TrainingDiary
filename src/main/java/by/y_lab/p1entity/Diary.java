package by.y_lab.p1entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Класс дневника
 */
public class Diary {

    private final List<TreeSet<NoteExerciseInDiary>> diaryList = new ArrayList<>();

    /**
     * Добавление актуальной тренировки в дневник
     * @param exercise добавляемая в дневник тренировка
     * @param times количество единиц выполнения
     */
    public void addExercise(Exercise exercise, int times) {
        createNowDays();
        Optional<NoteExerciseInDiary> exerciseOptional = getLastDay()
                .stream()
                .filter(note -> note.getExercise().equals(exercise))
                .findFirst();

        if (exerciseOptional.isPresent()) {
            times += exerciseOptional.get().getTimesCount();
            getLastDay().remove(exerciseOptional.get());
        }
        getLastDay().add(new NoteExerciseInDiary(exercise, LocalDateTime.now(), times));
    }

    /**
     * Добавление неактуальной тренировки в дневник
     * @param user аккаунт-владелец дневника
     * @param exercise добавляемая в дневник тренировка
     * @param times количество единиц выполнения
     * @param exerciseTime дата и время проведенной тренировки
     * @return проверка временной корректности запрошенной даты
     */
    public boolean addExercise(User user, Exercise exercise, int times, LocalDateTime exerciseTime) {
        createNowDays();

        int between = (int) ChronoUnit.DAYS.between(user.getRegistrationDate(), exerciseTime.toLocalDate());
        if(between < 0 || ChronoUnit.DAYS.between(exerciseTime.toLocalDate(), LocalDateTime.now()) < 0) {
            return false;
        }

        Optional<NoteExerciseInDiary> exerciseOptional = getSelectedDay(between)
                .stream()
                .filter(note -> note.getExercise().equals(exercise))
                .findFirst();

        if (exerciseOptional.isPresent()) {
            getSelectedDay(between).remove(exerciseOptional.get());
            times += exerciseOptional.get().getTimesCount();
        }
        getSelectedDay(between).add(new NoteExerciseInDiary(exercise, LocalDateTime.now(), times));
        return true;
    }

    /**
     * Проверяет на наличие записи тренировки в определенный день
     * @param user аккаунт-владелец дневника
     * @param exercise проверяемая на наличие тренировка из дневника
     * @param exerciseTime время проверяемой на наличие тренировки из дневника
     * @return результат проверки
     */
    public boolean isExerciseInDiary(User user, Exercise exercise, LocalDateTime exerciseTime) {
        createNowDays();

        int between = (int) ChronoUnit.DAYS.between(user.getRegistrationDate(), exerciseTime.toLocalDate());
        if(between < 0 || ChronoUnit.DAYS.between(exerciseTime.toLocalDate(), LocalDateTime.now()) < 0) {
            return false;
        }

        return getSelectedDay(between)
                .stream()
                .filter(ex -> ex.getExercise().getExerciseName().equals(exercise.getExerciseName()))
                .anyMatch(note -> note.getDateTime().equals(exerciseTime));
    }

    /**
     * Удаление неактуальной тренировки из дневника
     * @param user аккаунт-владелец дневника
     * @param exercise удаляемая из дневника тренировка
     * @param exerciseDate дата и время удаляемой тренировки
     */
    public void deleteExercise(User user, Exercise exercise, LocalDate exerciseDate) {
        int between = (int) ChronoUnit.DAYS.between(user.getRegistrationDate(), exerciseDate);
        if(between < 0 || ChronoUnit.DAYS.between(exerciseDate, LocalDateTime.now()) < 0) {
            return;
        }
        TreeSet<NoteExerciseInDiary> selectedDay = getSelectedDay(between);
        Optional<NoteExerciseInDiary> deletedExercise = selectedDay
                .stream()
                .filter(note -> note.getExercise().equals(exercise))
                .findFirst();

        deletedExercise.ifPresent(selectedDay::remove);

    }

    /**
     * Добавление актуального количества дней в дневник
     */
    private void createNowDays() {
        if (diaryList.isEmpty()) {
            diaryList.add(new TreeSet<>(Comparator.comparing(NoteExerciseInDiary::hashCode)));
        } else {
            long between = ChronoUnit.DAYS.between(getLastDay().last().getDateTime().toLocalDate(), LocalDate.now());
            for (long i = 0; i < between; i++) {
                diaryList.add(new TreeSet<>(Comparator.comparing(NoteExerciseInDiary::hashCode)));
            }
        }

    }

    /**
     * Запрос последнего дня в дневнике
     * @return страницу текущих суток дневника
     */
    public TreeSet<NoteExerciseInDiary> getLastDay() {
        if(diaryList.isEmpty()){
            return new TreeSet<>(Comparator.comparing(NoteExerciseInDiary::hashCode));
        }
        return diaryList.get(getDiaryList().size() - 1);
    }

    /**
     * Запрос выбранного дня в дневнике
     * @param day номер дня от регистрации пользователя
     * @return страницу текущих суток дневника
     */
    public TreeSet<NoteExerciseInDiary> getSelectedDay(Integer day) {
        return diaryList.get(day);
    }

    /**
     * Предоставление полного списка тренировок в дневнике
     * @return список тренировок
     */
    public List<TreeSet<NoteExerciseInDiary>> getDiaryList() {
        return diaryList;
    }
}
