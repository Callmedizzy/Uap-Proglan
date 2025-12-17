package app.ui;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class GradientPanel extends JPanel {
    private final Color start;
    private final Color end;

    public GradientPanel(Color start, Color end) {
        this.start = start;
        this.end = end;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(0, 0, start, getWidth(), getHeight(), end));
        g2.fillRect(0, 0, getWidth(), getHeight());
        super.paintComponent(g);
    }
}

