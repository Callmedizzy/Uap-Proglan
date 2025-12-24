package app.ui;

import app.model.Purchase;
import app.repository.PurchaseRepository;
import app.service.PurchaseService;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class ListPanel extends JPanel {
    private final PurchaseService service;
    private final PurchaseTableModel tableModel;
    private final JTable table;
    private final TableRowSorter<PurchaseTableModel> sorter;
    private final JTextField searchField;
    private final JComboBox<String> sortCombo;
    private final JLabel countLabel;
    private final Consumer<Purchase> openForm;
    private final Runnable dataChanged;

    public ListPanel(PurchaseService service, Consumer<Purchase> openForm, Runnable dataChanged) {
        this.service = service;
        this.openForm = openForm;
        this.dataChanged = dataChanged;
        setLayout(new BorderLayout());
        setBackground(Theme.BG);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.BG);
        header.setBorder(BorderFactory.createEmptyBorder(14, 18, 6, 18));

        JLabel title = new JLabel("List Data Purchase");
        title.setFont(Theme.SUBTITLE_FONT);
        title.setForeground(Theme.TEXT_DARK);
        header.add(title, BorderLayout.WEST);

        countLabel = new JLabel("0 data");
        countLabel.setFont(Theme.BODY_FONT);
        countLabel.setForeground(Theme.TEXT_DARK);
        header.add(countLabel, BorderLayout.EAST);

        JPanel controls = new JPanel(new GridLayout(2, 1, 0, 6));
        controls.setBackground(Theme.BG);
        controls.setBorder(BorderFactory.createEmptyBorder(0, 18, 8, 18));

        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        searchRow.setBackground(Theme.BG);
        searchField = new JTextField();
        searchField.setFont(Theme.BODY_FONT);
        searchField.setToolTipText("Cari berdasarkan nama, email, atau ref");
        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(Theme.BODY_BOLD);
        searchLabel.setForeground(Theme.TEXT_DARK);
        searchRow.add(searchLabel, BorderLayout.WEST);
        searchRow.add(searchField, BorderLayout.CENTER);

        JPanel sortRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        sortRow.setBackground(Theme.BG);
        sortCombo = new JComboBox<>(new String[] {
                "Sesuai CSV",
                "Tanggal Terbaru",
                "Total (tertinggi)",
                "Customer A-Z",
                "Ticket Type"
        });
        sortCombo.setFont(Theme.BODY_FONT);
        JLabel sortLabel = new JLabel("Sort:");
        sortLabel.setFont(Theme.BODY_BOLD);
        sortLabel.setForeground(Theme.TEXT_DARK);
        sortRow.add(sortLabel);
        sortRow.add(sortCombo);

        controls.add(searchRow);
        controls.add(sortRow);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Theme.BG);
        top.add(header, BorderLayout.NORTH);
        top.add(controls, BorderLayout.SOUTH);
        add(top, BorderLayout.NORTH);

        tableModel = new PurchaseTableModel();
        table = new JTable(tableModel);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table.setRowHeight(20);
        table.setFont(Theme.BODY_FONT);
        table.getTableHeader().setFont(Theme.BODY_BOLD);
        table.getTableHeader().setBackground(Theme.CARD);

        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 18, 0, 18));
        add(scrollPane, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 10));
        actions.setBackground(Theme.BG);
        actions.setBorder(BorderFactory.createEmptyBorder(0, 18, 8, 18));

        JButton addBtn = new JButton("Tambah");
        Theme.stylePrimaryButton(addBtn);
        addBtn.addActionListener(e -> openForm.accept(null));

        JButton editBtn = new JButton("Edit");
        Theme.styleSecondaryButton(editBtn);
        editBtn.addActionListener(e -> editSelected());

        JButton deleteBtn = new JButton("Hapus");
        Theme.styleSecondaryButton(deleteBtn);
        deleteBtn.addActionListener(e -> deleteSelected());

        JButton refreshBtn = new JButton("Reload");
        Theme.styleSecondaryButton(refreshBtn);
        refreshBtn.addActionListener(e -> reloadData());

        JButton exportBtn = new JButton("Download CSV");
        Theme.styleSecondaryButton(exportBtn);
        exportBtn.addActionListener(e -> exportData());

        actions.add(addBtn);
        actions.add(editBtn);
        actions.add(deleteBtn);
        actions.add(exportBtn);
        actions.add(refreshBtn);

        add(actions, BorderLayout.SOUTH);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyFilter();
            }
        });

        sortCombo.addActionListener(e -> applySort());

        refresh();
    }

    public void refresh() {
        applySort();
    }

    private void reloadData() {
        try {
            service.load();
            applySort();
            dataChanged.run();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal reload data: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applySort() {
        String selected = (String) sortCombo.getSelectedItem();
        List<Purchase> data;
        if ("Tanggal Terbaru".equals(selected)) {
            data = service.getSorted(PurchaseService.sortByCreatedAt().reversed());
        } else if ("Total (tertinggi)".equals(selected)) {
            data = service.getSorted(PurchaseService.sortByTotalPaidDesc());
        } else if ("Customer A-Z".equals(selected)) {
            data = service.getSorted(PurchaseService.sortByCustomerName());
        } else if ("Ticket Type".equals(selected)) {
            data = service.getSorted(PurchaseService.sortByTicketType());
        } else {
            data = service.getAll();
        }
        tableModel.setData(data);
        applyFilter();
    }

    private void applyFilter() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + Pattern.quote(keyword)));
        }
        updateCountLabel();
    }

    private void updateCountLabel() {
        countLabel.setText(table.getRowCount() + " data");
    }

    private void editSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        Purchase purchase = tableModel.getAt(modelRow);
        if (purchase != null) {
            openForm.accept(purchase);
        }
    }

    private void deleteSelected() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(viewRow);
        Purchase purchase = tableModel.getAt(modelRow);
        if (purchase == null) {
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Hapus data dengan ref: " + purchase.getRef() + "?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            service.delete(purchase.getRef());
            applySort();
            dataChanged.run();
        } catch (IOException | IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal hapus data: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportData() {
        List<Purchase> data = collectVisibleData();
        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tidak ada data untuk di-download.");
            return;
        }
        try {
            Path path = Path.of("data", buildExportFileName());
            PurchaseRepository exporter = new PurchaseRepository(path);
            exporter.save(data);
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan:\n" + path.toAbsolutePath());
        } catch (IOException | RuntimeException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan data: " + ex.getClass().getSimpleName() + " - " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private List<Purchase> collectVisibleData() {
        List<Purchase> data = new ArrayList<>();
        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            int modelRow = table.convertRowIndexToModel(i);
            Purchase purchase = tableModel.getAt(modelRow);
            if (purchase != null) {
                data.add(purchase);
            }
        }
        return data;
    }

    private String buildExportFileName() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String stamp = LocalDateTime.now().format(formatter);
        return "eticket_data_" + stamp + ".csv";
    }
}

