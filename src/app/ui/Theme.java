package app.ui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.Color;
import java.awt.Font;

public final class Theme {
    public static final Color BG = new Color(246, 232, 216);
    public static final Color CARD = new Color(251, 247, 241);
    public static final Color SIDEBAR = new Color(59, 51, 44);
    public static final Color SIDEBAR_DARK = new Color(47, 41, 36);
    public static final Color ACCENT = new Color(200, 90, 58);
    public static final Color ACCENT_GREEN = new Color(70, 122, 99);
    public static final Color TEXT_DARK = new Color(59, 51, 44);
    public static final Color TEXT_LIGHT = new Color(248, 243, 236);
    public static final Color BUTTON_LIGHT = new Color(239, 220, 201);

    public static final Font TITLE_FONT = new Font("Georgia", Font.BOLD, 24);
    public static final Font SUBTITLE_FONT = new Font("Georgia", Font.BOLD, 16);
    public static final Font BODY_FONT = new Font("Trebuchet MS", Font.PLAIN, 13);
    public static final Font BODY_BOLD = new Font("Trebuchet MS", Font.BOLD, 13);

    private Theme() {
    }

    public static void styleNavButton(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setFont(BODY_BOLD);
        button.setForeground(TEXT_LIGHT);
        button.setBackground(SIDEBAR_DARK);
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setRolloverEnabled(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(10);
    }

    public static void stylePrimaryButton(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setFont(BODY_BOLD);
        button.setForeground(TEXT_LIGHT);
        button.setBackground(ACCENT);
        button.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.setRolloverEnabled(false);
    }

    public static void styleSecondaryButton(JButton button) {
        button.setUI(new BasicButtonUI());
        button.setFont(BODY_BOLD);
        button.setForeground(TEXT_DARK);
        button.setBackground(BUTTON_LIGHT);
        button.setBorder(BorderFactory.createLineBorder(ACCENT, 1));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setRolloverEnabled(false);
    }
}
