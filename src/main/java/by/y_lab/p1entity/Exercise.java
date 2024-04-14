package by.y_lab.p1entity;

import lombok.Getter;

import java.util.Objects;

/**
 * Класс типа тренировки
 */
@Getter
public class Exercise {

    private final String exerciseName;
    private final int caloriesBurnInHour;

    public Exercise(String exerciseName, int caloriesBurnInHour) {
        this.exerciseName = exerciseName;
        this.caloriesBurnInHour = caloriesBurnInHour;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(exerciseName, exercise.exerciseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseName);
    }
}
