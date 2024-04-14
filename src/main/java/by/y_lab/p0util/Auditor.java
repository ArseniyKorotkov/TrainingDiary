package by.y_lab.p0util;

import by.y_lab.p1entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Auditor {

    private static final List<String> auditList = new ArrayList<>();

    public static void writeAction(User user, String action) {
        auditList.add(LocalDateTime.now().format(FormatDateTime.reformDateTime()) + ":: User " +
                      user.getFirstName() + " " + user.getLastName() + " with email " + user.getEmail() + ": " + action);
    }

    public static void showActions(String action) {
        auditList.forEach(System.out::println);
    }

}
