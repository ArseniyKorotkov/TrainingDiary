package by.yLab.entity;

import by.yLab.util.FormatDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Класс аккаунта пользователя
 */
@Getter
@Setter
@EqualsAndHashCode
public class User {

    private static final String USER_TO_STRING_FORMAT = "User %s %s, \nbirthday %s, email \"%s\", \nregistration date %s";

    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String email;
    private LocalDate registrationDate;

    public User(String firstName, String lastName, LocalDate birthday, String email, LocalDate registrationDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.registrationDate = registrationDay;
    }

    @Override
    public String toString() {
        return USER_TO_STRING_FORMAT.formatted(firstName, lastName, birthday.format(FormatDateTime.reformDate()),
                email, registrationDate.format(FormatDateTime.reformDate()));


    }
}
