package by.y_lab.p4service;

import by.y_lab.p1entity.User;
import by.y_lab.p2dao.UserDao;

import java.util.HashSet;
import java.util.Optional;

public class UserService {

    private static final UserService INSTANCE = new UserService();
    private final UserDao userDao = UserDao.getInstance();

    public boolean isUserInBase(User user) {
        return userDao.isUserRegistered(user);
    }

    public Optional<User> findUser(User user) {
        return userDao.findUser(user);
    }

    public void addUser(User user) {
        userDao.addUser(user);
    }

    public HashSet<User> getAllUsers() {
        return userDao.getUsers();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }
}
