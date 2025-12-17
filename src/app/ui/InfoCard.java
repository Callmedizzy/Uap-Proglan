package app.ui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;

public class InfoCard extends JPanel {
    private final JLabel valueLabel;

    public InfoCard(String title, Color accent) {
        setLayout(new BorderLayout());
        setBackground(Theme.CARD);
        setBorder(new EmptyBorder(12, 14, 12, 14));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(Theme.BODY_BOLD);
        titleLabel.setForeground(Theme.TEXT_DARK);

        valueLabel = new JLabel("0", SwingConstants.LEFT);
        valueLabel.setFont(Theme.SUBTITLE_FONT);
        valueLabel.setForeground(accent);

        add(titleLabel, BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }
}

