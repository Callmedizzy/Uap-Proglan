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
    private final CardLayout adminLayout;
    private final JPanel adminContentPanel;
    private final CardLayout rootLayout;
    private final JPanel rootPanel;
    private final JPanel adminPanel;
    private final LoginPanel loginPanel;
    private final MemberPanel memberPanel;

    public AppFrame() {
        super("E-Ticket Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1080, 660);
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

        adminLayout = new CardLayout();
        adminContentPanel = new JPanel(adminLayout);
        adminContentPanel.add(dashboardPanel, "dashboard");
        adminContentPanel.add(listPanel, "list");
        adminContentPanel.add(formPanel, "form");
        adminContentPanel.add(historyPanel, "history");

        adminPanel = new JPanel(new BorderLayout());
        adminPanel.setBackground(Theme.BG);
        adminPanel.add(buildSidebar(), BorderLayout.WEST);
        adminPanel.add(adminContentPanel, BorderLayout.CENTER);

        memberPanel = new MemberPanel(service, this::onMemberSaved, this::showLogin);
        loginPanel = new LoginPanel(this::handleLogin);

        rootLayout = new CardLayout();
        rootPanel = new JPanel(rootLayout);
        rootPanel.add(loginPanel, "login");
        rootPanel.add(adminPanel, "admin");
        rootPanel.add(memberPanel, "member");
        setContentPane(rootPanel);

        refreshAll();
        showLogin();
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setBackground(Theme.SIDEBAR);
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setPreferredSize(new Dimension(190, 0));
        side.setBorder(BorderFactory.createEmptyBorder(16, 12, 16, 12));

        JPanel brand = buildBrandPanel();
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(brand);
        side.add(Box.createVerticalStrut(18));

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
        JButton logoutBtn = navButton("Logout", e -> showLogin());
        logoutBtn.setIcon(IconFactory.letterIcon("O", Theme.ACCENT_GREEN, Theme.TEXT_LIGHT));

        side.add(Box.createVerticalGlue());
        side.add(logoutBtn);

        return side;
    }

    private JPanel buildBrandPanel() {
        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("E - Ticket Pengajian");
        title.setForeground(Theme.TEXT_LIGHT);
        title.setFont(Theme.SUBTITLE_FONT);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("Ticket Purchase Pengajian");
        subtitle.setForeground(Theme.TEXT_LIGHT);
        subtitle.setFont(Theme.BODY_FONT);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(title);
        brand.add(subtitle);
        return brand;
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
        adminLayout.show(adminContentPanel, "form");
    }

    private void onFormSaved() {
        refreshAll();
        showList();
    }

    private void showDashboard() {
        adminLayout.show(adminContentPanel, "dashboard");
    }

    private void showList() {
        adminLayout.show(adminContentPanel, "list");
    }

    private void showHistory() {
        adminLayout.show(adminContentPanel, "history");
    }

    private void showLogin() {
        loginPanel.reset();
        rootLayout.show(rootPanel, "login");
    }

    private void showAdmin() {
        rootLayout.show(rootPanel, "admin");
        showDashboard();
    }

    private void showMember() {
        rootLayout.show(rootPanel, "member");
    }

    private void handleLogin(LoginPanel.Role role, String username) {
        if (role == LoginPanel.Role.ADMIN) {
            refreshAll();
            showAdmin();
        } else {
            memberPanel.prepareFor(username);
            showMember();
        }
    }

    private void onMemberSaved() {
        refreshAll();
    }

    private void refreshAll() {
        dashboardPanel.refresh();
        listPanel.refresh();
        historyPanel.refresh();
    }
}
