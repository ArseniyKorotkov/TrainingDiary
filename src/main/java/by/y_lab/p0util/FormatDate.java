package by.y_lab.p0util;

import java.time.format.DateTimeFormatter;

public class FormatDate {
    public static DateTimeFormatter reform() {
        return DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }
}
