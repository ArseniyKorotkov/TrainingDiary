package by.y_lab.p5controller;

import static by.y_lab.p0util.SelectionItems.*;

import by.y_lab.p0util.FormatDateTime;
import by.y_lab.p1entity.User;
import by.y_lab.p4service.DiaryService;
import by.y_lab.p4service.UserService;
import by.y_lab.p3dto.UserDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Обработка введенных данных на этапе входа в систему
 */
public class LoginController {

    private static final LoginController INSTANCE = new LoginController();
    public static final String NOT_CORRECTLY_EMAIL = "not correctly email";
    public static final String BAD_BIRTHDAY_FORMAT = "bad birthday format. Use DAY.MONTH.YEAR";
    public static final String NOT_HAVE_SO_ACCOUNT = "not have so account";
    public static final char DOT = '.';
    public static final char AT = '@';


    private final UserService userService = UserService.getInstance();
    private final DiaryService diaryService = DiaryService.getInstance();

    private final ArrayList<String> badAnswers = new ArrayList<>();

    private LoginController() {
    }

    /**
     * Регистрация пользователя. Создание для него дневника тренировок.
     *
     * @param userDto сведения, введенные пользователем при регистрации
     * @return созданный аккаунт при корректно введенных пользователем данных
     */
    public Optional<User> createUser(UserDto userDto) {
        Optional<User> optionalUser = Optional.empty();
        if (checkBirthday(userDto.getBirthday()) && checkEmail(userDto.getEmail())) {

            User user = new User(userDto.getFirstName(),
                    userDto.getLastName(),
                    LocalDate.parse(userDto.getBirthday(), FormatDateTime.reformDate()),
                    userDto.getEmail(),
                    LocalDate.now());

            diaryService.createDiary(user);

            if (isUserInBase(user)) {
                badAnswers.add(ACCOUNT_ALREADY_USED);
            } else {
                userService.addUser(user);
                optionalUser = Optional.of(user);
            }
        }
        return optionalUser;
    }

    /**
     * Возвращение пользователя к ранее созданному аккаунту
     *
     * @param userDto сведения, введенные пользователем
     * @return аккаунт пользователя из точки хранения аккаунтов
     */
    public Optional<User> checkAccount(UserDto userDto) {
        Optional<User> userOptional = userService.findUser(userDto.getLastName(), userDto.getEmail());
        if(userOptional.isEmpty()) {
            badAnswers.add(NOT_HAVE_SO_ACCOUNT);
        }
        return userOptional;
    }

    /**
     * Проверка на корректность введенных данных о дате рождения пользователя
     *
     * @param birthday сведения о дате рождения пользователя
     * @return сведения о корректности введенных данных
     */
    private boolean checkBirthday(String birthday) {
        boolean isGoodBirthday = true;
        try {
            LocalDate.parse(birthday, FormatDateTime.reformDate());
        } catch (Exception e) {
            badAnswers.add(BAD_BIRTHDAY_FORMAT);
            isGoodBirthday = false;
        }
        return isGoodBirthday;
    }

    /**
     * Проверка корректности введенных данных о почте пользователя
     *
     * @param email предоставляет приложению сведения о почте пользователя
     * @return сведения о корректности введенных данных
     */
    private boolean checkEmail(String email) {
        boolean isGoodEmail = false;
        char[] emailLetters = email.toCharArray();
        boolean haveAt = false;
        boolean haveDot = false;
        for (char letter : emailLetters) {
            if (letter == AT) {
                haveAt = true;
            }
            if (letter == DOT && haveAt) {
                haveDot = true;
            }
        }
        if (haveAt && haveDot) {
            isGoodEmail = true;
        } else {
            badAnswers.add(NOT_CORRECTLY_EMAIL);
        }
        return isGoodEmail;
    }

    /**
     * Проверка наличия аккаунта у пользователя
     *
     * @param user аккаунт пользователя
     * @return сведения о наличии аккаунта пользователя в списке зарегистрированных пользователей
     */
    public boolean isUserInBase(User user) {
        return userService.isUserInBase(user);

    }

    /**
     * Корректность предоставленных данных
     *
     * @return список ошибок предоставленных данных
     */
    public ArrayList<String> getBadAnswers() {
        return badAnswers;
    }

    public static LoginController getInstance() {
        return INSTANCE;
    }
}
