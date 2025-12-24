package app.ui;

import app.util.Validation;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.function.BiConsumer;

public class LoginPanel extends JPanel {
    public enum Role {
        ADMIN("Admin"),
        MEMBER("Anggota");

        private final String label;

        Role(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private static final String ADMIN_USER = "admin";
    private static final String ADMIN_PASS = "admin123";
    private static final String MEMBER_PASS = "anggota123";
    private static final int INPUT_WIDTH = 340;
    private static final int INPUT_HEIGHT = 34;

    private final BiConsumer<Role, String> onLogin;
    private final JTextField nameField;
    private final JPasswordField passwordField;

    public LoginPanel(BiConsumer<Role, String> onLogin) {
        this.onLogin = onLogin;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        JPanel center = new JPanel(new GridBagLayout());
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(40, 24, 40, 24));

        JPanel card = new JPanel();
        card.setBackground(Theme.CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT, 1, true),
                new EmptyBorder(26, 32, 26, 32)));
        card.setLayout(new GridBagLayout());
        card.setPreferredSize(new Dimension(420, 320));

        JLabel title = new JLabel("Selamat Datang");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Silakan login dengan akun kamu");
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setForeground(Theme.TEXT_DARK);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.gridx = 0;
        cardGbc.anchor = GridBagConstraints.CENTER;
        cardGbc.insets = new Insets(0, 0, 6, 0);
        cardGbc.gridy = 0;
        card.add(title, cardGbc);

        cardGbc.gridy = 1;
        cardGbc.insets = new Insets(0, 0, 18, 0);
        card.add(subtitle, cardGbc);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);
        form.setMaximumSize(new Dimension(INPUT_WIDTH, 160));
        form.setPreferredSize(new Dimension(INPUT_WIDTH, 160));

        nameField = new JTextField(24);
        passwordField = new JPasswordField(24);
        styleField(nameField);
        styleField(passwordField);

        addFormRow(form, 0, buildField("Nama", nameField));
        addFormRow(form, 1, buildField("Password", passwordField));

        cardGbc.gridy = 2;
        cardGbc.insets = new Insets(0, 0, 18, 0);
        card.add(form, cardGbc);

        JButton loginBtn = new JButton("Login Sekarang");
        Theme.stylePrimaryButton(loginBtn);
        loginBtn.setMaximumSize(new Dimension(INPUT_WIDTH, 38));
        loginBtn.setPreferredSize(new Dimension(INPUT_WIDTH, 38));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> attemptLogin());
        passwordField.addActionListener(e -> attemptLogin());

        cardGbc.gridy = 3;
        cardGbc.insets = new Insets(0, 0, 0, 0);
        card.add(loginBtn, cardGbc);

        center.add(card, new GridBagConstraints());
        add(center, BorderLayout.CENTER);
    }

    public void reset() {
        nameField.setText("");
        passwordField.setText("");
    }

    private void attemptLogin() {
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama dan password wajib diisi.",
                    "Login Gagal",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (isAdminLogin(name, password)) {
            onLogin.accept(Role.ADMIN, name);
            return;
        }

        Validation.requireNotBlank("Nama", name);

        if (!MEMBER_PASS.equals(password)) {
            JOptionPane.showMessageDialog(this,
                    "Password anggota tidak sesuai.",
                    "Login Gagal",
                    JOptionPane.WARNING_MESSAGE);
            passwordField.setText("");
            return;
        }

        onLogin.accept(Role.MEMBER, name);
    }

    private boolean isAdminLogin(String name, String password) {
        if (!ADMIN_PASS.equals(password)) {
            return false;
        }
        return ADMIN_USER.equalsIgnoreCase(name);
    }

    private JPanel buildField(String labelText, JTextField field) {
        JPanel wrap = new JPanel();
        wrap.setOpaque(false);
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setMaximumSize(new Dimension(INPUT_WIDTH, 70));
        wrap.setPreferredSize(new Dimension(INPUT_WIDTH, 70));
        wrap.setMinimumSize(new Dimension(INPUT_WIDTH, 70));

        JLabel label = new JLabel(labelText);
        label.setFont(Theme.BODY_BOLD);
        label.setForeground(Theme.TEXT_DARK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        Dimension labelSize = label.getPreferredSize();
        label.setPreferredSize(new Dimension(INPUT_WIDTH, labelSize.height));
        label.setMaximumSize(new Dimension(INPUT_WIDTH, labelSize.height));

        field.setAlignmentX(Component.CENTER_ALIGNMENT);

        wrap.add(label);
        wrap.add(Box.createVerticalStrut(6));
        wrap.add(field);
        return wrap;
    }

    private void addFormRow(JPanel panel, int row, JPanel fieldPanel) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(6, 0, 6, 0);
        panel.add(fieldPanel, gbc);
    }

    private void styleField(JTextField field) {
        field.setFont(Theme.BODY_FONT);
        field.setBackground(Theme.CARD);
        field.setForeground(Theme.TEXT_DARK);
        field.setCaretColor(Theme.TEXT_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.ACCENT, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        field.setMaximumSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));
        field.setPreferredSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));
        field.setMinimumSize(new Dimension(INPUT_WIDTH, INPUT_HEIGHT));
    }
}
