package by.yLab.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Класс типа тренировки
 */
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Exercise {

    @EqualsAndHashCode.Include
    private final String exerciseName;
    private final int caloriesBurnInHour;

    public Exercise(String exerciseName, int caloriesBurnInHour) {
        this.exerciseName = exerciseName;
        this.caloriesBurnInHour = caloriesBurnInHour;
    }
}
