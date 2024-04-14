package by.y_lab.p3dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Контейнер для введенных данных пользователя о записываемой тренировке
 */
@Getter
@Setter
public class NoteExerciseInDiaryDto {

    private String exerciseName;
    private int timesCount;

    public NoteExerciseInDiaryDto(String exerciseName, int timesCount) {
        this.exerciseName = exerciseName;
        this.timesCount = timesCount;
    }

}
