package by.yLab.entity;

import by.yLab.util.FormatDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Класс записи в дневнике
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class NoteDiary {

    private static final String NOTE_EXERCISE_IN_DIARY_TO_STRING =
            "exercise '%s' done %s times in day %s. %s calories burned";

    @EqualsAndHashCode.Include
    private User user;
    @EqualsAndHashCode.Include
    private Exercise exercise;
    @EqualsAndHashCode.Include
    private LocalDateTime dateTime;
    private int timesCount;

    public NoteDiary(User user, Exercise exercise, LocalDateTime dateTime, int timesCount) {
        this.user = user;
        this.exercise = exercise;
        this.dateTime = dateTime;
        this.timesCount = timesCount;
    }

    @Override
    public String toString() {
        return NOTE_EXERCISE_IN_DIARY_TO_STRING.formatted(exercise.getExerciseName(),
                timesCount,
                dateTime.format(FormatDateTime.reformDateTime()),
                exercise.getCaloriesBurnInHour() * timesCount);
    }
}
