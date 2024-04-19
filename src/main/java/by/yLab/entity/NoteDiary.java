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
    private long userId;
    @EqualsAndHashCode.Include
    private long exerciseId;
    @EqualsAndHashCode.Include
    private LocalDateTime dateTime;
    private int timesCount;

    public NoteDiary(long userId, long exerciseId, LocalDateTime dateTime, int timesCount) {
        this.userId = userId;
        this.exerciseId = exerciseId;
        this.dateTime = dateTime;
        this.timesCount = timesCount;
    }
}
