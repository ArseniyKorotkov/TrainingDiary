package by.y_lab.inOut;

import by.y_lab.p3dto.UserDto;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Страница входа в систему. Регистрация пользователя, авторизация пользователя
 */
public class LoginPage extends Page {

    private static final String USER_DATA_REQUEST = "Enter your %s, please ";
    private static final String HELLO = "Hello, sportsman!";
    private static final String HAVE_ACCOUNT_QUEST = "Do you have an account? Y/N ";
    private static final String REGISTRATION = "Registration:";
    private static final String FIRST_NAME = "first name";
    private static final String LAST_NAME = "last name";
    private static final String BIRTHDAY = "birthday";
    private static final String EMAIL = "email";
    private static final String AUTHORIZATION = "Authorization:";
    private static final String LAST_NAME_LIKE_PASSWORD = "last name like password";
    private static final String YES_OR_NO = "Yes or no?";

    /**
     * Демонстрация слов приветствия
     */
    public static void startConversation() {
        System.out.println(HELLO);
        System.out.print(HAVE_ACCOUNT_QUEST);
    }

    /**
     * Демонстрация вопроса о наличии аккаунта у пользователя
     * @return ответ пользователя
     */
    public static String takeAnswerAboutAccount() {
        return SCANNER.nextLine().toUpperCase(Locale.ROOT);
    }

    /**
     * Запрос данных пользователя для создания аккаунта
     * @return dto контейнер с введенными данными
     */
    public static UserDto Registration() {
        System.out.println(REGISTRATION);
        requestData(FIRST_NAME);
        String firstName = SCANNER.nextLine();
        requestData(LAST_NAME);
        String lastName = SCANNER.nextLine();
        requestData(BIRTHDAY);
        String birthday = SCANNER.nextLine();
        requestData(EMAIL);
        String email = SCANNER.nextLine();
        System.out.println();
        return new UserDto(firstName, lastName, birthday, email);
    }

    /**
     * Запрос данных пользователя для входа в аккаунт
     * @return dto контейнер с введенными данными
     */
    public static UserDto Authorization() {
        System.out.println(AUTHORIZATION);
        requestData(EMAIL);
        String email = SCANNER.nextLine();
        requestData(LAST_NAME_LIKE_PASSWORD);
        String lastName = SCANNER.nextLine();
        System.out.println();
        return new UserDto(lastName, email);
    }

    /**
     * Демонстрация пользовательских ошибок при вводе данных
     * @param answers список пользовательских ошибок при вводе данных
     */
    public static void showAnswers(ArrayList<String> answers) {
        for (String answer : answers) {
            System.out.println(answer);
            System.out.println();
        }
        answers.clear();
    }

    /**
     * Предложение выбора пользователю
     */
    public static void questionYesOrNo() {
        System.out.println(YES_OR_NO);
    }

    private static void requestData(String data) {
        System.out.printf(USER_DATA_REQUEST, data);
    }
}
