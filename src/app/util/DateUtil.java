package app.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");

    private DateUtil() {
    }

    public static LocalDateTime parseDateTime(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return LocalDateTime.parse(trimmed, DATE_TIME);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    public static String formatDateTime(LocalDateTime value) {
        if (value == null) {
            return "";
        }
        return DATE_TIME.format(value);
    }
}

