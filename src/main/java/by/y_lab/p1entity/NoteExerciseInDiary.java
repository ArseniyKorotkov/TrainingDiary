package by.y_lab.p1entity;

import by.y_lab.p0util.FormatDate;

import java.time.LocalDateTime;
import java.util.Objects;

public class NoteExerciseInDiary implements Comparable<NoteExerciseInDiary> {

    private Exercise exercise;
    private LocalDateTime dateTime;
    private int timesCount;

    public NoteExerciseInDiary(Exercise exercise, LocalDateTime dateTime, int timesCount) {
        this.exercise = exercise;
        this.dateTime = dateTime;
        this.timesCount = timesCount;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getTimesCount() {
        return timesCount;
    }

    public void setTimesCount(int timesCount) {
        this.timesCount = timesCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteExerciseInDiary that = (NoteExerciseInDiary) o;
        return Objects.equals(exercise, that.exercise);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exercise);
    }

    @Override
    public int compareTo(NoteExerciseInDiary o) {
        return dateTime.getHour() * 60 + dateTime.getMinute() - o.getDateTime().getHour() * 60 + o.getDateTime().getMinute();
    }

    @Override
    public String toString() {
        return "exercise '" + exercise.getExerciseName()
               + "' done " + timesCount
               + " times in day "
               + dateTime.format(FormatDate.reform())
               + ". " + exercise.getCaloriesBurnInHour() * timesCount + " calories burned";
    }
}
