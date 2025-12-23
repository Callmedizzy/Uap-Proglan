package app.ui;

import app.model.Purchase;
import app.service.PurchaseService;
import app.util.DateUtil;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardPanel extends JPanel {
    private final PurchaseService service;
    private final InfoCard totalCard;
    private final InfoCard revenueCard;
    private final InfoCard regularCard;
    private final InfoCard vipCard;
    private final JLabel lastUpdateLabel;

    public DashboardPanel(PurchaseService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        GradientPanel header = new GradientPanel(new Color(232, 201, 173), new Color(246, 232, 216));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(14, 18, 14, 18));

        JLabel title = new JLabel("Dashboard");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.TEXT_DARK);

        JLabel subtitle = new JLabel("Ringkasan transaksi e-ticket terbaru");
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setForeground(Theme.TEXT_DARK);

        JPanel titleWrap = new JPanel(new BorderLayout());
        titleWrap.setOpaque(false);
        titleWrap.add(title, BorderLayout.NORTH);
        titleWrap.add(subtitle, BorderLayout.CENTER);

        header.add(titleWrap, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout());
        body.setBackground(Theme.BG);
        body.setBorder(new EmptyBorder(14, 18, 14, 18));

        JPanel cards = new JPanel(new GridLayout(2, 2, 12, 12));
        cards.setOpaque(false);

        totalCard = new InfoCard("Total Transaksi", Theme.ACCENT);
        revenueCard = new InfoCard("Total Pendapatan", Theme.ACCENT_GREEN);
        regularCard = new InfoCard("Total Regular", Theme.ACCENT);
        vipCard = new InfoCard("Total VIP", Theme.ACCENT_GREEN);

        cards.add(totalCard);
        cards.add(revenueCard);
        cards.add(regularCard);
        cards.add(vipCard);

        lastUpdateLabel = new JLabel("Update terakhir: -");
        lastUpdateLabel.setFont(Theme.BODY_FONT);
        lastUpdateLabel.setForeground(Theme.TEXT_DARK);

        body.add(cards, BorderLayout.CENTER);
        body.add(lastUpdateLabel, BorderLayout.SOUTH);

        add(body, BorderLayout.CENTER);
    }

    public void refresh() {
        List<Purchase> data = service.getAll();
        long totalRevenue = 0L;
        long totalRegular = 0L;
        long totalVip = 0L;
        LocalDateTime lastUpdate = null;

        for (Purchase purchase : data) {
            totalRevenue += purchase.getTotalPaid();
            String type = purchase.getTicketType();
            if (type != null) {
                if (type.equalsIgnoreCase("REGULAR")) {
                    totalRegular += purchase.getTotalPaid();
                } else if (type.equalsIgnoreCase("VIP")) {
                    totalVip += purchase.getTotalPaid();
                }
            }
            LocalDateTime updated = purchase.getUpdatedAt();
            if (updated != null && (lastUpdate == null || updated.isAfter(lastUpdate))) {
                lastUpdate = updated;
            }
        }

        totalCard.setValue(String.valueOf(data.size()));
        revenueCard.setValue(formatCurrency(totalRevenue));
        regularCard.setValue(formatCurrency(totalRegular));
        vipCard.setValue(formatCurrency(totalVip));
        lastUpdateLabel.setText("Update terakhir: " + DateUtil.formatDateTime(lastUpdate));
    }

    private String formatCurrency(long value) {
        String formatted = String.format("%,d", value).replace(',', '.');
        return "Rp " + formatted;
    }
}

