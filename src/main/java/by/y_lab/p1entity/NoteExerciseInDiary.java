package by.y_lab.p1entity;

import by.y_lab.p0util.FormatDateTime;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Класс записи в дневнике
 */
@Getter
@Setter
public class NoteExerciseInDiary {

    private static final String NOTE_EXERCISE_IN_DIARY_TO_STRING =
            "exercise '%s' done %s times in day %s. %s calories burned";
    private Exercise exercise;
    private LocalDateTime dateTime;
    private int timesCount;

    public NoteExerciseInDiary(Exercise exercise, LocalDateTime dateTime, int timesCount) {
        this.exercise = exercise;
        this.dateTime = dateTime;
        this.timesCount = timesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteExerciseInDiary that = (NoteExerciseInDiary) o;
        return Objects.equals(exercise.getExerciseName(), that.exercise.getExerciseName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(exercise.getExerciseName());
    }


    @Override
    public String toString() {
        return NOTE_EXERCISE_IN_DIARY_TO_STRING.formatted(exercise.getExerciseName(),
                timesCount,
                dateTime.format(FormatDateTime.reformDateTime()),
                exercise.getCaloriesBurnInHour() * timesCount);
    }
}
