package app.repository;

import app.model.Purchase;
import app.util.Csvu;
import app.util.DateUtil;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class PurchaseRepository {
    public static final String[] HEADERS = new String[] {
            "Ref/MerchantRef",
            "Event",
            "Ticket Type",
            "Customer Name",
            "Customer Email",
            "Customer Phone",
            "Payment Method",
            "Status",
            "Check-in",
            "Total (paid)",
            "Created At",
            "Updated At",
            "Payment Type",
            "Bank",
            "Quantity",
            "Exported At",
            "Total Reguler (summary)",
            "Total VIP (summary)",
            "Total Semua (summary)"
    };

    private final Path path;

    public PurchaseRepository(Path path) {
        this.path = path;
    }

    public List<Purchase> load() throws IOException {
        List<String[]> rows = Csvu.read(path);
        List<Purchase> purchases = new ArrayList<>();
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length == 0) {
                continue;
            }
            String first = get(row, 0);
            if (i == 0 && "Ref/MerchantRef".equalsIgnoreCase(first)) {
                continue;
            }
            if (first.isEmpty()) {
                continue;
            }
            Purchase purchase = new Purchase();
            purchase.setRef(first);
            purchase.setEventName(get(row, 1));
            purchase.setTicketType(get(row, 2));
            purchase.setCustomerName(get(row, 3));
            purchase.setCustomerEmail(get(row, 4));
            purchase.setCustomerPhone(get(row, 5));
            purchase.setPaymentMethod(get(row, 6));
            purchase.setStatus(get(row, 7));
            purchase.setCheckInStatus(get(row, 8));
            purchase.setTotalPaid(parseLong(get(row, 9)));
            purchase.setCreatedAt(DateUtil.parseDateTime(get(row, 10)));
            purchase.setUpdatedAt(DateUtil.parseDateTime(get(row, 11)));
            purchase.setPaymentType(get(row, 12));
            purchase.setBank(get(row, 13));
            purchase.setQuantity(parseInt(get(row, 14)));
            purchase.setExportedAt(DateUtil.parseDateTime(get(row, 15)));
            purchases.add(purchase);
        }
        return purchases;
    }

    public void save(List<Purchase> purchases) throws IOException {
        List<String[]> rows = new ArrayList<>();
        rows.add(HEADERS);
        for (Purchase purchase : purchases) {
            rows.add(purchase.toCsvRow());
        }
        Csvu.write(path, rows);
    }

    private static String get(String[] row, int index) {
        if (index >= 0 && index < row.length) {
            String value = row[index] == null ? "" : row[index].trim();
            if (index == 0 && value.startsWith("\uFEFF")) {
                return value.substring(1);
            }
            return value;
        }
        return "";
    }

    private static long parseLong(String value) {
        try {
            return Long.parseLong(value.trim());
        } catch (Exception ex) {
            return 0L;
        }
    }

    private static int parseInt(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception ex) {
            return 0;
        }
    }
}

