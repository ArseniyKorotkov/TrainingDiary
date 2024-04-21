package by.yLab.util;

import lombok.Getter;

@Getter
public enum Action {

    REGISTRATION(" создал аккаунт и вошел ") ,
    AUTHORIZATION(" вошел в аккаунт "),
    WRITE_DIARY_NOTE(" добавил тренировку в дневник "),
    TRY_WRITE_DIARY_NOTE(" попытался добавить несозданный тип тренировки в дневник "),
    CRETE_EXERCISE(" создал новый тип тренировки "),
    UPDATE_EXERCISE("обновил тип тренировки"),
    SEE_TODAY_DIARY("просмотрел свою текущую активность"),
    SEE_BEFORE_DIARY(" просмотрел свою прошлую активность "),
    UPDATE_BEFORE_DIARY_NOTE(" внес изменения в прошлую активность "),
    TRY_UPDATE_BEFORE_DIARY_NOTE(" попытался внести изменения в прошлую активность "),
    DELETE_DIARY_NOTE(" удалил запись о прошлой активности "),
    TRY_DELETE_DIARY_NOTE(" попытался удалить запись р прошлой активности "),
    ENTER_ADMIN_MODE(" вошел как администратор ");

    private final String message;

    Action (String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}
