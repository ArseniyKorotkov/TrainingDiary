package by.y_lab.p3dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Контейнер для введенных данных пользователя о создаваемой тренировке
 */
@Getter
@Setter
public class ExerciseDto {

    private String exerciseName;
    private int caloriesBurnInHour;

    public ExerciseDto(String exerciseName, int caloriesBurnInHour) {
        this.exerciseName = exerciseName;
        this.caloriesBurnInHour = caloriesBurnInHour;
    }

}
