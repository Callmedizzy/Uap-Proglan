package app.ui;

import app.model.Purchase;
import app.service.PurchaseService;
import app.util.DateUtil;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HistoryPanel extends JPanel {
    private final PurchaseService service;
    private final InfoCard totalAllCard;
    private final InfoCard totalRegularCard;
    private final InfoCard totalVipCard;
    private final InfoCard paidCard;
    private final JLabel lastUpdateLabel;
    private final HistoryTableModel tableModel;

    public HistoryPanel(PurchaseService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG);
        header.setBorder(new EmptyBorder(14, 18, 6, 18));

        JLabel title = new JLabel("History & Report");
        title.setFont(Theme.SUBTITLE_FONT);
        title.setForeground(Theme.TEXT_DARK);
        header.add(title, BorderLayout.WEST);

        lastUpdateLabel = new JLabel("Update terakhir: -");
        lastUpdateLabel.setFont(Theme.BODY_FONT);
        lastUpdateLabel.setForeground(Theme.TEXT_DARK);
        header.add(lastUpdateLabel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        JPanel summary = new JPanel(new GridLayout(1, 4, 10, 10));
        summary.setBackground(Theme.BG);
        summary.setBorder(new EmptyBorder(4, 18, 10, 18));

        totalAllCard = new InfoCard("Total Semua", Theme.ACCENT);
        totalRegularCard = new InfoCard("Total Regular", Theme.ACCENT_GREEN);
        totalVipCard = new InfoCard("Total VIP", Theme.ACCENT);
        paidCard = new InfoCard("Paid Count", Theme.ACCENT_GREEN);

        summary.add(totalAllCard);
        summary.add(totalRegularCard);
        summary.add(totalVipCard);
        summary.add(paidCard);

        tableModel = new HistoryTableModel();
        JTable table = new JTable(tableModel);
        table.setRowHeight(20);
        table.setFont(Theme.BODY_FONT);
        table.getTableHeader().setFont(Theme.BODY_BOLD);
        table.getTableHeader().setBackground(Theme.CARD);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 18, 12, 18));

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Theme.BG);
        content.add(summary, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);

        add(content, BorderLayout.CENTER);

        refresh();
    }

    public void refresh() {
        List<Purchase> data = service.getHistory();
        long totalAll = 0L;
        long totalRegular = 0L;
        long totalVip = 0L;
        int paidCount = 0;
        LocalDateTime lastUpdate = null;

        for (Purchase purchase : data) {
            totalAll += purchase.getTotalPaid();
            String type = purchase.getTicketType();
            if (type != null && type.equalsIgnoreCase("REGULAR")) {
                totalRegular += purchase.getTotalPaid();
            } else if (type != null && type.equalsIgnoreCase("VIP")) {
                totalVip += purchase.getTotalPaid();
            }
            if (purchase.getStatus() != null && purchase.getStatus().equalsIgnoreCase("PAID")) {
                paidCount++;
            }
            LocalDateTime updated = purchase.getUpdatedAt();
            if (updated != null && (lastUpdate == null || updated.isAfter(lastUpdate))) {
                lastUpdate = updated;
            }
        }

        totalAllCard.setValue(formatCurrency(totalAll));
        totalRegularCard.setValue(formatCurrency(totalRegular));
        totalVipCard.setValue(formatCurrency(totalVip));
        paidCard.setValue(String.valueOf(paidCount));
        if (data.isEmpty()) {
            lastUpdateLabel.setText("Belum ada riwayat.");
        } else {
            lastUpdateLabel.setText("Update terakhir: " + DateUtil.formatDateTime(lastUpdate));
        }

        List<Purchase> recent = new ArrayList<>(data);
        recent.sort(Comparator.comparing(Purchase::getUpdatedAt, Comparator.nullsLast(Comparator.naturalOrder()))
                .reversed());
        if (recent.size() > 10) {
            recent = recent.subList(0, 10);
        }
        tableModel.setData(recent);
    }

    private String formatCurrency(long value) {
        String formatted = String.format("%,d", value).replace(',', '.');
        return "Rp " + formatted;
    }

    private static class HistoryTableModel extends AbstractTableModel {
        private final String[] columns = new String[] {
                "Ref",
                "Customer",
                "Ticket Type",
                "Status",
                "Total",
                "Updated At"
        };
        private List<Purchase> data = new ArrayList<>();

        public void setData(List<Purchase> data) {
            this.data = new ArrayList<>(data);
            fireTableDataChanged();
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Purchase purchase = data.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return purchase.getRef();
                case 1:
                    return purchase.getCustomerName();
                case 2:
                    return purchase.getTicketType();
                case 3:
                    return purchase.getStatus();
                case 4:
                    return purchase.getTotalPaid();
                case 5:
                    return DateUtil.formatDateTime(purchase.getUpdatedAt());
                default:
                    return "";
            }
        }
    }
}

