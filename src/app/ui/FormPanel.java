package app.ui;

import app.model.Purchase;
import app.service.PurchaseService;
import app.util.DateUtil;
import app.util.Validation;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.time.LocalDateTime;

public class FormPanel extends JPanel {
    private final PurchaseService service;
    private final Runnable onSaved;
    private final Runnable onCancel;

    private final JLabel titleLabel;
    private final JTextField refField;
    private final JTextField eventField;
    private final JComboBox<String> ticketTypeBox;
    private final JTextField customerNameField;
    private final JTextField customerEmailField;
    private final JTextField customerPhoneField;
    private final JTextField paymentMethodField;
    private final JComboBox<String> statusBox;
    private final JComboBox<String> checkInBox;
    private final JTextField totalPaidField;
    private final JTextField createdAtField;
    private final JTextField updatedAtField;
    private final JTextField paymentTypeField;
    private final JTextField bankField;
    private final JTextField quantityField;
    private final JTextField exportedAtField;

    private Purchase editing;

    public FormPanel(PurchaseService service, Runnable onSaved, Runnable onCancel) {
        this.service = service;
        this.onSaved = onSaved;
        this.onCancel = onCancel;

        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        titleLabel = new JLabel("Input Data");
        titleLabel.setFont(Theme.SUBTITLE_FONT);
        titleLabel.setForeground(Theme.TEXT_DARK);
        titleLabel.setBorder(new EmptyBorder(18, 22, 8, 22));
        add(titleLabel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.BG);
        form.setBorder(new EmptyBorder(8, 22, 8, 22));

        refField = new JTextField();
        eventField = new JTextField();
        ticketTypeBox = new JComboBox<>(new String[] { "REGULAR", "VIP" });
        customerNameField = new JTextField();
        customerEmailField = new JTextField();
        customerPhoneField = new JTextField();
        paymentMethodField = new JTextField();
        statusBox = new JComboBox<>(new String[] { "PAID", "UNPAID" });
        checkInBox = new JComboBox<>(new String[] { "Belum", "Sudah" });
        totalPaidField = new JTextField();
        createdAtField = new JTextField();
        updatedAtField = new JTextField();
        paymentTypeField = new JTextField();
        bankField = new JTextField();
        quantityField = new JTextField();
        exportedAtField = new JTextField();

        refField.setToolTipText("Kosongkan jika ingin auto-generate");
        createdAtField.setToolTipText("Format: yy-MM-dd HH:mm");
        updatedAtField.setToolTipText("Format: yy-MM-dd HH:mm");
        exportedAtField.setToolTipText("Format: yy-MM-dd HH:mm");
        updatedAtField.setEditable(false);

        styleField(refField);
        styleField(eventField);
        styleField(customerNameField);
        styleField(customerEmailField);
        styleField(customerPhoneField);
        styleField(paymentMethodField);
        styleField(totalPaidField);
        styleField(createdAtField);
        styleField(updatedAtField);
        styleField(paymentTypeField);
        styleField(bankField);
        styleField(quantityField);
        styleField(exportedAtField);
        ticketTypeBox.setFont(Theme.BODY_FONT);
        statusBox.setFont(Theme.BODY_FONT);
        checkInBox.setFont(Theme.BODY_FONT);

        int row = 0;
        addRow(form, row++, "Ref", refField, "Event", eventField);
        addRow(form, row++, "Ticket Type", ticketTypeBox, "Status", statusBox);
        addRow(form, row++, "Customer Name", customerNameField, "Customer Email", customerEmailField);
        addRow(form, row++, "Customer Phone", customerPhoneField, "Payment Method", paymentMethodField);
        addRow(form, row++, "Payment Type", paymentTypeField, "Bank", bankField);
        addRow(form, row++, "Check-in", checkInBox, "Quantity", quantityField);
        addRow(form, row++, "Total Paid", totalPaidField, "Created At", createdAtField);
        addRow(form, row++, "Updated At", updatedAtField, "Exported At", exportedAtField);

        add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 12));
        actions.setBackground(Theme.BG);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 22, 10, 22));

        JButton saveBtn = new JButton("Simpan");
        Theme.stylePrimaryButton(saveBtn);
        saveBtn.addActionListener(e -> save());

        JButton resetBtn = new JButton("Reset");
        Theme.styleSecondaryButton(resetBtn);
        resetBtn.addActionListener(e -> resetForm());

        JButton cancelBtn = new JButton("Kembali");
        Theme.styleSecondaryButton(cancelBtn);
        cancelBtn.addActionListener(e -> onCancel.run());

        actions.add(saveBtn);
        actions.add(resetBtn);
        actions.add(cancelBtn);

        add(actions, BorderLayout.SOUTH);

        setPurchase(null);
    }

    public void setPurchase(Purchase purchase) {
        editing = purchase;
        if (purchase == null) {
            titleLabel.setText("Tambah Data Purchase");
            refField.setText("");
            refField.setEnabled(true);
            eventField.setText("");
            ticketTypeBox.setSelectedIndex(0);
            customerNameField.setText("");
            customerEmailField.setText("");
            customerPhoneField.setText("");
            paymentMethodField.setText("");
            statusBox.setSelectedIndex(0);
            checkInBox.setSelectedIndex(0);
            totalPaidField.setText("");
            paymentTypeField.setText("");
            bankField.setText("");
            quantityField.setText("1");
            LocalDateTime now = LocalDateTime.now();
            createdAtField.setText(DateUtil.formatDateTime(now));
            updatedAtField.setText(DateUtil.formatDateTime(now));
            exportedAtField.setText(DateUtil.formatDateTime(now));
        } else {
            titleLabel.setText("Edit Data Purchase");
            refField.setText(purchase.getRef());
            refField.setEnabled(false);
            eventField.setText(purchase.getEventName());
            ticketTypeBox.setSelectedItem(purchase.getTicketType());
            customerNameField.setText(purchase.getCustomerName());
            customerEmailField.setText(purchase.getCustomerEmail());
            customerPhoneField.setText(purchase.getCustomerPhone());
            paymentMethodField.setText(purchase.getPaymentMethod());
            statusBox.setSelectedItem(purchase.getStatus());
            checkInBox.setSelectedItem(purchase.getCheckInStatus());
            totalPaidField.setText(String.valueOf(purchase.getTotalPaid()));
            createdAtField.setText(DateUtil.formatDateTime(purchase.getCreatedAt()));
            updatedAtField.setText(DateUtil.formatDateTime(purchase.getUpdatedAt()));
            paymentTypeField.setText(purchase.getPaymentType());
            bankField.setText(purchase.getBank());
            quantityField.setText(String.valueOf(purchase.getQuantity()));
            exportedAtField.setText(DateUtil.formatDateTime(purchase.getExportedAt()));
        }
    }

    private void resetForm() {
        setPurchase(editing);
    }

    private void save() {
        try {
            String ref = refField.getText().trim();
            String eventName = Validation.requireNotBlank("Event", eventField.getText());
            String ticketType = (String) ticketTypeBox.getSelectedItem();
            String customerName = Validation.requireNotBlank("Nama", customerNameField.getText());
            String email = Validation.validateEmail("Email", customerEmailField.getText());
            String phone = Validation.validatePhone("Telepon", customerPhoneField.getText());
            String paymentMethod = Validation.requireNotBlank("Payment Method", paymentMethodField.getText());
            String status = (String) statusBox.getSelectedItem();
            String checkIn = (String) checkInBox.getSelectedItem();
            long totalPaid = Validation.parseLong("Total Paid", totalPaidField.getText());
            String paymentType = paymentTypeField.getText().trim();
            String bank = bankField.getText().trim();
            int quantity = Validation.parseInt("Quantity", quantityField.getText());

            String createdText = createdAtField.getText().trim();
            LocalDateTime createdAt = DateUtil.parseDateTime(createdText);
            if (!createdText.isEmpty() && createdAt == null) {
                throw new IllegalArgumentException("Created At harus format yy-MM-dd HH:mm.");
            }
            if (createdAt == null) {
                createdAt = LocalDateTime.now();
            }

            String exportedText = exportedAtField.getText().trim();
            LocalDateTime exportedAt = DateUtil.parseDateTime(exportedText);
            if (!exportedText.isEmpty() && exportedAt == null) {
                throw new IllegalArgumentException("Exported At harus format yy-MM-dd HH:mm.");
            }

            Purchase purchase = new Purchase();
            purchase.setRef(ref);
            purchase.setEventName(eventName);
            purchase.setTicketType(ticketType);
            purchase.setCustomerName(customerName);
            purchase.setCustomerEmail(email);
            purchase.setCustomerPhone(phone);
            purchase.setPaymentMethod(paymentMethod);
            purchase.setStatus(status);
            purchase.setCheckInStatus(checkIn);
            purchase.setTotalPaid(totalPaid);
            purchase.setCreatedAt(createdAt);
            purchase.setPaymentType(paymentType);
            purchase.setBank(bank);
            purchase.setQuantity(quantity);
            purchase.setExportedAt(exportedAt);

            if (editing == null) {
                service.add(purchase);
            } else {
                service.update(purchase);
            }
            onSaved.run();
        } catch (IllegalArgumentException | IOException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void styleField(JTextField field) {
        field.setFont(Theme.BODY_FONT);
    }

    private void addRow(JPanel panel, int row, String leftLabel, java.awt.Component leftField,
                        String rightLabel, java.awt.Component rightField) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 2, 6, 12);
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
