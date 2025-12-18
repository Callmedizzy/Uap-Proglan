package app.ui;

import app.model.Purchase;
import app.repository.PurchaseRepository;
import app.service.PurchaseService;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;
import java.nio.file.Paths;

public class AppFrame extends JFrame {
    private final PurchaseService service;
    private final DashboardPanel dashboardPanel;
    private final ListPanel listPanel;
    private final FormPanel formPanel;
    private final HistoryPanel historyPanel;
    private final CardLayout cardLayout;
    private final JPanel contentPanel;

    public AppFrame() {
        super("E-Ticket Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 720);
        setLocationRelativeTo(null);

        PurchaseRepository repository = new PurchaseRepository(Paths.get("data", "history.csv"));
        service = new PurchaseService(repository);
        try {
            service.load();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Gagal membaca data CSV: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        dashboardPanel = new DashboardPanel(service);
        listPanel = new ListPanel(service, this::openForm, this::refreshAll);
        formPanel = new FormPanel(service, this::onFormSaved, this::showList);
        historyPanel = new HistoryPanel(service);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.add(dashboardPanel, "dashboard");
        contentPanel.add(listPanel, "list");
        contentPanel.add(formPanel, "form");
        contentPanel.add(historyPanel, "history");

        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Theme.BG);
        main.add(buildSidebar(), BorderLayout.WEST);
        main.add(contentPanel, BorderLayout.CENTER);
        setContentPane(main);

        refreshAll();
        showDashboard();
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setBackground(Theme.SIDEBAR);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(210, 0));
        side.setBorder(BorderFactory.createEmptyBorder(18, 12, 18, 12));

        JLabel title = new JLabel("E-TICKET");
        title.setForeground(Theme.TEXT_LIGHT);
        title.setFont(Theme.SUBTITLE_FONT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Ticket Purchase");
        subtitle.setForeground(Theme.TEXT_LIGHT);
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(title);
        side.add(subtitle);
        side.add(Box.createVerticalStrut(20));

        JButton dashboardBtn = navButton("Dashboard", e -> showDashboard());
        JButton listBtn = navButton("List Data", e -> showList());
        JButton formBtn = navButton("Input Data", e -> openForm(null));
        JButton historyBtn = navButton("History", e -> showHistory());

        dashboardBtn.setIcon(IconFactory.letterIcon("D", Theme.ACCENT, Theme.TEXT_LIGHT));
        listBtn.setIcon(IconFactory.letterIcon("L", Theme.ACCENT_GREEN, Theme.TEXT_LIGHT));
        formBtn.setIcon(IconFactory.letterIcon("I", Theme.ACCENT, Theme.TEXT_LIGHT));
        historyBtn.setIcon(IconFactory.letterIcon("H", Theme.ACCENT_GREEN, Theme.TEXT_LIGHT));

        side.add(dashboardBtn);
        side.add(Box.createVerticalStrut(8));
        side.add(listBtn);
        side.add(Box.createVerticalStrut(8));
        side.add(formBtn);
        side.add(Box.createVerticalStrut(8));
        side.add(historyBtn);
        side.add(Box.createVerticalGlue());

        return side;
    }

    private JButton navButton(String label, java.awt.event.ActionListener listener) {
        JButton button = new JButton(label);
        Theme.styleNavButton(button);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.addActionListener(listener);
        return button;
    }

    private void openForm(Purchase purchase) {
        formPanel.setPurchase(purchase);
        cardLayout.show(contentPanel, "form");
    }

    private void onFormSaved() {
        refreshAll();
        showList();
    }

    private void showDashboard() {
        cardLayout.show(contentPanel, "dashboard");
    }

    private void showList() {
        cardLayout.show(contentPanel, "list");
    }

    private void showHistory() {
        cardLayout.show(contentPanel, "history");
    }

    private void refreshAll() {
        dashboardPanel.refresh();
        listPanel.refresh();
        historyPanel.refresh();
    }
}
