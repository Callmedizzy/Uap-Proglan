package app.ui;

import app.model.Purchase;
import app.service.PurchaseService;
import app.util.Validation;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;

public class MemberPanel extends JPanel {
    private static final long PRICE_REGULAR = 25000L;
    private static final long PRICE_VIP = 50000L;
    private static final String[] EVENTS = new String[] {
            "Pengajian Akbar",
            "Kajian Subuh",
            "Majelis Dzikir",
            "Pengajian Jumat",
            "Tafsir Ramadhan"
    };

    private final PurchaseService service;
    private final Runnable onSaved;
    private final Runnable onLogout;
    private final JLabel memberLabel;
    private final JComboBox<String> eventBox;
    private final JComboBox<String> ticketTypeBox;
    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JComboBox<String> paymentMethodBox;
    private final JComboBox<String> bankBox;
    private final JSpinner quantitySpinner;
    private final JLabel totalLabel;

    public MemberPanel(PurchaseService service, Runnable onSaved, Runnable onLogout) {
        this.service = service;
        this.onSaved = onSaved;
        this.onLogout = onLogout;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        GradientPanel header = new GradientPanel(new Color(232, 201, 173), new Color(246, 232, 216));
        header.setLayout(new BorderLayout());
        header.setBorder(new EmptyBorder(16, 18, 16, 18));

        JLabel title = new JLabel("Pemesanan Tiket Pengajian");
        title.setFont(Theme.TITLE_FONT);
        title.setForeground(Theme.TEXT_DARK);

        JLabel subtitle = new JLabel("Isi data singkat untuk memesan tiket");
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setForeground(Theme.TEXT_DARK);

        JPanel titleWrap = new JPanel(new BorderLayout());
        titleWrap.setOpaque(false);
        titleWrap.add(title, BorderLayout.NORTH);
        titleWrap.add(subtitle, BorderLayout.CENTER);

        memberLabel = new JLabel("Login sebagai: -");
        memberLabel.setFont(Theme.BODY_FONT);
        memberLabel.setForeground(Theme.TEXT_DARK);

        header.add(titleWrap, BorderLayout.WEST);
        header.add(memberLabel, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.BG);
        form.setBorder(new EmptyBorder(10, 18, 10, 18));

        eventBox = new JComboBox<>(EVENTS);
        ticketTypeBox = new JComboBox<>(new String[] { "REGULAR", "VIP" });
        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();
        paymentMethodBox = new JComboBox<>(new String[] { "Transfer Bank", "E-Wallet", "Tunai" });
        bankBox = new JComboBox<>(new String[] { "BSI", "BCA", "BRI", "Mandiri" });
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        totalLabel = new JLabel();

        eventBox.setFont(Theme.BODY_FONT);
        ticketTypeBox.setFont(Theme.BODY_FONT);
        nameField.setFont(Theme.BODY_FONT);
        emailField.setFont(Theme.BODY_FONT);
        phoneField.setFont(Theme.BODY_FONT);
        paymentMethodBox.setFont(Theme.BODY_FONT);
        bankBox.setFont(Theme.BODY_FONT);
        quantitySpinner.setFont(Theme.BODY_FONT);
        totalLabel.setFont(Theme.BODY_BOLD);
        totalLabel.setForeground(Theme.TEXT_DARK);

        int row = 0;
        addRow(form, row++, "Nama", nameField, "Email", emailField);
        addRow(form, row++, "Telepon", phoneField, "Jumlah", quantitySpinner);
        addRow(form, row++, "Pengajian", eventBox, "Ticket Type", ticketTypeBox);
        addRow(form, row++, "Metode Bayar", paymentMethodBox, "Bank", bankBox);
        addRow(form, row++, "Total Bayar", totalLabel, "", new JLabel(""));

        add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        actions.setBackground(Theme.BG);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 18, 10, 18));

        JButton submitBtn = new JButton("Pesan");
        Theme.stylePrimaryButton(submitBtn);
        submitBtn.addActionListener(e -> submit());

        JButton resetBtn = new JButton("Reset");
        Theme.styleSecondaryButton(resetBtn);
        resetBtn.addActionListener(e -> resetForm());

        JButton logoutBtn = new JButton("Logout");
        Theme.styleSecondaryButton(logoutBtn);
        logoutBtn.addActionListener(e -> onLogout.run());

        actions.add(submitBtn);
        actions.add(resetBtn);
        actions.add(logoutBtn);
        add(actions, BorderLayout.SOUTH);

        ticketTypeBox.addActionListener(e -> updateTotal());
        quantitySpinner.addChangeListener(e -> updateTotal());
        paymentMethodBox.addActionListener(e -> updateBankState());

        resetForm();
    }

    public void prepareFor(String memberName) {
        resetForm();
        if (memberName == null || memberName.trim().isEmpty()) {
            memberLabel.setText("Login sebagai: -");
            return;
        }
        String name = memberName.trim();
        memberLabel.setText("Login sebagai: " + name);
        nameField.setText(name);
    }

    private void resetForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        eventBox.setSelectedIndex(0);
        ticketTypeBox.setSelectedIndex(0);
        paymentMethodBox.setSelectedIndex(0);
        bankBox.setSelectedIndex(0);
        quantitySpinner.setValue(1);
        updateBankState();
        updateTotal();
    }

    private void submit() {
        try {
            int quantity = readQuantity();
            String eventName = (String) eventBox.getSelectedItem();
            String ticketType = (String) ticketTypeBox.getSelectedItem();
            String customerName = Validation.requireNotBlank("Nama", nameField.getText());
            String email = Validation.validateEmail("Email", emailField.getText());
            String phone = Validation.validatePhone("Telepon", phoneField.getText());
            String paymentMethod = (String) paymentMethodBox.getSelectedItem();
            String paymentType = resolvePaymentType(paymentMethod);
            String bank = bankBox.isEnabled() ? (String) bankBox.getSelectedItem() : "";
            long totalPaid = calculateTotal(ticketType, quantity);

            Purchase purchase = new Purchase();
            purchase.setEventName(eventName);
            purchase.setTicketType(ticketType);
            purchase.setCustomerName(customerName);
            purchase.setCustomerEmail(email);
            purchase.setCustomerPhone(phone);
            purchase.setPaymentMethod(paymentMethod);
            purchase.setStatus("UNPAID");
            purchase.setCheckInStatus("Belum");
            purchase.setTotalPaid(totalPaid);
            purchase.setPaymentType(paymentType);
            purchase.setBank(bank);
            purchase.setQuantity(quantity);

            LocalDateTime now = LocalDateTime.now();
            purchase.setCreatedAt(now);
            purchase.setUpdatedAt(now);
            purchase.setExportedAt(now);

            service.add(purchase);
            onSaved.run();

            JOptionPane.showMessageDialog(this,
                    "Pesanan berhasil dibuat.\nRef: " + purchase.getRef(),
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE);
            resetForm();
        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "Jumlah tidak valid.",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private int readQuantity() throws ParseException {
        quantitySpinner.commitEdit();
        Object value = quantitySpinner.getValue();
        if (value instanceof Number) {
            int quantity = ((Number) value).intValue();
            if (quantity <= 0) {
                throw new IllegalArgumentException("Jumlah minimal 1.");
            }
            return quantity;
        }
        throw new IllegalArgumentException("Jumlah tidak valid.");
    }

    private void updateTotal() {
        int quantity;
        try {
            quantity = readQuantity();
        } catch (Exception ex) {
            quantity = 1;
        }
        String ticketType = (String) ticketTypeBox.getSelectedItem();
        long total = calculateTotal(ticketType, quantity);
        totalLabel.setText(formatCurrency(total));
    }

    private long calculateTotal(String ticketType, int quantity) {
        long price = priceFor(ticketType);
        return price * Math.max(1, quantity);
    }

    private long priceFor(String ticketType) {
        if (ticketType != null && ticketType.equalsIgnoreCase("VIP")) {
            return PRICE_VIP;
        }
        return PRICE_REGULAR;
    }

    private String resolvePaymentType(String paymentMethod) {
        if (paymentMethod == null) {
            return "";
        }
        if (paymentMethod.equalsIgnoreCase("Transfer Bank")) {
            return "Transfer";
        }
        if (paymentMethod.equalsIgnoreCase("E-Wallet")) {
            return "E-Wallet";
        }
        return "Tunai";
    }

    private void updateBankState() {
        String method = (String) paymentMethodBox.getSelectedItem();
        boolean needsBank = "Transfer Bank".equals(method);
        bankBox.setEnabled(needsBank);
        if (!needsBank) {
            bankBox.setSelectedIndex(0);
        }
    }

    private String formatCurrency(long value) {
        String formatted = String.format("%,d", value).replace(',', '.');
        return "Rp " + formatted;
    }

    private void addRow(JPanel panel, int row, String leftLabel, java.awt.Component leftField,
                        String rightLabel, java.awt.Component rightField) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 2, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(createLabel(leftLabel), gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        panel.add(leftField, gbc);

        gbc.gridx = 2;
        gbc.gridy = row;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        panel.add(createLabel(rightLabel), gbc);

        gbc.gridx = 3;
        gbc.gridy = row;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0.5;
        panel.add(rightField, gbc);
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.BODY_BOLD);
        label.setForeground(Theme.TEXT_DARK);
        return label;
    }
}
