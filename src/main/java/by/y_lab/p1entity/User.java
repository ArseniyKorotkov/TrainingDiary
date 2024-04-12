package by.y_lab.p1entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class User {

    private final Diary DIARY = new Diary();
    private final List<Exercise> exercises = new ArrayList<>();

    private String firstName;
    private String lastName;
    private LocalDate birthday;
    private String email;
    private LocalDate registrationDay;

    public User(String firstName, String lastName, LocalDate birthday, String email, LocalDate registrationDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.registrationDay = registrationDay;
    }

    public Diary getDiary() {
        return DIARY;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public LocalDate getRegistrationDate() {
        return registrationDay;
    }

    public void setRegistrationDay(LocalDate registrationDay) {
        this.registrationDay = registrationDay;
    }
}
