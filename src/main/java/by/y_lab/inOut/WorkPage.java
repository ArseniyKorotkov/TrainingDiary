package by.y_lab.inOut;

import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;
import by.y_lab.p3dto.UserDto;

import java.util.*;

public class WorkPage {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String USER_DATA_REQUEST = "Enter your %s, please ";

    public static void startConversation() {
        System.out.println("Hello, sportsman!");
        System.out.print("Do you have an account? Y/N ");
    }

    public static String takeAnswerAboutAccount() {
        return SCANNER.nextLine().toUpperCase(Locale.ROOT);
    }

    public static UserDto Registration() {
        System.out.println("Registration:");
        requestData("first name");
        String firstName = SCANNER.nextLine();
        requestData("last name");
        String lastName = SCANNER.nextLine();
        requestData("birthday");
        String birthday = SCANNER.nextLine();
        requestData("email");
        String email = SCANNER.nextLine();
        return new UserDto(firstName, lastName, birthday, email);
    }

    public static UserDto Authorization() {
        System.out.println("Authorization:");
        requestData("email");
        String email = SCANNER.nextLine();
        requestData("last name like password");
        String lastName = SCANNER.nextLine();
        return new UserDto(lastName, email);
    }

    public static void showAnswers(ArrayList<String> answers) {
        for (String answer : answers) {
            System.out.println(answer);
        }
        answers.clear();
    }

    public static void questionYesOrNo() {
        System.out.println("Yes or no?");
    }

    public static String sayOptionsList() {
        System.out.println("Options list:");
        System.out.println("Add exercise");
        System.out.println("Create exercise");
        System.out.println("Check diary");
        System.out.println("Admin");
        System.out.println("Exit");
        return SCANNER.nextLine();
    }

    public static Integer howMuch() {
        System.out.println("How much");
        return Integer.parseInt(SCANNER.nextLine());
    }

    public static String exerciseList(User user) {
        System.out.println("Select from:");
        for (Exercise exercise : user.getExercises()) {
            System.out.println(exercise.getExerciseName());
        }
        return SCANNER.nextLine();

    }

    public static Exercise createExercise() {
        System.out.println("Enter name new exercise:");
        String name = SCANNER.nextLine();
        System.out.println("Enter burn calories on one time");
        int calories = Integer.parseInt(SCANNER.nextLine());
        return new Exercise(name, calories);
    }

    public static String showDiary(User user) {
        for (NoteExerciseInDiary noteExerciseInDiary : user.getDiary().getLastDay()) {
            System.out.println(noteExerciseInDiary);
        }
        System.out.println("Do you want more information? Y/N");
        return SCANNER.nextLine();
    }

    public static String askAdminPassword() {
        System.out.println("enter admin password");
        return SCANNER.nextLine();
    }

    public static void showAllUsers(HashSet<User> users) {
        for (User user : users) {
            System.out.println(user.getFirstName() + " " + user.getLastName());
        }
    }

    public static String askDays() {
        System.out.println("Enter days format dd.mm.yyyy/dd.mm.yyyy");
        return SCANNER.nextLine();
    }

    public static void showTrainingDays(List<TreeSet<NoteExerciseInDiary>> trainingInDaysList) {
        for (TreeSet<NoteExerciseInDiary> oneTrainingDay : trainingInDaysList) {
            oneTrainingDay.forEach(System.out::println);
        }
    }


    private static void requestData(String data) {
        System.out.printf(USER_DATA_REQUEST, data);
    }
}
