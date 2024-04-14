package by.y_lab.p3dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Контейнер для введенных данных пользователя о своем аккаунте
 */
@Getter
@Setter
public class UserDto {
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;

    public UserDto(String firstName, String lastName, String birthday, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
    }

    public UserDto(String lastName, String email) {
        this.lastName = lastName;
        this.email = email;
    }
}
