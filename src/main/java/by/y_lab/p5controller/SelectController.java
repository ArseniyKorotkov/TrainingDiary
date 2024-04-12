package by.y_lab.p5controller;

import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.User;
import by.y_lab.p4service.UserService;

import java.util.HashSet;
import java.util.Optional;

public class SelectController {

    private static final SelectController INSTANCE = new SelectController();

    private final UserService USER_SERVICE = UserService.getInstance();

    private SelectController() {}

    public void addExerciseInDiary(String exerciseName, int times, User user) {
        Optional<Exercise> optionalExercise = user.getExercises().stream().filter(exercise -> exercise.getExerciseName().equals(exerciseName)).findFirst();
        optionalExercise.ifPresentOrElse(exercise -> user.getDiary().addExercise(exercise, times),
                () -> System.out.println("Don`t have so exercise in list"));
    }

    public void createExercise(Exercise exercise, User user) {
        user.getExercises().add(exercise);
    }

    public HashSet<User> getAllUsers() {
        return USER_SERVICE.getAllUsers();
    }


    public static SelectController getInstance() {
        return INSTANCE;
    }


}
