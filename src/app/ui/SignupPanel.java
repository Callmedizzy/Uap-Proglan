package app.ui;

import app.service.MemberAccountService;

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
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.util.function.Consumer;

public class SignupPanel extends JPanel {
    private static final int INPUT_WIDTH = 340;
    private static final int INPUT_HEIGHT = 34;

    private final MemberAccountService accountService;
    private final Consumer<String> onSuccess;
    private final Runnable onBack;
    private final JTextField nameField;
    private final JPasswordField passwordField;
    private final JPasswordField confirmField;

    public SignupPanel(MemberAccountService accountService, Consumer<String> onSuccess, Runnable onBack) {
        this.accountService = accountService;
        this.onSuccess = onSuccess;
        this.onBack = onBack;
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
        card.setPreferredSize(new Dimension(420, 370));

        JLabel title = new JLabel("Daftar Akun Anggota");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Isi data untuk membuat akun baru");
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
        form.setMaximumSize(new Dimension(INPUT_WIDTH, 200));
        form.setPreferredSize(new Dimension(INPUT_WIDTH, 200));

        nameField = new JTextField(24);
        passwordField = new JPasswordField(24);
        confirmField = new JPasswordField(24);
        styleField(nameField);
        styleField(passwordField);
        styleField(confirmField);

        addFormRow(form, 0, buildField("Nama", nameField));
        addFormRow(form, 1, buildField("Password", passwordField));
        addFormRow(form, 2, buildField("Konfirmasi", confirmField));

        cardGbc.gridy = 2;
        cardGbc.insets = new Insets(0, 0, 18, 0);
        card.add(form, cardGbc);

        JButton signupBtn = new JButton("Daftar");
        Theme.stylePrimaryButton(signupBtn);
        signupBtn.setMaximumSize(new Dimension(INPUT_WIDTH, 38));
        signupBtn.setPreferredSize(new Dimension(INPUT_WIDTH, 38));
        signupBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        signupBtn.addActionListener(e -> attemptSignup());
        confirmField.addActionListener(e -> attemptSignup());

        cardGbc.gridy = 3;
        cardGbc.insets = new Insets(0, 0, 0, 0);
        card.add(signupBtn, cardGbc);

        JPanel backRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        backRow.setOpaque(false);

        JLabel backLabel = new JLabel("Sudah punya akun?");
        backLabel.setFont(Theme.BODY_FONT.deriveFont(11f));
        backLabel.setForeground(Theme.TEXT_DARK);

        JButton backBtn = new JButton("Login");
        backBtn.setFont(Theme.BODY_FONT.deriveFont(11f));
        backBtn.setForeground(Theme.ACCENT);
        backBtn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusPainted(false);
        backBtn.setOpaque(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> onBack.run());

        backRow.add(backLabel);
        backRow.add(backBtn);

        cardGbc.gridy = 4;
        cardGbc.insets = new Insets(10, 0, 0, 0);
        card.add(backRow, cardGbc);

        center.add(card, new GridBagConstraints());
        add(center, BorderLayout.CENTER);
    }

    public void reset() {
        nameField.setText("");
        passwordField.setText("");
        confirmField.setText("");
    }

    private void attemptSignup() {
        String name = nameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmField.getPassword()).trim();

        if (name.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Nama dan password wajib diisi.",
                    "Daftar Gagal",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this,
                    "Konfirmasi password tidak sama.",
                    "Daftar Gagal",
                    JOptionPane.WARNING_MESSAGE);
            confirmField.setText("");
            return;
        }

        try {
            accountService.register(name, password);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Daftar Gagal",
                    JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan akun: " + ex.getMessage(),
                    "Daftar Gagal",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Akun berhasil dibuat. Silakan login.",
                "Berhasil",
                JOptionPane.INFORMATION_MESSAGE);
        reset();
        onSuccess.accept(name);
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
