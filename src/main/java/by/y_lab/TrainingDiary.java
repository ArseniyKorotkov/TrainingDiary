package by.y_lab;

import static by.y_lab.p0util.Auditor.showActions;
import static by.y_lab.p0util.SelectionItems.*;
import static by.y_lab.p0util.Auditor.writeAction;

import by.y_lab.inOut.*;
import by.y_lab.p0util.FormatDateTime;
import by.y_lab.p1entity.Exercise;
import by.y_lab.p1entity.NoteExerciseInDiary;
import by.y_lab.p1entity.User;
import by.y_lab.p3dto.ExerciseDto;
import by.y_lab.p3dto.NoteExerciseInDiaryDto;
import by.y_lab.p3dto.UserDto;
import by.y_lab.p5controller.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * @author Arseni Karatkou
 * @version 1.1
 */

public class TrainingDiary {

    private static final LoginController LOGIN_CONTROLLER = LoginController.getInstance();
    private static final DiaryController DIARY_CONTROLLER = DiaryController.getInstance();
    private static final ExerciseController EXERCISE_CONTROLLER = ExerciseController.getInstance();
    private static final AdminController ADMIN_CONTROLLER = AdminController.getInstance();

    private static User userNow;
    private static DisplayedPage displayedPage = DisplayedPage.LOGIN;

    /**
     * @param args не используется.
     *             Входная точка приложения
     */
    public static void main(String[] args) {
        changePages();
    }

    /**
     * Переход по логическим частям приложения
     */
    private static void changePages() {
        while (true) {
            switch (displayedPage) {
                case LOGIN -> userNow = registrationAndAuthorization();
                case MENU -> selectableMenuItems();
                case ADD_EXERCISE_TO_DIARY -> addExerciseToDiaryMenuItem();
                case CREATE_EXERCISE -> createExerciseMenuItem();
                case SHOW_DIARY -> showDiaryMenuItem();
                case SHOW_DIARY_TIME_SLICE -> showDiaryTimeSliceMenuItem();
                case ADMIN -> adminFunctions();
            }
        }
    }


    /**
     * Вход в систему.
     * Регистрация пользователя.
     * Авторизация пользователя
     */
    private static User registrationAndAuthorization() {
        String answer;
        while (true) {
            LoginPage.startConversation();
            answer = LoginPage.takeAnswerAboutAccount();
            if (answer.equals(SHORT_AGREE) || answer.equals(AGREE)) {
                Optional<User> userOptional = LOGIN_CONTROLLER.checkAccount(LoginPage.Authorization());
                if (userOptional.isEmpty()) {
                    LoginPage.showAnswers(LOGIN_CONTROLLER.getBadAnswers());
                } else {
                    displayedPage = DisplayedPage.MENU;
                    writeAction(userOptional.get(), "вошел в аккаунт");
                    return userOptional.get();
                }
            } else if (answer.equals(SHORT_REFUSE) || answer.equals(REFUSE)) {
                Optional<User> userOptional = LOGIN_CONTROLLER.createUser(LoginPage.Registration());
                if (userOptional.isEmpty()) {
                    LoginPage.showAnswers(LOGIN_CONTROLLER.getBadAnswers());
                } else {
                    displayedPage = DisplayedPage.MENU;
                    writeAction(userOptional.get(), "создал аккаунт и вошел");
                    return userOptional.get();
                }
            } else {
                LoginPage.questionYesOrNo();
            }

        }
    }


    /**
     * Переход по частям приложения через основное меню по полученным от пользователя запросам
     */
    private static void selectableMenuItems() {
        switch (MenuOptionsPage.sayOptionsList().toLowerCase(Locale.ROOT)) {
            case ADD_EXERCISE_TO_DIARY -> displayedPage = DisplayedPage.ADD_EXERCISE_TO_DIARY;
            case CREATE_EXERCISE -> displayedPage = DisplayedPage.CREATE_EXERCISE;
            case SHOW_DIARY -> displayedPage = DisplayedPage.SHOW_DIARY;
            case ADMIN -> displayedPage = DisplayedPage.ADMIN;
            case EXIT -> displayedPage = DisplayedPage.LOGIN;
            default -> MenuOptionsPage.nonMenuMessage();
        }
    }

    /**
     * Пункт основного меню.
     * Добавление актуальной тренировки в дневник.
     * Сохранение в текущем моменте времени с указанием количества единиц выполнения
     *
     * @see TrainingDiary#changePages()
     */
    private static void addExerciseToDiaryMenuItem() {
        Set<Exercise> userExercises = EXERCISE_CONTROLLER.getUserExercises(userNow);
        Optional<NoteExerciseInDiaryDto> noteExerciseInDiaryDto = AddExerciseInDiaryPage.addExerciseInDiary(userExercises);
        if (noteExerciseInDiaryDto.isPresent()) {
            Optional<Exercise> exerciseOptional =
                    EXERCISE_CONTROLLER.getExerciseToName(userNow, noteExerciseInDiaryDto.get().getExerciseName());
            if (exerciseOptional.isPresent()) {
                DIARY_CONTROLLER.addExercise(userNow,
                        exerciseOptional.get(),
                        noteExerciseInDiaryDto.get().getTimesCount());
                writeAction(userNow, "добавил тип тренировки " + exerciseOptional.get().getExerciseName() + " в дневник");
                AddExerciseInDiaryPage.agreeMessage();
            } else {
                writeAction(userNow, "попытался добавить несозданный тип тренировки в дневник");
                AddExerciseInDiaryPage.refuseMessage();
            }
        }
        displayedPage = DisplayedPage.MENU;

    }

    /**
     * Пункт основного меню.
     * Создание нового типа тренировки, расширение перечня типов тренировок.
     * Включает в себя название тренировки и ее энергозатратность в единицу выполнения
     *
     * @see TrainingDiary#changePages()
     */
    private static void createExerciseMenuItem() {
        ExerciseDto exerciseDto = CreateExercisePage.createExercise();
        Exercise exercise = new Exercise(exerciseDto.getExerciseName(), exerciseDto.getCaloriesBurnInHour());
        boolean isExerciseNew = EXERCISE_CONTROLLER.isExerciseNew(userNow, exercise);
        EXERCISE_CONTROLLER.createExercise(userNow,
                exerciseDto.getExerciseName(),
                exerciseDto.getCaloriesBurnInHour());
        if (isExerciseNew) {
            CreateExercisePage.exerciseCreated(exercise);
            writeAction(userNow, "создал новый тип тренировки " + exercise.getExerciseName());
        } else {
            CreateExercisePage.exerciseUpdated(exercise);
            writeAction(userNow, "обновил тип тренировки " + exercise.getExerciseName());
        }
        displayedPage = DisplayedPage.MENU;
    }

    /**
     * Пункт основного меню.
     * Просмотр тренировок за текущие сутки.
     * Вывод суммы сожженных калорий за текущие сутки.
     *
     * @see TrainingDiary#changePages()
     */
    private static void showDiaryMenuItem() {
        TreeSet<NoteExerciseInDiary> lastDay = DIARY_CONTROLLER.getLastDay(userNow);
        int burnCalories = DIARY_CONTROLLER.getBurnCalories(List.of(lastDay));
        String answer = ShowDiaryPage.showDiary(lastDay, burnCalories);
        writeAction(userNow, "просмотрел свою текущую активность");
        if (answer.equalsIgnoreCase(SHORT_AGREE) || answer.equalsIgnoreCase(AGREE)) {
            displayedPage = DisplayedPage.SHOW_DIARY_TIME_SLICE;
        } else {
            displayedPage = DisplayedPage.MENU;
        }

    }

    /**
     * Подпункт просмотра тренировок
     * Просмотр тренировок в прошедший промежуток времени отсортированные по дате.
     * Реализация возможности получения статистики по тренировкам выводя количество потраченных калорий в конце списка
     * Если заданный промежуток времени выходит за рамки пребывания в системе, он ограничивается этими рамками
     *
     * @see TrainingDiary#showDiaryMenuItem()
     */
    private static void showDiaryTimeSliceMenuItem() {
        List<TreeSet<NoteExerciseInDiary>> diaryTimeSlice =
                DIARY_CONTROLLER.getDiaryTimeSlice(ShowDiaryTimeSlicePage.askTimeSlice(), userNow);
        int burnCalories = DIARY_CONTROLLER.getBurnCalories(diaryTimeSlice);
        ShowDiaryTimeSlicePage.showTrainingDays(diaryTimeSlice, burnCalories);
        writeAction(userNow, "просмотрел свою прошлую активность");
        String answer = ShowDiaryTimeSlicePage.diaryMenu();
        switch (answer) {
            case ADD -> addMissingExerciseDairy();
            case DELETE -> deleteExerciseDairy();
            default -> displayedPage = DisplayedPage.MENU;
        }
    }

    /**
     * Подпункт правок тренировок в прошедшем промежутке времени
     * Добавление неактуальной тренировки в дневник
     *
     * @see TrainingDiary#showDiaryTimeSliceMenuItem()
     */
    private static void addMissingExerciseDairy() {
        LocalDateTime exerciseTime = LocalDateTime.parse(ShowDiaryTimeSlicePage.askDate(), FormatDateTime.reformDateTime());
        Set<Exercise> userExercises = EXERCISE_CONTROLLER.getUserExercises(userNow);
        Optional<NoteExerciseInDiaryDto> noteExerciseInDiaryDto = AddExerciseInDiaryPage.addExerciseInDiary(userExercises);
        if (noteExerciseInDiaryDto.isPresent()) {
            Optional<Exercise> exerciseOptional =
                    EXERCISE_CONTROLLER.getExerciseToName(userNow, noteExerciseInDiaryDto.get().getExerciseName());
            if (exerciseOptional.isPresent()) {
                if (DIARY_CONTROLLER.addMissingExercise(userNow,
                        exerciseOptional.get(),
                        noteExerciseInDiaryDto.get().getTimesCount(),
                        exerciseTime)) {

                    AddExerciseInDiaryPage.agreeMessage();
                    writeAction(userNow, "внес изменения в прошлую активность");
                } else {
                    writeAction(userNow, "попытался внести изменения в прошлую активность");
                    AddExerciseInDiaryPage.refuseDateMessage();
                }
            } else {
                AddExerciseInDiaryPage.refuseMessage();
                writeAction(userNow, "попытался внести изменения в прошлую активность");
            }
        }
        displayedPage = DisplayedPage.MENU;

    }

    /**
     * Подпункт правок тренировок в прошедшем промежутке времени
     * Удаление неактуальной тренировки из дневника
     *
     * @see TrainingDiary#showDiaryTimeSliceMenuItem()
     */
    private static void deleteExerciseDairy() {
        LocalDateTime exerciseDate = LocalDateTime.parse(ShowDiaryTimeSlicePage.askDate(), FormatDateTime.reformDateTime());
        Set<Exercise> userExercises = EXERCISE_CONTROLLER.getUserExercises(userNow);
        Optional<String> stringOptional = ShowDiaryTimeSlicePage.selectExerciseToDelete(userExercises);
        if (stringOptional.isPresent()) {
            Optional<Exercise> exerciseOptional =
                    EXERCISE_CONTROLLER.getExerciseToName(userNow, stringOptional.get());
            if (exerciseOptional.isPresent() && DIARY_CONTROLLER
                    .isExerciseInDiary(userNow, exerciseOptional.get(), exerciseDate)) {
                DIARY_CONTROLLER.deleteExercise(userNow,
                        exerciseOptional.get(), exerciseDate.toLocalDate());
                ShowDiaryTimeSlicePage.agreeMessage();
            } else {
                ShowDiaryTimeSlicePage.refuseMessage();
            }
        }
        displayedPage = DisplayedPage.MENU;
    }

    /**
     * Пункт основного меню.
     * Доступ пользователю к возможностям администратора через пароль.
     * Реализация контроля прав пользователя
     */
    private static void adminFunctions() {
        if (ADMIN_CONTROLLER.checkPassword(AdminPage.askAdminPassword())) {
            String answer = AdminPage.showAllUsers(userNow, ADMIN_CONTROLLER.getAllUsers());
            switch (answer) {
                case SHOW_USERS_DATA -> showUserFromAdmin();
                case DELETE_USER -> deleteUserFromAdmin();
                case SHOW_AUDIT_FILES -> showAudit();
                case EXIT -> displayedPage = DisplayedPage.MENU;
            }
        } else {
            AdminPage.refuseAnswer();
            displayedPage = DisplayedPage.MENU;
        }
    }


    /**
     * Подпункт возможностей администратора
     * Просмотр списка всех пользователей со списками их тренировок
     *
     * @see TrainingDiary#adminFunctions()
     */
    private static void showUserFromAdmin() {
        UserDto userDto = AdminPage.getUserDto();
        Optional<User> userOptional = ADMIN_CONTROLLER.getUser(userDto.getLastName(), userDto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String registrationDate = user.getRegistrationDate().format(FormatDateTime.reformDate());
            String timeSlice = registrationDate + REGEX + LocalDate.now().format(FormatDateTime.reformDate());
            List<TreeSet<NoteExerciseInDiary>> diaryTimeSlice = DIARY_CONTROLLER.getDiaryTimeSlice(timeSlice, user);
            AdminPage.showUser(user, diaryTimeSlice);
            writeAction(userNow, "просмотрел активность пользователя " + user.getLastName()
                                 + " with email " + user.getEmail() + " на правах администратора");
        } else {
            AdminPage.noFindUserAnswer();
            writeAction(userNow, "попытался просмотреть активность пользователя " + userDto.getLastName()
                                 + " with email " + userDto.getEmail() + " на правах администратора");
        }
    }

    /**
     * Подпункт возможностей администратора
     * Просмотр списка всех действий пользователей приложения
     */
    private static void showAudit() {
        showActions();
    }

    /**
     * Подпункт возможностей администратора
     * Удаление из списка всех пользователей
     *
     * @see TrainingDiary#adminFunctions()
     */
    private static void deleteUserFromAdmin() {
        UserDto userDto = AdminPage.getUserDto();
        Optional<User> userOptional = ADMIN_CONTROLLER.getUser(userDto.getLastName(), userDto.getEmail());
        userOptional.ifPresentOrElse(user -> {
            ADMIN_CONTROLLER.deleteUser(user);
            writeAction(userNow, "удаление пользователя " + user.getLastName() + " with email " + user.getEmail()
                                 + " на правах администратора");
        }, AdminPage::noFindUserAnswer);
    }
}
