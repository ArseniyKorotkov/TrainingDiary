package by.yLab;

import static by.yLab.util.SelectionItems.*;

import by.yLab.inOut.*;
import by.yLab.util.Action;
import by.yLab.util.FormatDateTime;
import by.yLab.entity.Audit;
import by.yLab.entity.Exercise;
import by.yLab.entity.NoteDiary;
import by.yLab.entity.User;
import by.yLab.dto.ExerciseDto;
import by.yLab.dto.NoteDiaryDto;
import by.yLab.dto.UserDto;
import by.yLab.controller.*;

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
                    ADMIN_CONTROLLER.addAction(userOptional.get(), Action.AUTHORIZATION);
                    return userOptional.get();
                }
            } else if (answer.equals(SHORT_REFUSE) || answer.equals(REFUSE)) {
                Optional<User> userOptional = LOGIN_CONTROLLER.createUser(LoginPage.Registration());
                if (userOptional.isEmpty()) {
                    LoginPage.showAnswers(LOGIN_CONTROLLER.getBadAnswers());
                } else {
                    displayedPage = DisplayedPage.MENU;
                    ADMIN_CONTROLLER.addAction(userOptional.get(), Action.REGISTRATION);
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
        Optional<NoteDiaryDto> noteExerciseInDiaryDto = AddExerciseInDiaryPage.addExerciseInDiary(userExercises);
        if (noteExerciseInDiaryDto.isPresent()) {
            Optional<Exercise> exerciseOptional =
                    EXERCISE_CONTROLLER.getExerciseToName(userNow, noteExerciseInDiaryDto.get().getExerciseName());
            if (exerciseOptional.isPresent()) {
                DIARY_CONTROLLER.addExercise(userNow,
                        exerciseOptional.get(),
                        noteExerciseInDiaryDto.get().getTimesCount());
                ADMIN_CONTROLLER.addAction(userNow, Action.WRITE_DIARY_NOTE);
                AddExerciseInDiaryPage.agreeMessage();
            } else {
                ADMIN_CONTROLLER.addAction(userNow, Action.TRY_WRITE_DIARY_NOTE);
                AddExerciseInDiaryPage.refuseEmptyExercisesMessage();
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
            ADMIN_CONTROLLER.addAction(userNow, Action.WRITE_DIARY_NOTE);
        } else {
            CreateExercisePage.exerciseUpdated(exercise);
            ADMIN_CONTROLLER.addAction(userNow, Action.UPDATE_EXERCISE);
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
        List<NoteDiary> todayNote = DIARY_CONTROLLER.getLastDay(userNow);
        int burnCalories = DIARY_CONTROLLER.getBurnCalories(todayNote);
        String answer = ShowDiaryPage.showDiary(todayNote, burnCalories);
        ADMIN_CONTROLLER.addAction(userNow, Action.SEE_TODAY_DIARY);
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
        List<NoteDiary> diaryTimeSlice =
                DIARY_CONTROLLER.getDiaryTimeSlice(ShowDiaryTimeSlicePage.askTimeSlice(), userNow);
        int burnCalories = DIARY_CONTROLLER.getBurnCalories(diaryTimeSlice);
        ShowDiaryTimeSlicePage.showTrainingDays(diaryTimeSlice, burnCalories);
        ADMIN_CONTROLLER.addAction(userNow, Action.SEE_BEFORE_DIARY);
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
        Optional<NoteDiaryDto> noteExerciseInDiaryDto = AddExerciseInDiaryPage.addExerciseInDiary(userExercises);
        if (noteExerciseInDiaryDto.isPresent()) {
            Optional<Exercise> exerciseOptional =
                    EXERCISE_CONTROLLER.getExerciseToName(userNow, noteExerciseInDiaryDto.get().getExerciseName());
            if (exerciseOptional.isPresent()) {
                DIARY_CONTROLLER.addMissingExercise(userNow,
                        exerciseOptional.get(),
                        noteExerciseInDiaryDto.get().getTimesCount(),
                        exerciseTime);

                AddExerciseInDiaryPage.agreeMessage();
                ADMIN_CONTROLLER.addAction(userNow, Action.UPDATE_BEFORE_DIARY_NOTE);
            } else {
                ADMIN_CONTROLLER.addAction(userNow, Action.TRY_UPDATE_BEFORE_DIARY_NOTE);
                AddExerciseInDiaryPage.refuseNoSuchExerciseMessage();
            }
        } else {
            AddExerciseInDiaryPage.refuseEmptyExercisesMessage();
            ADMIN_CONTROLLER.addAction(userNow, Action.TRY_UPDATE_BEFORE_DIARY_NOTE);
        }
        displayedPage = DisplayedPage.MENU;
    }


    /**
     * Подпункт правок тренировок в прошедшем промежутке времени.
     * Удаление неактуальной тренировки из дневника.
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
                ADMIN_CONTROLLER.addAction(userNow, Action.DELETE_DIARY_NOTE);

            } else {
                ADMIN_CONTROLLER.addAction(userNow, Action.TRY_DELETE_DIARY_NOTE);
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
                case SHOW_AUDIT_USER_FILES -> showUserAudit();
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
            List<NoteDiary> diaryTimeSlice = DIARY_CONTROLLER.getDiaryTimeSlice(timeSlice, user);
            AdminPage.showUser(user, diaryTimeSlice);
        } else {
            AdminPage.noFindUserAnswer();
        }
    }

    /**
     * Подпункт возможностей администратора
     * Просмотр списка всех действий пользователя приложения
     *
     * @see TrainingDiary#adminFunctions()
     */
    private static void showUserAudit() {
        UserDto userDto = AdminPage.getUserDto();
        Optional<User> userOptional = ADMIN_CONTROLLER.getUser(userDto.getLastName(), userDto.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<Audit> auditUser = ADMIN_CONTROLLER.getAuditUser(user);
            AdminPage.showUserAudit(user, auditUser);
        } else {
            AdminPage.noFindUserAnswer();
        }
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
        userOptional.ifPresentOrElse(ADMIN_CONTROLLER::deleteUser, AdminPage::noFindUserAnswer);
    }
}
