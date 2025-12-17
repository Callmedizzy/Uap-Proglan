package app.ui;

import app.model.Purchase;
import app.util.DateUtil;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PurchaseTableModel extends AbstractTableModel {
    private final String[] columns = new String[] {
            "Ref",
            "Event",
            "Ticket Type",
            "Customer",
            "Email",
            "Phone",
            "Payment",
            "Status",
            "Check-in",
            "Total",
            "Created At",
            "Updated At",
            "Payment Type",
            "Bank",
            "Qty",
            "Exported At"
    };

    private List<Purchase> data = new ArrayList<>();

    public void setData(List<Purchase> data) {
        this.data = new ArrayList<>(data);
        fireTableDataChanged();
    }

    public Purchase getAt(int row) {
        if (row < 0 || row >= data.size()) {
            return null;
        }
        return data.get(row);
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
                return purchase.getEventName();
            case 2:
                return purchase.getTicketType();
            case 3:
                return purchase.getCustomerName();
            case 4:
                return purchase.getCustomerEmail();
            case 5:
                return purchase.getCustomerPhone();
            case 6:
                return purchase.getPaymentMethod();
            case 7:
                return purchase.getStatus();
            case 8:
                return purchase.getCheckInStatus();
            case 9:
                return purchase.getTotalPaid();
            case 10:
                return DateUtil.formatDateTime(purchase.getCreatedAt());
            case 11:
                return DateUtil.formatDateTime(purchase.getUpdatedAt());
            case 12:
                return purchase.getPaymentType();
            case 13:
                return purchase.getBank();
            case 14:
                return purchase.getQuantity();
            case 15:
                return DateUtil.formatDateTime(purchase.getExportedAt());
            default:
                return "";
        }
    }
}

