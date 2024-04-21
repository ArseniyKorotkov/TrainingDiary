package by.yLab.dao;

import by.yLab.util.Action;
import by.yLab.entity.Audit;
import by.yLab.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AuditDao {

    private static final AuditDao INSTANCE = new AuditDao();

    private final List<Audit> auditList = new ArrayList<>();

    private AuditDao() {
    }

    /**
     * Добавление записи аудита
     *
     * @param user   совершивший действие пользователь
     * @param action действие пользователя
     */
    public void addAction(User user, Action action) {
        auditList.add(new Audit(user, action, LocalDateTime.now()));
    }

    /**
     * Запрос на список действий пользователя
     *
     * @param user запрашиваемый пользователь
     * @return список действий пользователя
     */
    public List<Audit> getAuditUser(User user) {
        return auditList.stream()
                .filter(audit -> audit.getUser().equals(user))
                .toList();
    }

    public static AuditDao getInstance() {
        return INSTANCE;
    }
}
