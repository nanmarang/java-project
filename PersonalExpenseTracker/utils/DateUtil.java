package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static String getCurrentDate() {
        return LocalDate.now().format(FORMATTER);
    }

    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, FORMATTER);
            return true;
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
