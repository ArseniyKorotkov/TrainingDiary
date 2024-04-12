package by.y_lab.p1entity;

import java.util.Objects;

public class Exercise {

    private String exerciseName;
    private int caloriesBurnInHour;

    public Exercise(String exerciseName, int caloriesBurnInHour) {
        this.exerciseName = exerciseName;
        this.caloriesBurnInHour = caloriesBurnInHour;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getCaloriesBurnInHour() {
        return caloriesBurnInHour;
    }

    public void setCaloriesBurnInHour(int caloriesBurnInHour) {
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
