package by.y_lab.p0util;

import java.time.format.DateTimeFormatter;

/**
 * Форматтеры дат
 */
public class FormatDateTime {

    public static DateTimeFormatter reformDate() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    public static DateTimeFormatter reformDateTime() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }
}
