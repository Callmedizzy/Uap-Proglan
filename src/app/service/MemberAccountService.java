package app.service;

import app.util.Csvu;
import app.util.Validation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MemberAccountService {
    private static final String RESERVED_ADMIN = "admin";

    private final Path path;
    private final Map<String, Account> accounts = new LinkedHashMap<>();

    public MemberAccountService(Path path) {
        this.path = path;
    }

    public void load() throws IOException {
        accounts.clear();
        List<String[]> rows = Csvu.read(path);
        for (String[] row : rows) {
            if (row.length < 2) {
                continue;
            }
            String name = row[0];
            String password = row[1];
            if (name == null || name.trim().isEmpty()) {
                continue;
            }
            String key = normalize(name);
            if (!accounts.containsKey(key)) {
                accounts.put(key, new Account(name.trim(), password == null ? "" : password));
            }
        }
    }

    public void register(String name, String password) throws IOException {
        String cleanName = Validation.requireNotBlank("Nama", name);
        String cleanPassword = Validation.requireNotBlank("Password", password);
        if (isAdminName(cleanName)) {
            throw new IllegalArgumentException("Nama \"admin\" hanya untuk admin.");
        }
        String key = normalize(cleanName);
        if (accounts.containsKey(key)) {
            throw new IllegalArgumentException("Nama sudah terdaftar.");
        }
        accounts.put(key, new Account(cleanName, cleanPassword));
        save();
    }

    public String authenticate(String name, String password) {
        if (name == null || password == null) {
            return null;
        }
        String key = normalize(name);
        Account account = accounts.get(key);
        if (account == null) {
            return null;
        }
        if (!account.password.equals(password)) {
            return null;
        }
        return account.name;
    }

    public boolean isEmpty() {
        return accounts.isEmpty();
    }

    public boolean isAdminName(String name) {
        return name != null && name.trim().equalsIgnoreCase(RESERVED_ADMIN);
    }

    private void save() throws IOException {
        List<String[]> rows = new ArrayList<>();
        for (Account account : accounts.values()) {
            rows.add(new String[] { account.name, account.password });
        }
        Csvu.write(path, rows);
    }

    private String normalize(String name) {
        return name.trim().toLowerCase();
    }

    private static final class Account {
        private final String name;
        private final String password;

        private Account(String name, String password) {
            this.name = name;
            this.password = password;
        }
    }
}
