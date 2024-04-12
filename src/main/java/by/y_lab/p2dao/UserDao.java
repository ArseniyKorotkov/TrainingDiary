package by.y_lab.p2dao;

import by.y_lab.p1entity.User;

import java.util.HashSet;
import java.util.Optional;

public class UserDao {
    private static final UserDao INSTANCE = new UserDao();

    private static final HashSet<User> USERS = new HashSet<>();

    public boolean isUserRegistered(User user) {
        return USERS.stream()
                .filter(user1 -> user.getLastName().equals(user1.getLastName()))
                .anyMatch(user1 -> user.getEmail().equals(user1.getEmail()));
    }

    public void addUser(User user) {
        USERS.add(user);
    }

    public HashSet<User> getUsers() {
        return USERS;
    }

    public Optional<User> findUser(User user) {
        if(isUserRegistered(user)) {

            return USERS.stream()
                    .filter(user1 -> user.getLastName().equals(user1.getLastName()))
                    .filter(user1 -> user.getEmail().equals(user1.getEmail()))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }


    public static UserDao getInstance() {
        return INSTANCE;
    }
}
