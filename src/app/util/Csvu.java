package app.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Csvu {
    private Csvu() {
    }

    public static List<String[]> read(Path path) throws IOException {
        List<String[]> rows = new ArrayList<>();
        if (!Files.exists(path)) {
            return rows;
        }
        List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        for (String line : lines) {
            rows.add(parseLine(line));
        }
        return rows;
    }

    public static void write(Path path, List<String[]> rows) throws IOException {
        List<String> lines = new ArrayList<>();
        for (String[] row : rows) {
            lines.add(toLine(row));
        }
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public static String[] parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (inQuotes) {
                if (ch == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        current.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    current.append(ch);
                }
            } else {
                if (ch == '"') {
                    inQuotes = true;
                } else if (ch == ',') {
                    fields.add(current.toString());
                    current.setLength(0);
                } else {
                    current.append(ch);
                }
            }
        }
        fields.add(current.toString());
        return fields.toArray(new String[0]);
    }

    public static String toLine(String[] row) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < row.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(escape(row[i]));
        }
        return builder.toString();
    }

    public static String escape(String value) {
        if (value == null) {
            return "";
        }
        boolean needsQuotes = value.contains(",") || value.contains("\"") || value.contains("\n")
                || value.contains("\r");
        if (!needsQuotes) {
            return value;
        }
        String escaped = value.replace("\"", "\"\"");
        return '"' + escaped + '"';
    }
}

