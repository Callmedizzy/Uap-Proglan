package app.ui;

import javax.swing.Icon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public final class IconFactory {
    private static final int SIZE = 16;

    private IconFactory() {
    }

    public static Icon letterIcon(String letter, Color background, Color foreground) {
        return new LetterIcon(letter, background, foreground);
    }

    private static class LetterIcon implements Icon {
        private final String letter;
        private final Color background;
        private final Color foreground;

        private LetterIcon(String letter, Color background, Color foreground) {
            if (letter == null || letter.trim().isEmpty()) {
                this.letter = "?";
            } else {
                this.letter = letter.trim().substring(0, 1).toUpperCase();
            }
            this.background = background;
            this.foreground = foreground;
        }

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(background);
            g2.fillRoundRect(x, y, SIZE, SIZE, 6, 6);

            g2.setColor(foreground);
            g2.setFont(new Font("Trebuchet MS", Font.BOLD, 10));
            FontMetrics metrics = g2.getFontMetrics();
            int textWidth = metrics.stringWidth(letter);
            int textHeight = metrics.getAscent();
            int textX = x + (SIZE - textWidth) / 2;
            int textY = y + (SIZE + textHeight) / 2 - 2;
            g2.drawString(letter, textX, textY);
            g2.dispose();
        }
    }
}
