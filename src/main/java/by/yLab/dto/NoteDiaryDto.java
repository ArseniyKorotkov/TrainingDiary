package by.yLab.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Контейнер для введенных данных пользователя о записываемой тренировке
 */
@Getter
@Setter
public class NoteDiaryDto {

    private String exerciseName;
    private int timesCount;

    public NoteDiaryDto(String exerciseName, int timesCount) {
        this.exerciseName = exerciseName;
        this.timesCount = timesCount;
    }

}
