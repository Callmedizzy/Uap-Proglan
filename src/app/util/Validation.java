package app.util;

import java.util.regex.Pattern;

public class Validation {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern PHONE = Pattern.compile("^[0-9+][0-9\\s-]{6,}$");

    private Validation() {
    }

    public static String requireNotBlank(String label, String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(label + " tidak boleh kosong.");
        }
        return value.trim();
    }

    public static long parseLong(String label, String value) {
        try {
            return Long.parseLong(requireNotBlank(label, value));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " harus berupa angka.");
        }
    }

    public static int parseInt(String label, String value) {
        try {
            return Integer.parseInt(requireNotBlank(label, value));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " harus berupa angka.");
        }
    }

    public static String validateEmail(String label, String value) {
        String trimmed = requireNotBlank(label, value);
        if (!EMAIL.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(label + " tidak valid.");
        }
        return trimmed;
    }

    public static String validatePhone(String label, String value) {
        String trimmed = requireNotBlank(label, value);
        if (!PHONE.matcher(trimmed).matches()) {
            throw new IllegalArgumentException(label + " tidak valid.");
        }
        return trimmed;
    }
}

