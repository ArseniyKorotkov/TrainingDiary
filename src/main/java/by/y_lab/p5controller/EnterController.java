package by.y_lab.p5controller;

import by.y_lab.p0util.FormatDate;
import by.y_lab.p1entity.User;
import by.y_lab.p4service.UserService;
import by.y_lab.p3dto.UserDto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class EnterController {

    private static final EnterController INSTANCE = new EnterController();
    private final UserService userService = UserService.getInstance();
    private final ArrayList<String> badAnswers = new ArrayList<>();

    private EnterController() {}

    public Optional<User> createUser(UserDto userDto) {
        Optional<User> optionalUser = Optional.empty();
        if (checkBirthday(userDto.getBirthday()) && checkEmail(userDto.getEmail())) {

            User user = new User(userDto.getFirstName(),
                    userDto.getLastName(),
                    LocalDate.parse(userDto.getBirthday(), FormatDate.reform()),
                    userDto.getEmail(),
                    LocalDate.now());

            user.getDiary().nextDays();

            if (isUserInBase(user)) {
                badAnswers.add("this account already used");
            } else {
                userService.addUser(user);
                optionalUser = Optional.of(user);
            }
        }
        return optionalUser;
    }

    public Optional<User> getUser(UserDto userDto) {
        User user = new User(null, userDto.getLastName(), null, userDto.getEmail(), null);
        return userService.findUser(user);
    }

    private boolean checkBirthday(String birthday) {
        boolean isGoodEmail = true;
        try {
            LocalDate.parse(birthday, FormatDate.reform());
        } catch (Exception e) {
            badAnswers.add("bad birthday format. Use DAY.MONTH.YEAR");
            isGoodEmail = false;
        }
        return isGoodEmail;
    }

    private boolean checkEmail(String email) {
        boolean isGoodEmail = false;
        char[] emailLetters = email.toCharArray();
        boolean haveDog = false;
        boolean haveDot = false;
        for (char letter : emailLetters) {
            if (letter == '@') {
                haveDog = true;
            }
            if (letter == '.' && haveDog) {
                haveDot = true;
            }
        }
        if (haveDog && haveDot) {
            isGoodEmail = true;
        } else {
            badAnswers.add("not correctly email");
        }
        return isGoodEmail;
    }

    public boolean isUserInBase(User user) {
        return userService.isUserInBase(user);

    }

    public static EnterController getInstance() {
        return INSTANCE;
    }

    public ArrayList<String> getBadAnswers() {
        return badAnswers;
    }
}
