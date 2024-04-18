package by.yLab.util;

import java.time.format.DateTimeFormatter;

/**
 * Форматтеры дат
 */
public final class FormatDateTime {

    private FormatDateTime() {
    }

    /**
     * @return форматтер даты
     */
    public static DateTimeFormatter reformDate() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    /**
     * @return форматтер даты и времени
     */
    public static DateTimeFormatter reformDateTime() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }
}
