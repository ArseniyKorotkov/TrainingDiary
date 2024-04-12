package by.y_lab;

import by.y_lab.inOut.WorkPage;
import by.y_lab.p0util.StepApp;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;
import by.y_lab.p5controller.EnterController;
import by.y_lab.p5controller.SelectController;
import by.y_lab.p5controller.ShowDiaryController;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TreeSet;

public class TrainingDiary {

    private static final EnterController ENTER_CONTROLLER = EnterController.getInstance();
    private static final SelectController SELECT_CONTROLLER = SelectController.getInstance();
    private static final ShowDiaryController SHOW_DIARY_CONTROLLER = ShowDiaryController.getInstance();
    private static User userNow;
    private static StepApp stepApp = StepApp.REGISTRATION;

    public static void main(String[] args) {

        while (true) {
            switch (stepApp) {
                case REGISTRATION -> userNow = registrationAndAuthorization();
                case SELECT_FUNCTION -> selectFunction();
                case SHOW_DIARY_IN_DAYS -> showDiaryInDays();
            }
        }
    }

    private static void selectFunction() {
        String s = WorkPage.sayOptionsList().toLowerCase(Locale.ROOT);
        switch (s) {
            case "add exercise" ->
                SELECT_CONTROLLER.addExerciseInDiary(WorkPage.exerciseList(userNow),
                        WorkPage.howMuch(),
                        userNow);
            case "create exercise" -> SELECT_CONTROLLER.createExercise(WorkPage.createExercise(), userNow);
            case "check diary" -> {
                String answer = WorkPage.showDiary(userNow);
                if(answer.equalsIgnoreCase("Y") || answer.equalsIgnoreCase("YES")) {
                    stepApp = StepApp.SHOW_DIARY_IN_DAYS;
                }
            }
            case "admin" -> adminFunction(WorkPage.askAdminPassword());
            case "exit" -> stepApp = StepApp.REGISTRATION;
            default -> System.out.println("select from the list provided");
        }
    }

    private static void showDiaryInDays() {
        List<TreeSet<NoteExerciseInDiary>> diaryInDays = SHOW_DIARY_CONTROLLER.getDiaryInDays(WorkPage.askDays(), userNow);
        WorkPage.showTrainingDays(diaryInDays);
        stepApp = StepApp.SELECT_FUNCTION;
    }

    private static void adminFunction(String password) {
        if(password.equals("iAmAdmin")) {
            WorkPage.showAllUsers(SELECT_CONTROLLER.getAllUsers());
        }
    }

    private static User registrationAndAuthorization() {
        String answer;
        while (true) {
            WorkPage.startConversation();
            answer = WorkPage.takeAnswerAboutAccount();
            if (answer.equals("Y") || answer.equals("YES")) {
                Optional<User> userOptional = ENTER_CONTROLLER.getUser(WorkPage.Authorization());
                if (userOptional.isEmpty()) {
                    WorkPage.showAnswers(ENTER_CONTROLLER.getBadAnswers());
                } else {
                    stepApp = StepApp.SELECT_FUNCTION;
                    return userOptional.get();
                }
            } else if (answer.equals("N") || answer.equals("NO")) {
                Optional<User> userOptional = ENTER_CONTROLLER.createUser(WorkPage.Registration());
                if (userOptional.isEmpty()) {
                    WorkPage.showAnswers(ENTER_CONTROLLER.getBadAnswers());
                } else {
                    stepApp = StepApp.SELECT_FUNCTION;
                    return userOptional.get();
                }
            } else {
                WorkPage.questionYesOrNo();
            }

        }
    }
}
