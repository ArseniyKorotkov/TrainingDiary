package by.yLab.service;

import by.yLab.util.Action;
import by.yLab.entity.Audit;
import by.yLab.entity.User;
import by.yLab.dao.AuditDao;

import java.util.List;

public class AuditService {

    private static final AuditService INSTANCE = new AuditService();
    private AuditDao auditDao = AuditDao.getInstance();

    private AuditService() {
    }

    /**
     * Добавление записи аудита
     *
     * @param user   совершивший действие пользователь
     * @param action действие пользователя
     */
    public void addAction(User user, Action action) {
        auditDao.addAction(user, action);
    }

    /**
     * Запрос на список действий пользователя
     *
     * @param user запрашиваемый пользователь
     * @return список действий пользователя
     */
    public List<Audit> getAuditUser(User user) {
        return auditDao.getAuditUser(user);
    }

    public static AuditService getInstance() {
        return INSTANCE;
    }
}
