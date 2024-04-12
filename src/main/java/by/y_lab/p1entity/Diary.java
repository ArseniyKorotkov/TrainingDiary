package by.y_lab.p1entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class Diary {

    private final List<TreeSet<NoteExerciseInDiary>> diaryList = new ArrayList<>();


    public void addExercise(Exercise exercise, int times) {
        nextDays();
        NoteExerciseInDiary noteExerciseInDiary = new NoteExerciseInDiary(exercise, LocalDateTime.now(), times);
        if (getLastDay().contains(noteExerciseInDiary)) {
            NoteExerciseInDiary ceiling = getLastDay().ceiling(noteExerciseInDiary);
            times += ceiling.getTimesCount();
        }
        getLastDay().add(new NoteExerciseInDiary(exercise, LocalDateTime.now(), times));
    }

    public void editExercise(Exercise exercise, LocalDateTime dateTime, int times) {
        NoteExerciseInDiary noteExerciseInDiary = new NoteExerciseInDiary(exercise, dateTime, times);
        if (getLastDay().contains(noteExerciseInDiary)) {
            NoteExerciseInDiary exerciseInDiary = getLastDay().ceiling(noteExerciseInDiary);
            exerciseInDiary.setDateTime(dateTime);
            exerciseInDiary.setTimesCount(times);
            getLastDay().add(exerciseInDiary);
        }
    }

    public void nextDays() {
        if (diaryList.isEmpty()) {
            diaryList.add(new TreeSet<>());
            getLastDay().add(new NoteExerciseInDiary(new Exercise("registration", 0),
                    LocalDateTime.now(), 1));
        } else {
            for (int i = 0; i < ChronoUnit.DAYS.between(getLastDay().last().getDateTime().toLocalDate(), LocalDate.now()); i++) {
                diaryList.add(new TreeSet<>());
            }
        }

    }

    public TreeSet<NoteExerciseInDiary> getLastDay() {
        return diaryList.get(getDiaryList().size() - 1);
    }

    public List<TreeSet<NoteExerciseInDiary>> getDiaryList() {
        return diaryList;
    }
}
