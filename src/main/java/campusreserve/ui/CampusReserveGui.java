package campusreserve.ui;

import campusreserve.database.DataManager;
import campusreserve.model.Equipment;
import campusreserve.model.Reservation;
import campusreserve.model.Role;
import campusreserve.model.Room;
import campusreserve.model.User;
import campusreserve.resources.ResourceManager;
import campusreserve.reservations.Schedule;
import campusreserve.users.UserManager;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class CampusReserveGui
{
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Color ACCENT_COLOR = new Color(31, 78, 121);
    private static final Color ACCENT_DARK = new Color(23, 58, 92);
    private static final Color ACCENT_LIGHT = new Color(70, 130, 180);
    private static final Color SOFT_TEXT = new Color(97, 108, 122);
    private static final Color BACKGROUND_COLOR = new Color(246, 248, 252);
    private static final Color CARD_COLOR = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(219, 226, 236);
    private static final Color DARK_BACKGROUND = new Color(16, 18, 22);
    private static final Color DARK_CARD = new Color(26, 29, 35);
    private static final Color DARK_BORDER = new Color(52, 60, 70);
    private static final Color DARK_TEXT = new Color(220, 226, 234);
    private static final Color DARK_SOFT_TEXT = new Color(154, 165, 179);
    private static final Color DARK_ACCENT = new Color(82, 140, 198);
    private static final Color DARK_ACCENT_DARK = new Color(44, 78, 117);
    private static final Color DARK_ACCENT_LIGHT = new Color(112, 170, 228);
    private static final Path SETTINGS_FILE = Path.of(System.getProperty("user.home"), ".campusreserve-ui.properties");
    private static final String DARK_MODE_KEY = "darkMode";

    private final ResourceManager resourceManager;
    private final Schedule schedule;
    private final UserManager userManager;
    private final DataManager dataManager;

    private JFrame frame;
    private CardLayout cardLayout;
    private JPanel rootPanel;
    private JTabbedPane tabbedPane;
    private boolean darkMode = false;

    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;
    private JLabel loggedUserLabel;
    private JLabel roomsCountValue;
    private JLabel reservationsCountValue;
    private JLabel usersCountValue;

    private final DefaultListModel<Room> roomListModel = new DefaultListModel<>();
    private final DefaultComboBoxModel<Room> reservationRoomModel = new DefaultComboBoxModel<>();
    private final DefaultListModel<Reservation> reservationListModel = new DefaultListModel<>();
    private final DefaultListModel<User> userListModel = new DefaultListModel<>();

    private JList<Room> roomList;
    private JComboBox<Room> reservationRoomCombo;
    private JList<Reservation> reservationList;
    private JList<User> userList;

    private JTextArea roomDetailsArea;
    private JTextField startField;
    private JTextField endField;

    private User loggedInUser;

    public CampusReserveGui(ResourceManager resourceManager, Schedule schedule, UserManager userManager, DataManager dataManager)
    {
        this.resourceManager = resourceManager;
        this.schedule = schedule;
        this.userManager = userManager;
        this.dataManager = dataManager;
        loadThemePreference();
    }

    public void show()
    {
        applyLookAndFeel();
        applyModernTheme();
        seedDemoRoomsIfNeeded();

        frame = new JFrame("CampusReserve - system rezerwacji");
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(1100, 720));
        frame.setLocationRelativeTo(null);

        rootPanel = new JPanel();
        cardLayout = new CardLayout();
        rootPanel.setLayout(cardLayout);
        rootPanel.add(buildLoginView(), "LOGIN");
        rootPanel.add(buildMainView(), "MAIN");

        frame.setContentPane(rootPanel);
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                exitApplication();
            }
        });

        cardLayout.show(rootPanel, loggedInUser == null ? "LOGIN" : "MAIN");
        frame.setVisible(true);
    }

    private void applyLookAndFeel()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (Exception ignored)
        {
        }
    }

    private void applyModernTheme()
    {
        UIManager.put("Panel.background", themeBackground());
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 13));
        UIManager.put("Label.foreground", themeText());
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 13));
        UIManager.put("Button.background", themeCard());
        UIManager.put("Button.foreground", themeText());
        UIManager.put("Button.focus", themeBorder());
        UIManager.put("Button.shadow", themeBorder());
        UIManager.put("Button.darkShadow", themeBorder());
        UIManager.put("Button.highlight", themeCard());
        UIManager.put("Button.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeBorder()),
                new EmptyBorder(6, 14, 6, 14)));
        UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 13));
        UIManager.put("TextField.background", themeCard());
        UIManager.put("TextField.foreground", themeText());
        UIManager.put("TextField.caretForeground", themeText());
        UIManager.put("PasswordField.font", new Font("SansSerif", Font.PLAIN, 13));
        UIManager.put("PasswordField.background", themeCard());
        UIManager.put("PasswordField.foreground", themeText());
        UIManager.put("PasswordField.caretForeground", themeText());
        UIManager.put("ComboBox.font", new Font("SansSerif", Font.PLAIN, 13));
        UIManager.put("ComboBox.background", themeCard());
        UIManager.put("ComboBox.foreground", themeText());
        UIManager.put("List.background", themeCard());
        UIManager.put("List.foreground", themeText());
        UIManager.put("List.selectionBackground", darkMode ? new Color(45, 52, 62) : new Color(214, 226, 238));
        UIManager.put("List.selectionForeground", themeText());
        UIManager.put("TabbedPane.font", new Font("SansSerif", Font.BOLD, 13));
        UIManager.put("TabbedPane.background", themeBackground());
        UIManager.put("TabbedPane.foreground", themeText());
        UIManager.put("TabbedPane.selected", themeCard());
        UIManager.put("TabbedPane.contentAreaColor", themeCard());
        UIManager.put("TabbedPane.unselectedBackground", themeBackground());
        UIManager.put("TabbedPane.selectedForeground", themeText());
        UIManager.put("TabbedPane.tabAreaBackground", themeBackground());
        UIManager.put("TabbedPane.focus", themeBorder());
        UIManager.put("TabbedPane.highlight", themeBorder());
        UIManager.put("TabbedPane.shadow", themeBorder());
        UIManager.put("OptionPane.background", themeCard());
        UIManager.put("OptionPane.foreground", themeText());
        UIManager.put("OptionPane.messageForeground", themeText());
        UIManager.put("OptionPane.buttonBackground", themeCard());
        UIManager.put("OptionPane.buttonForeground", themeText());
        UIManager.put("OptionPane.buttonFocus", themeBorder());
        UIManager.put("OptionPane.border", BorderFactory.createEmptyBorder(12, 12, 12, 12));
        UIManager.put("OptionPane.messageAreaBorder", BorderFactory.createEmptyBorder(12, 12, 8, 12));
        UIManager.put("OptionPane.buttonAreaBorder", BorderFactory.createEmptyBorder(0, 12, 12, 12));
        UIManager.put("RootPane.background", themeBackground());
        UIManager.put("Dialog.background", themeCard());
    }

    private Color themeBackground()
    {
        return darkMode ? DARK_BACKGROUND : BACKGROUND_COLOR;
    }

    private Color themeCard()
    {
        return darkMode ? DARK_CARD : CARD_COLOR;
    }

    private Color themeBorder()
    {
        return darkMode ? DARK_BORDER : BORDER_COLOR;
    }

    private Color themeText()
    {
        return darkMode ? DARK_TEXT : new Color(50, 58, 69);
    }

    private Color themeSoftText()
    {
        return darkMode ? DARK_SOFT_TEXT : SOFT_TEXT;
    }

    private Color themeAccent()
    {
        return darkMode ? DARK_ACCENT : ACCENT_COLOR;
    }

    private Color themeAccentDark()
    {
        return darkMode ? DARK_ACCENT_DARK : ACCENT_DARK;
    }

    private Color themeAccentLight()
    {
        return darkMode ? DARK_ACCENT_LIGHT : ACCENT_LIGHT;
    }

    private class PremiumTabbedPaneUI extends BasicTabbedPaneUI
    {
        @Override
        protected void paintTabBackground(java.awt.Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            g.setColor(isSelected ? themeCard() : themeBackground());
            g.fillRect(x, y, w, h);
        }

        @Override
        protected void paintTabBorder(java.awt.Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected)
        {
            g.setColor(themeBorder());
            g.drawRect(x, y, w, h);
        }

        @Override
        protected void paintContentBorder(java.awt.Graphics g, int tabPlacement, int selectedIndex)
        {
            g.setColor(themeBorder());
            g.drawRect(0, 0, tabPane.getWidth() - 1, tabPane.getHeight() - 1);
        }
    }

    private void seedDemoRoomsIfNeeded()
    {
        if (!resourceManager.getAllBuildings().isEmpty())
        {
            return;
        }

        resourceManager.addRoom(new Room(101, "Aula 101", "Budynek A", 30, "Wykładowa", true, true));
        resourceManager.addRoom(new Room(102, "Laboratorium 102", "Budynek A", 20, "Laboratoryjna", true, true));
        resourceManager.addRoom(new Room(201, "Sala 201", "Budynek B", 45, "Seminaryjna", true, true));
        resourceManager.addRoom(new Room(301, "Sala konferencyjna", "Budynek C", 18, "Konferencyjna", true, true));
        seedDemoEquipmentIfNeeded();
    }

    private void seedDemoEquipmentIfNeeded()
    {
        if (!resourceManager.getEquipmentForRoom(resourceManager.getRoomByNumber(101).getRoomId()).isEmpty())
        {
            return;
        }

        resourceManager.addEquipment(new Equipment("Przykładowe wyposażenie do zajęć audytoryjnych", "Projektor multimedialny", 1, false, resourceManager.getRoomByNumber(101).getRoomId()));
        resourceManager.addEquipment(new Equipment("Sterowanie prezentacją i nagłośnienie", "Tablica interaktywna", 1, false, resourceManager.getRoomByNumber(101).getRoomId()));

        resourceManager.addEquipment(new Equipment("Stanowiska laboratoryjne", "Zestaw komputerów", 20, false, resourceManager.getRoomByNumber(102).getRoomId()));
        resourceManager.addEquipment(new Equipment("Wyposażenie do pracy w grupach", "Mikrofony bezprzewodowe", 4, false, resourceManager.getRoomByNumber(102).getRoomId()));

        resourceManager.addEquipment(new Equipment("Wyposażenie do seminarium", "Ekran projekcyjny", 1, false, resourceManager.getRoomByNumber(201).getRoomId()));
        resourceManager.addEquipment(new Equipment("Prezentacje i spotkania", "Flipchart", 1, false, resourceManager.getRoomByNumber(201).getRoomId()));

        resourceManager.addEquipment(new Equipment("Sala spotkań i wideokonferencji", "Monitor 75 cali", 1, false, resourceManager.getRoomByNumber(301).getRoomId()));
        resourceManager.addEquipment(new Equipment("Komunikacja hybrydowa", "Kamera konferencyjna", 1, false, resourceManager.getRoomByNumber(301).getRoomId()));
    }

    private JPanel buildLoginView()
    {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(themeBackground());
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setOpaque(true);
        header.setBackground(themeAccent());
        header.setBorder(new EmptyBorder(24, 24, 24, 24));
        JLabel title = new JLabel("CampusReserve");
        title.setFont(new Font("SansSerif", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        JLabel subtitle = new JLabel("System rezerwacji sal i zasobów uczelni");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitle.setForeground(darkMode ? DARK_TEXT : new Color(225, 235, 245));
        header.add(title, BorderLayout.NORTH);
        header.add(subtitle, BorderLayout.SOUTH);
        panel.add(header, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(themeCard());
        center.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeBorder()),
                new EmptyBorder(18, 18, 18, 18)));

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0;
        gc.gridy = 0;

        center.add(new JLabel("Email:"), gc);
        gc.gridx = 1;
        loginEmailField = new JTextField(24);
        center.add(loginEmailField, gc);

        gc.gridx = 0;
        gc.gridy++;
        center.add(new JLabel("Hasło:"), gc);
        gc.gridx = 1;
        loginPasswordField = new JPasswordField(24);
        center.add(loginPasswordField, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        styleButtonStrip(buttons);
        JButton loginButton = new JButton("Zaloguj się");
        bindAction(loginButton, this::attemptLogin);
        stylePrimaryButton(loginButton);
        JButton registerButton = new JButton("Rejestracja");
        bindAction(registerButton, this::showRegisterDialog);
        styleSecondaryButton(registerButton);
        buttons.add(loginButton);
        buttons.add(registerButton);
        center.add(buttons, gc);

        gc.gridy++;
        JLabel hint = new JLabel("Demo konta: admin@campus.pl / admin123 | anna.kowalska@campus.pl / student123 | piotr.nowak@campus.pl / pracownik123");
        hint.setForeground(themeSoftText());
        center.add(hint, gc);

        panel.add(center, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMainView()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(themeBackground());
        panel.setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(themeAccentDark());
        topBar.setBorder(new EmptyBorder(12, 14, 12, 14));
        JLabel appTitle = new JLabel("CampusReserve - panel użytkownika");
        appTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        appTitle.setForeground(Color.WHITE);
        loggedUserLabel = new JLabel(" ");
        loggedUserLabel.setForeground(darkMode ? DARK_TEXT : new Color(230, 236, 242));
        JButton logoutButton = new JButton("Wyloguj");
        bindAction(logoutButton, this::logout);
        styleSecondaryButton(logoutButton);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        right.setOpaque(false);
        right.add(loggedUserLabel);
        // dark mode toggle (recreates UI on change)
        javax.swing.JToggleButton darkToggle = new javax.swing.JToggleButton("Dark");
        darkToggle.setSelected(darkMode);
        darkToggle.setFocusPainted(false);
        darkToggle.addActionListener(e -> {
            darkMode = darkToggle.isSelected();
            saveThemePreference();
            try { frame.dispose(); } catch (Exception ignored) {}
            javax.swing.SwingUtilities.invokeLater(this::show);
        });
        styleSecondaryButton(darkToggle);
        right.add(darkToggle);
        right.add(logoutButton);

        topBar.add(appTitle, BorderLayout.WEST);
        topBar.add(right, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tabbedPane.setBackground(themeBackground());
        tabbedPane.setForeground(themeText());
        tabbedPane.setOpaque(true);
        tabbedPane.setUI(new PremiumTabbedPaneUI());
        tabbedPane.addTab("Sale", buildRoomsTab());
        tabbedPane.addTab("Rezerwacje", buildReservationsTab());
        tabbedPane.addTab("Użytkownicy", buildUsersTab());

        JPanel contentArea = new JPanel(new BorderLayout(0, 0));
        contentArea.setOpaque(false);
        contentArea.add(buildDashboardPanel(), BorderLayout.NORTH);
        contentArea.add(tabbedPane, BorderLayout.CENTER);

        panel.add(contentArea, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildDashboardPanel()
    {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(14, 0, 14, 0));

        JPanel dashboard = new JPanel(new java.awt.GridLayout(1, 3, 14, 0));
        dashboard.setOpaque(false);

        roomsCountValue = new JLabel("0");
        reservationsCountValue = new JLabel("0");
        usersCountValue = new JLabel("0");

        dashboard.add(createMetricCard("Sale", "Dostępne pomieszczenia w systemie", roomsCountValue, themeAccent()));
        dashboard.add(createMetricCard("Rezerwacje", "Wszystkie zaplanowane rezerwacje", reservationsCountValue, themeAccentLight()));
        dashboard.add(createMetricCard("Użytkownicy", "Konta w bazie użytkowników", usersCountValue, themeAccentDark()));

        wrapper.add(dashboard, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createMetricCard(String title, String subtitle, JLabel valueLabel, Color accent)
    {
        JPanel card = new JPanel(new BorderLayout(8, 6));
        card.setBackground(themeCard());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeBorder()),
                new EmptyBorder(18, 18, 18, 18)));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        titleLabel.setForeground(themeSoftText());

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 30));
        valueLabel.setForeground(accent);

        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setForeground(themeSoftText());

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(subtitleLabel, BorderLayout.SOUTH);
        return card;
    }

    private JPanel buildRoomsTab()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(themeBackground());

        roomList = new JList<>(roomListModel);
        roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomList.setCellRenderer(new RoomRenderer());
        roomList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting())
            {
                Room selected = roomList.getSelectedValue();
                showRoomDetails(selected);
                if (selected != null)
                {
                    reservationRoomCombo.setSelectedItem(selected);
                }
            }
        });

        roomDetailsArea = new JTextArea();
        roomDetailsArea.setEditable(false);
        roomDetailsArea.setLineWrap(true);
        roomDetailsArea.setWrapStyleWord(true);
        roomDetailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        roomDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        roomDetailsArea.setBackground(themeCard());
        roomDetailsArea.setForeground(themeText());

        JPanel left = new JPanel(new BorderLayout(8, 8));
        left.setBackground(themeCard());
        left.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(themeBorder()), new EmptyBorder(10, 10, 10, 10)));
        JLabel roomsLabel = new JLabel("Lista sal");
        roomsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        left.add(roomsLabel, BorderLayout.NORTH);
        JScrollPane roomScroll = new JScrollPane(roomList);
        roomScroll.setBorder(BorderFactory.createLineBorder(themeBorder()));
        left.add(roomScroll, BorderLayout.CENTER);
        left.setPreferredSize(new Dimension(460, 500));

        JPanel right = new JPanel(new BorderLayout(8, 8));
        right.setBackground(themeCard());
        right.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(themeBorder()), new EmptyBorder(10, 10, 10, 10)));
        JLabel detailsLabel = new JLabel("Szczegóły sali i wyposażenie");
        detailsLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        right.add(detailsLabel, BorderLayout.NORTH);
        JScrollPane detailsScroll = new JScrollPane(roomDetailsArea);
        detailsScroll.setBorder(BorderFactory.createLineBorder(themeBorder()));
        right.add(detailsScroll, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setOpaque(false);
        JButton refreshButton = new JButton("Odśwież listę");
        bindAction(refreshButton, this::refreshRooms);
        styleSecondaryButton(refreshButton);
        actions.add(refreshButton);

        panel.add(actions, BorderLayout.NORTH);
        panel.add(left, BorderLayout.WEST);
        panel.add(right, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildReservationsTab()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(themeBackground());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(themeCard());
        form.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(themeBorder()), new EmptyBorder(16, 16, 16, 16)));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0;
        gc.gridy = 0;

        form.add(new JLabel("Sala:"), gc);
        gc.gridx = 1;
        reservationRoomCombo = new JComboBox<>(reservationRoomModel);
        applyComboBoxTheme(reservationRoomCombo, new RoomRenderer());
        bindAction(reservationRoomCombo, this::refreshReservationList);
        form.add(reservationRoomCombo, gc);

        gc.gridx = 0;
        gc.gridy++;
        form.add(new JLabel("Początek (yyyy-MM-dd HH:mm):"), gc);
        gc.gridx = 1;
        startField = new JTextField(24);
        form.add(startField, gc);

        gc.gridx = 0;
        gc.gridy++;
        form.add(new JLabel("Koniec (yyyy-MM-dd HH:mm):"), gc);
        gc.gridx = 1;
        endField = new JTextField(24);
        form.add(endField, gc);

        gc.gridx = 0;
        gc.gridy++;
        gc.gridwidth = 2;
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        styleButtonStrip(buttons);
        JButton reserveButton = new JButton("Zarezerwuj");
        bindAction(reserveButton, this::createReservation);
        stylePrimaryButton(reserveButton);
        JButton refreshButton = new JButton("Odśwież harmonogram");
        bindAction(refreshButton, this::refreshReservationList);
        styleSecondaryButton(refreshButton);
        JButton cancelButton = new JButton("Anuluj wybraną rezerwację");
        bindAction(cancelButton, this::cancelSelectedReservation);
        styleDangerButton(cancelButton);
        buttons.add(reserveButton);
        buttons.add(refreshButton);
        buttons.add(cancelButton);
        form.add(buttons, gc);

        reservationList = new JList<>(reservationListModel);
        reservationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationList.setCellRenderer(new ReservationRenderer());

        JPanel schedulePanel = new JPanel(new BorderLayout(8, 8));
        schedulePanel.setBackground(themeCard());
        schedulePanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(themeBorder()), new EmptyBorder(10, 10, 10, 10)));
        JLabel scheduleLabel = new JLabel("Harmonogram sali");
        scheduleLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        schedulePanel.add(scheduleLabel, BorderLayout.NORTH);
        JScrollPane reservationScroll = new JScrollPane(reservationList);
        reservationScroll.setBorder(BorderFactory.createLineBorder(themeBorder()));
        schedulePanel.add(reservationScroll, BorderLayout.CENTER);

        panel.add(form, BorderLayout.NORTH);
        panel.add(schedulePanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildUsersTab()
    {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(themeBackground());
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.setCellRenderer(new UserRenderer());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        styleButtonStrip(top);
        JButton refreshButton = new JButton("Odśwież użytkowników");
        bindAction(refreshButton, this::refreshUsers);
        styleSecondaryButton(refreshButton);
        JButton activateButton = new JButton("Aktywuj wybranego użytkownika");
        bindAction(activateButton, this::activateSelectedUser);
        stylePrimaryButton(activateButton);
        JButton deactivateButton = new JButton("Dezaktywuj wybranego użytkownika");
        bindAction(deactivateButton, this::deactivateSelectedUser);
        styleDangerButton(deactivateButton);
        top.add(refreshButton);
        top.add(activateButton);
        top.add(deactivateButton);

        panel.add(top, BorderLayout.NORTH);
        JScrollPane usersScroll = new JScrollPane(userList);
        usersScroll.setBorder(BorderFactory.createLineBorder(themeBorder()));
        panel.add(usersScroll, BorderLayout.CENTER);
        return panel;
    }

    private void attemptLogin()
    {
        try
        {
            String email = loginEmailField.getText().trim();
            String password = new String(loginPasswordField.getPassword()).trim();
            User user = userManager.login(email, password);

            if (user == null)
            {
                showStyledMessageDialog(frame, "Nieprawidłowy email lub hasło.", "Logowanie", JOptionPane.WARNING_MESSAGE);
                return;
            }

            loggedInUser = user;
            loginPasswordField.setText("");
            loginEmailField.setText("");
            enterMainView();
        }
        catch (IllegalArgumentException ex)
        {
            showStyledMessageDialog(frame, ex.getMessage(), "Logowanie", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex)
        {
            showStyledMessageDialog(frame, "Nie udało się zalogować: " + ex.getMessage(), "Logowanie", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRegisterDialog()
    {
        JDialog dialog = new JDialog(frame, "Rejestracja", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(460, 360);
        dialog.setLocationRelativeTo(frame);

        JPanel content = new JPanel(new GridBagLayout());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));
        content.setBackground(themeCard());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridx = 0;
        gc.gridy = 0;

        JTextField firstNameField = new JTextField(20);
        JTextField lastNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Pracownik"});
        applyComboBoxTheme(roleBox, null);
        JTextField extraField = new JTextField(20);
        JLabel extraLabel = new JLabel("Numer albumu:");

        bindAction(roleBox, () -> {
            String selected = (String) roleBox.getSelectedItem();
            extraLabel.setText("Student".equals(selected) ? "Numer albumu:" : "Wydział/Katedra:");
            extraField.setText("");
        });

        addFormRow(content, gc, "Imię:", firstNameField);
        addFormRow(content, gc, "Nazwisko:", lastNameField);
        addFormRow(content, gc, "Email:", emailField);
        addFormRow(content, gc, "Hasło:", passwordField);
        addFormRow(content, gc, "Typ konta:", roleBox);
        addFormRow(content, gc, extraLabel, extraField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        JButton cancelButton = new JButton("Anuluj");
        bindAction(cancelButton, dialog::dispose);
        styleSecondaryButton(cancelButton);
        JButton saveButton = new JButton("Zarejestruj");
        bindAction(saveButton, () -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String selectedRole = (String) roleBox.getSelectedItem();
            String extra = extraField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || extra.isEmpty())
            {
                showStyledMessageDialog(dialog, "Wypełnij wszystkie pola.", "Rejestracja", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try
            {
                if ("Student".equals(selectedRole))
                {
                    userManager.registerStudent(firstName, lastName, email, password, extra);
                }
                else
                {
                    userManager.registerPracownik(firstName, lastName, email, password, extra);
                }

                persistUsers();
                showStyledMessageDialog(dialog, "Konto zostało utworzone. Możesz się zalogować.", "Rejestracja", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            }
            catch (IllegalArgumentException ex)
            {
                showStyledMessageDialog(dialog, ex.getMessage(), "Rejestracja", JOptionPane.ERROR_MESSAGE);
            }
            catch (Exception ex)
            {
                showStyledMessageDialog(dialog, "Nie udało się zarejestrować: " + ex.getMessage(), "Rejestracja", JOptionPane.ERROR_MESSAGE);
            }
        });
        stylePrimaryButton(saveButton);
        buttons.add(cancelButton);
        buttons.add(saveButton);

        gc.gridx = 0;
        gc.gridy++;
        gc.gridwidth = 2;
        content.add(buttons, gc);

        dialog.setContentPane(content);
        dialog.setVisible(true);
    }

    private void enterMainView()
    {
        loggedUserLabel.setText("Zalogowano: " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName() + " [" + loggedInUser.getRole() + "]");
        refreshAll();
        tabbedPane.setEnabledAt(2, loggedInUser.getRole() == Role.ADMIN);
        if (loggedInUser.getRole() != Role.ADMIN)
        {
            tabbedPane.setTitleAt(2, "Użytkownicy (tylko admin)");
        }
        else
        {
            tabbedPane.setTitleAt(2, "Użytkownicy");
        }
        cardLayout.show(rootPanel, "MAIN");
    }

    private void logout()
    {
        loggedInUser = null;
        loggedUserLabel.setText(" ");
        loginPasswordField.setText("");
        refreshAll();
        cardLayout.show(rootPanel, "LOGIN");
    }

    private void refreshAll()
    {
        refreshRooms();
        refreshReservationList();
        refreshUsers();
        updateDashboardMetrics();
    }

    private void updateDashboardMetrics()
    {
        if (roomsCountValue != null)
        {
            roomsCountValue.setText(String.valueOf(collectAllRooms().size()));
        }

        if (reservationsCountValue != null)
        {
            reservationsCountValue.setText(String.valueOf(countAllReservations()));
        }

        if (usersCountValue != null)
        {
            usersCountValue.setText(String.valueOf(userManager.getAllUsers().size()));
        }
    }

    private int countAllReservations()
    {
        int total = 0;
        for (Room room : collectAllRooms())
        {
            total += schedule.getScheduleForResource(room.getRoomId()).size();
        }
        return total;
    }

    private void refreshRooms()
    {
        Room selectedRoom = roomList == null ? null : roomList.getSelectedValue();
        roomListModel.clear();
        reservationRoomModel.removeAllElements();

        for (Room room : collectAllRooms())
        {
            roomListModel.addElement(room);
            reservationRoomModel.addElement(room);
        }

        if (roomListModel.isEmpty())
        {
            showRoomDetails(null);
            return;
        }

        if (selectedRoom != null)
        {
            roomList.setSelectedValue(selectedRoom, true);
        }
        else
        {
            roomList.setSelectedIndex(0);
        }

        if (reservationRoomCombo.getItemCount() > 0 && reservationRoomCombo.getSelectedItem() == null)
        {
            reservationRoomCombo.setSelectedIndex(0);
        }

        showRoomDetails(roomList.getSelectedValue());
    }

    private List<Room> collectAllRooms()
    {
        ArrayList<Room> rooms = new ArrayList<>();
        Set<String> buildings = resourceManager.getAllBuildings();
        for (String building : buildings)
        {
            rooms.addAll(resourceManager.getRoomsByBuilding(building));
        }
        rooms.sort(Comparator.comparingInt(Room::getRoomNumber));
        return rooms;
    }

    private void showRoomDetails(Room room)
    {
        if (room == null)
        {
            roomDetailsArea.setText("Brak wybranej sali.");
            return;
        }

        String equipmentText = formatEquipmentForRoom(room);
        String details = "Numer sali: " + room.getRoomNumber() + '\n'
                + "Nazwa: " + room.getRoomName() + '\n'
                + "Budynek: " + room.getBuildingName() + '\n'
                + "Pojemność: " + room.getCapacity() + '\n'
                + "Typ: " + room.getRoomType() + '\n'
                + "Aktywna: " + (room.isActive() ? "Tak" : "Nie") + '\n'
                + "Dostępna: " + (room.isAvailable() ? "Tak" : "Nie") + '\n'
                + "\nWyposażenie przykładowe:\n"
                + equipmentText + '\n'
                + "\nID: " + room.getRoomId();
        roomDetailsArea.setText(details);
    }

    private String formatEquipmentForRoom(Room room)
    {
        ArrayList<Equipment> equipmentList = resourceManager.getEquipmentForRoom(room.getRoomId());
        if (equipmentList.isEmpty())
        {
            return "- brak przypisanego wyposażenia";
        }

        StringBuilder builder = new StringBuilder();
        for (Equipment equipment : equipmentList)
        {
            builder.append("- ")
                    .append(equipment.getEquipmentName())
                    .append(" x")
                    .append(equipment.getEquipmentQuantity());

            if (equipment.getEquipmentDescription() != null && !equipment.getEquipmentDescription().isBlank())
            {
                builder.append(" (").append(equipment.getEquipmentDescription()).append(")");
            }

            if (equipment.isBroken())
            {
                builder.append(" [uszkodzone]");
            }

            builder.append('\n');
        }

        return builder.toString().trim();
    }

    private void refreshReservationList()
    {
        reservationListModel.clear();
        Room selectedRoom = (Room) reservationRoomCombo.getSelectedItem();
        if (selectedRoom == null)
        {
            return;
        }

        ArrayList<Reservation> reservations = schedule.getScheduleForResource(selectedRoom.getRoomId());
        for (Reservation reservation : reservations)
        {
            reservationListModel.addElement(reservation);
        }
    }

    private void createReservation()
    {
        if (loggedInUser == null)
        {
            showStyledMessageDialog(frame, "Najpierw zaloguj się do systemu.", "Rezerwacja", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Room selectedRoom = (Room) reservationRoomCombo.getSelectedItem();
        if (selectedRoom == null)
        {
            showStyledMessageDialog(frame, "Wybierz salę.", "Rezerwacja", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try
        {
            LocalDateTime start = LocalDateTime.parse(startField.getText().trim(), DATE_FORMAT);
            LocalDateTime end = LocalDateTime.parse(endField.getText().trim(), DATE_FORMAT);
            Reservation reservation = new Reservation(loggedInUser.getUserId(), selectedRoom.getRoomId(), start, end);
            schedule.addReservation(reservation);
            showStyledMessageDialog(frame, "Rezerwacja została dodana.", "Rezerwacja", JOptionPane.INFORMATION_MESSAGE);
            refreshReservationList();
        }
        catch (DateTimeParseException ex)
        {
            showStyledMessageDialog(frame, "Nieprawidłowy format daty. Użyj: yyyy-MM-dd HH:mm", "Rezerwacja", JOptionPane.ERROR_MESSAGE);
        }
        catch (IllegalArgumentException | IllegalStateException ex)
        {
            showStyledMessageDialog(frame, ex.getMessage(), "Rezerwacja", JOptionPane.ERROR_MESSAGE);
        }
        catch (Exception ex)
        {
            showStyledMessageDialog(frame, "Nie udało się utworzyć rezerwacji: " + ex.getMessage(), "Rezerwacja", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelSelectedReservation()
    {
        Reservation selected = reservationList.getSelectedValue();
        if (selected == null)
        {
            showStyledMessageDialog(frame, "Wybierz rezerwację z listy.", "Anulowanie", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try
        {
            schedule.cancelReservation(selected.getReservationId());
            showStyledMessageDialog(frame, "Rezerwacja została anulowana.", "Anulowanie", JOptionPane.INFORMATION_MESSAGE);
            refreshReservationList();
        }
        catch (IllegalArgumentException ex)
        {
            showStyledMessageDialog(frame, ex.getMessage(), "Anulowanie", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshUsers()
    {
        userListModel.clear();
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN)
        {
            return;
        }

        for (User user : userManager.getAllUsers())
        {
            userListModel.addElement(user);
        }
    }

    private void deactivateSelectedUser()
    {
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN)
        {
            showStyledMessageDialog(frame, "Tylko administrator ma dostęp do tej funkcji.", "Użytkownicy", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User selected = userList.getSelectedValue();
        if (selected == null)
        {
            showStyledMessageDialog(frame, "Wybierz użytkownika z listy.", "Użytkownicy", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selected.getUserId().equals(loggedInUser.getUserId()))
        {
            showStyledMessageDialog(frame, "Nie możesz dezaktywować własnego konta.", "Użytkownicy", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try
        {
            userManager.deactivateUser(selected.getUserId());
            persistUsers();
            refreshUsers();
            showStyledMessageDialog(frame, "Użytkownik został dezaktywowany.", "Użytkownicy", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (IllegalArgumentException ex)
        {
            showStyledMessageDialog(frame, ex.getMessage(), "Użytkownicy", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void activateSelectedUser()
    {
        if (loggedInUser == null || loggedInUser.getRole() != Role.ADMIN)
        {
            showStyledMessageDialog(frame, "Tylko administrator ma dostęp do tej funkcji.", "Użytkownicy", JOptionPane.WARNING_MESSAGE);
            return;
        }

        User selected = userList.getSelectedValue();
        if (selected == null)
        {
            showStyledMessageDialog(frame, "Wybierz użytkownika z listy.", "Użytkownicy", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try
        {
            userManager.activateUser(selected.getUserId());
            persistUsers();
            refreshUsers();
            showStyledMessageDialog(frame, "Użytkownik został aktywowany.", "Użytkownicy", JOptionPane.INFORMATION_MESSAGE);
        }
        catch (IllegalArgumentException ex)
        {
            showStyledMessageDialog(frame, ex.getMessage(), "Użytkownicy", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void persistUsers()
    {
        dataManager.saveUsers(userManager.getAllUsers());
    }

    private void showStyledMessageDialog(Component parent, String message, String title, int messageType)
    {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setBackground(themeCard());
        content.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel messagePanel = new JPanel(new BorderLayout(14, 0));
        messagePanel.setOpaque(false);

        JLabel iconLabel = new JLabel();
        if (messageType == JOptionPane.ERROR_MESSAGE)
        {
            iconLabel.setIcon(UIManager.getIcon("OptionPane.errorIcon"));
        }
        else if (messageType == JOptionPane.WARNING_MESSAGE)
        {
            iconLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        }
        else if (messageType == JOptionPane.INFORMATION_MESSAGE)
        {
            iconLabel.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        }
        else
        {
            iconLabel.setIcon(UIManager.getIcon("OptionPane.questionIcon"));
        }
        messagePanel.add(iconLabel, BorderLayout.WEST);

        JTextArea messageArea = new JTextArea(message);
        messageArea.setEditable(false);
        messageArea.setOpaque(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setFont(new Font("SansSerif", Font.PLAIN, 13));
        messageArea.setForeground(themeText());
        messageArea.setColumns(30);
        messageArea.setRows(3);
        messageArea.setBorder(new EmptyBorder(4, 0, 4, 0));
        messagePanel.add(messageArea, BorderLayout.CENTER);

        content.add(messagePanel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttons.setOpaque(false);
        JButton okButton = new JButton("OK");
        stylePrimaryButton(okButton);
        bindAction(okButton, dialog::dispose);
        dialog.getRootPane().setDefaultButton(okButton);
        buttons.add(okButton);
        content.add(buttons, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setSize(Math.max(dialog.getWidth(), 430), dialog.getHeight());
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parent != null ? parent : frame);
        dialog.setVisible(true);
    }

    private void exitApplication()
    {
        try
        {
            persistUsers();
        }
        catch (Exception ignored)
        {
        }
        saveThemePreference();
        frame.dispose();
        System.exit(0);
    }

    private void loadThemePreference()
    {
        Properties properties = new Properties();
        if (!Files.exists(SETTINGS_FILE))
        {
            darkMode = false;
            return;
        }

        try (InputStream inputStream = Files.newInputStream(SETTINGS_FILE))
        {
            properties.load(inputStream);
            darkMode = Boolean.parseBoolean(properties.getProperty(DARK_MODE_KEY, "false"));
        }
        catch (IOException ignored)
        {
            darkMode = false;
        }
    }

    private void saveThemePreference()
    {
        Properties properties = new Properties();
        properties.setProperty(DARK_MODE_KEY, Boolean.toString(darkMode));

        try (OutputStream outputStream = Files.newOutputStream(SETTINGS_FILE))
        {
            properties.store(outputStream, "CampusReserve UI settings");
        }
        catch (IOException ignored)
        {
        }
    }

    private void addFormRow(JPanel panel, GridBagConstraints gc, String labelText, JComponent component)
    {
        JLabel label = new JLabel(labelText);
        int currentY = gc.gridy;

        GridBagConstraints left = (GridBagConstraints) gc.clone();
        left.gridx = 0;
        left.gridy = currentY;
        left.gridwidth = 1;
        panel.add(label, left);

        GridBagConstraints right = (GridBagConstraints) gc.clone();
        right.gridx = 1;
        right.gridy = currentY;
        right.gridwidth = 1;
        panel.add(component, right);

        gc.gridy = currentY + 1;
    }

    private void addFormRow(JPanel panel, GridBagConstraints gc, JLabel label, JComponent component)
    {
        int currentY = gc.gridy;

        GridBagConstraints left = (GridBagConstraints) gc.clone();
        left.gridx = 0;
        left.gridy = currentY;
        left.gridwidth = 1;
        panel.add(label, left);

        GridBagConstraints right = (GridBagConstraints) gc.clone();
        right.gridx = 1;
        right.gridy = currentY;
        right.gridwidth = 1;
        panel.add(component, right);

        gc.gridy = currentY + 1;
    }

    private void styleButtonStrip(JPanel panel)
    {
        panel.setBackground(themeCard());
        panel.setOpaque(true);
        panel.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

    private void bindAction(JButton button, Runnable action)
    {
        button.addActionListener(event -> {
            if (event != null)
            {
                action.run();
            }
        });
    }

    private void bindAction(JComboBox<?> comboBox, Runnable action)
    {
        comboBox.addActionListener(event -> {
            if (event != null)
            {
                action.run();
            }
        });
    }

    private void applyComboBoxTheme(JComboBox<?> comboBox, ListCellRenderer<?> renderer)
    {
        comboBox.setBackground(themeCard());
        comboBox.setForeground(themeText());
        comboBox.setBorder(BorderFactory.createLineBorder(themeBorder()));
        comboBox.setUI(new PremiumComboBoxUI());

        if (renderer != null)
        {
            comboBox.setRenderer((ListCellRenderer) renderer);
        }
        else
        {
            comboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
                JLabel label = new JLabel(value == null ? "" : value.toString());
                label.setOpaque(true);
                label.setBackground(isSelected ? (darkMode ? new Color(45, 52, 62) : new Color(214, 226, 238)) : themeCard());
                label.setForeground(themeText());
                label.setBorder(new EmptyBorder(2, 6, 2, 6));
                return label;
            });
        }
    }

    private class PremiumComboBoxUI extends BasicComboBoxUI
    {
        @Override
        protected JButton createArrowButton()
        {
            JButton button = new JButton("\u25BE");
            button.setBackground(themeCard());
            button.setForeground(themeText());
            button.setBorder(BorderFactory.createLineBorder(themeBorder()));
            button.setFocusPainted(false);
            button.setFocusable(false);
            button.setContentAreaFilled(true);
            button.setRolloverEnabled(false);
            return button;
        }
    }

    private void stylePrimaryButton(javax.swing.AbstractButton button)
    {
        styleButton(button, themeAccent(), Color.WHITE);
    }

    private void styleSecondaryButton(javax.swing.AbstractButton button)
    {
        styleButton(button, darkMode ? new Color(54, 63, 74) : new Color(235, 240, 246),
                darkMode ? DARK_TEXT : new Color(45, 54, 65));
    }

    private void styleDangerButton(javax.swing.AbstractButton button)
    {
        styleButton(button, new Color(216, 72, 72), Color.WHITE);
    }

    private void styleButton(javax.swing.AbstractButton button, Color background, Color foreground)
    {
        // adapt colors for dark mode
        Color bg = background;
        Color fg = foreground;
        if (darkMode)
        {
            // invert for light backgrounds or darken for accent
            if (isLight(bg))
            {
                bg = new Color(54, 63, 74);
                fg = new Color(220, 226, 234);
            }
            else
            {
                bg = bg.darker();
                fg = Color.WHITE;
            }
        }

        if (button instanceof javax.swing.JToggleButton)
        {
            button.setUI(new javax.swing.plaf.basic.BasicToggleButtonUI());
        }
        else
        {
            button.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        }

        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(true);
        button.setRolloverEnabled(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg.darker()),
                new EmptyBorder(8, 14, 8, 14)));
        button.setOpaque(true);
    }

    private boolean isLight(Color c)
    {
        // perceived luminance
        double lum = 0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue();
        return lum > 180;
    }

    private static class RoomRenderer extends JLabel implements ListCellRenderer<Room>
    {
        RoomRenderer()
        {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Room> list, Room value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (value == null)
            {
                setText("");
            }
            else
            {
                setText(value.getRoomNumber() + " - " + value.getRoomName() + " | " + value.getBuildingName() + " | " + value.getCapacity() + " os.");
            }

            if (isSelected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }

    private class ReservationRenderer extends JLabel implements ListCellRenderer<Reservation>
    {
        ReservationRenderer()
        {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Reservation> list, Reservation value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (value == null)
            {
                setText("");
            }
            else
            {
                String roomLabel = "nieznana sala";
                try
                {
                    Room room = resourceManager.getRoomByUUID(value.getResourceId());
                    roomLabel = room.getRoomNumber() + " - " + room.getRoomName();
                }
                catch (Exception ignored)
                {
                }

                String userLabel = value.getUserId().toString();
                try
                {
                    User user = userManager.getUserByUUID(value.getUserId());
                    userLabel = user.getFirstName() + " " + user.getLastName();
                }
                catch (Exception ignored)
                {
                }

                setText(roomLabel + " | " + userLabel + " | " + value.getStartTime().format(DATE_FORMAT) + " -> " + value.getEndTime().format(DATE_FORMAT));
            }

            if (isSelected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }

    private static class UserRenderer extends JLabel implements ListCellRenderer<User>
    {
        UserRenderer()
        {
            setOpaque(true);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends User> list, User value, int index, boolean isSelected, boolean cellHasFocus)
        {
            if (value == null)
            {
                setText("");
            }
            else
            {
                String extraInfo = "";
                if (value.getRole() == Role.STUDENT && value instanceof campusreserve.model.Student student)
                {
                    extraInfo = " | album: " + student.getAlbumNumber();
                }
                else if (value.getRole() == Role.PRACOWNIK && value instanceof campusreserve.model.Pracownik pracownik)
                {
                    extraInfo = " | dział: " + pracownik.getDepartment();
                }

                setText(value.getFirstName() + " " + value.getLastName() + " | " + value.getEmail() + " | " + value.getRole() + extraInfo + " | " + (value.isActive() ? "aktywny" : "nieaktywny"));
            }

            if (isSelected)
            {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            }
            else
            {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }
            return this;
        }
    }
}


