package com.schoolmanagement.ui;

import com.schoolmanagement.config.AppConfig;
import com.schoolmanagement.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Main application window for the School Management System
 */
public class MainWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
    
    private User currentUser;
    private JMenuBar menuBar;
    private JTabbedPane tabbedPane;
    private JLabel statusBar;
    private JLabel dateTimeLabel;
    private Timer clockTimer;
    
    // Modern UI Colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color LIGHT_TEXT_COLOR = new Color(127, 140, 141);
    private static final Color BORDER_COLOR = new Color(221, 221, 221);

    public MainWindow(User user) {
        logger.info("MainWindow constructor starting for user: {}", user.getUsername());
        this.currentUser = user;
        logger.info("Step 1: initializeComponents");
        initializeComponents();
        logger.info("Step 2: setupLayout");
        setupLayout();
        logger.info("Step 3: setupMenuBar");
        setupMenuBar();
        logger.info("Step 4: setupEventHandlers");
        setupEventHandlers();
        logger.info("Step 5: configureWindow");
        configureWindow();
        logger.info("Step 6: startClock");
        startClock();
        logger.info("MainWindow constructor completed successfully");
    }
    
    // Constructor for testing without user authentication
    public MainWindow() {
        // Create a mock user for testing
        this.currentUser = new User();
        this.currentUser.setUsername("test_user");
        this.currentUser.setRole(User.UserRole.ADMIN);
        this.currentUser.setFirstName("Test");
        this.currentUser.setLastName("User");
        
        initializeComponents();
        setupLayout();
        setupMenuBar();
        setupEventHandlers();
        configureWindow();
        startClock();
    }

    private void initializeComponents() {
        // Set look and feel for modern appearance
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Customize UIManager for consistent styling
            UIManager.put("TabbedPane.selected", PRIMARY_COLOR);
            UIManager.put("TabbedPane.selectedForeground", Color.WHITE);
            UIManager.put("TabbedPane.contentBorderInsets", new Insets(0, 0, 0, 0));
            UIManager.put("TabbedPane.tabsOverlapBorder", true);
        } catch (Exception e) {
            // Use default look and feel
        }
        
        // Create main components
        menuBar = new JMenuBar();
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(CARD_BACKGROUND);
        tabbedPane.setForeground(TEXT_COLOR);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        // Status bar components
        statusBar = new JLabel("Ready");
        statusBar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusBar.setForeground(LIGHT_TEXT_COLOR);
        
        dateTimeLabel = new JLabel();
        dateTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateTimeLabel.setForeground(LIGHT_TEXT_COLOR);
        
        // Clock timer
        clockTimer = new Timer(1000, e -> updateDateTime());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Add components
        add(tabbedPane, BorderLayout.CENTER);
        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBackground(CARD_BACKGROUND);
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        // User info label
        JLabel userInfoLabel = new JLabel(String.format("Logged in as: %s (%s)", 
            currentUser.getUsername(), currentUser.getRole()));
        userInfoLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userInfoLabel.setForeground(TEXT_COLOR);
        
        statusPanel.add(statusBar, BorderLayout.WEST);
        statusPanel.add(dateTimeLabel, BorderLayout.CENTER);
        statusPanel.add(userInfoLabel, BorderLayout.EAST);
        add(statusPanel, BorderLayout.SOUTH);
        
        // Add initial tabs based on user role
        addRoleBasedTabs();
    }

    private void addRoleBasedTabs() {
        logger.info("Adding role-based tabs for role: {}", currentUser.getRole());
        switch (currentUser.getRole()) {
            case ADMIN:
                addAdminTabs();
                break;
            case TEACHER:
                addTeacherTabs();
                break;
            case STUDENT:
                addStudentTabs();
                break;
            case PARENT:
                addParentTabs();
                break;
        }
        logger.info("Role-based tabs added successfully");
    }

    private void addAdminTabs() {
        logger.info("Adding admin tabs");
        // Dashboard tab with modern icon
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("🏠 Dashboard", dashboardPanel);
        
        // Students Management tab with modern icon
        logger.info("Creating students panel...");
        JPanel studentsPanel = createStudentsPanel();
        tabbedPane.addTab("👥 Students", studentsPanel);
        logger.info("Students panel added successfully");
        
        // Teachers Management tab with modern icon
        JPanel teachersPanel = createTeachersPanel();
        tabbedPane.addTab("👨‍🏫 Teachers", teachersPanel);
        
        // Classes Management tab with modern icon
        JPanel classesPanel = createClassesPanel();
        tabbedPane.addTab("📚 Classes", classesPanel);
        
        // Reports tab with modern icon
        JPanel reportsPanel = createReportsPanel();
        tabbedPane.addTab("📊 Reports", reportsPanel);
        
        // Settings tab with modern icon
        JPanel settingsPanel = createSettingsPanel();
        tabbedPane.addTab("⚙️ Settings", settingsPanel);
    }

    private void addTeacherTabs() {
        // Dashboard tab
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("Dashboard", dashboardPanel);
        
        // My Classes tab
        JPanel myClassesPanel = createMyClassesPanel();
        tabbedPane.addTab("My Classes", myClassesPanel);
        
        // Attendance tab
        JPanel attendancePanel = createAttendancePanel();
        tabbedPane.addTab("Attendance", attendancePanel);
        
        // Grades tab
        JPanel gradesPanel = createGradesPanel();
        tabbedPane.addTab("Grades", gradesPanel);
    }

    private void addStudentTabs() {
        // Dashboard tab
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("Dashboard", dashboardPanel);
        
        // My Profile tab
        JPanel profilePanel = createProfilePanel();
        tabbedPane.addTab("My Profile", profilePanel);
        
        // My Grades tab
        JPanel gradesPanel = createStudentGradesPanel();
        tabbedPane.addTab("My Grades", gradesPanel);
        
        // Attendance tab
        JPanel attendancePanel = createStudentAttendancePanel();
        tabbedPane.addTab("Attendance", attendancePanel);
    }

    private void addParentTabs() {
        // Dashboard tab
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("Dashboard", dashboardPanel);
        
        // Child's Profile tab
        JPanel childProfilePanel = createChildProfilePanel();
        tabbedPane.addTab("Child's Profile", childProfilePanel);
        
        // Child's Grades tab
        JPanel childGradesPanel = createChildGradesPanel();
        tabbedPane.addTab("Child's Grades", childGradesPanel);
        
        // Child's Attendance tab
        JPanel childAttendancePanel = createChildAttendancePanel();
        tabbedPane.addTab("Child's Attendance", childAttendancePanel);
    }

    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create subtle gradient background
                GradientPaint gradient = new GradientPaint(0, 0, BACKGROUND_COLOR, 
                                                         getWidth(), getHeight(), CARD_BACKGROUND);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
        
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Welcome message
        JLabel welcomeLabel = new JLabel(String.format("Welcome, %s! 👋", currentUser.getFullName()));
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(TEXT_COLOR);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(welcomeLabel, gbc);
        
        // Role badge
        JLabel roleBadge = new JLabel(currentUser.getRole().toString());
        roleBadge.setFont(new Font("Segoe UI", Font.BOLD, 14));
        roleBadge.setForeground(Color.WHITE);
        roleBadge.setBackground(PRIMARY_COLOR);
        roleBadge.setOpaque(true);
        roleBadge.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_COLOR.darker(), 1),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        roleBadge.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridx = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(roleBadge, gbc);
        
        // Quick stats cards
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        
        panel.add(createStatCard("📊", "Quick Stats", "Access your dashboard features"), gbc);
        
        gbc.gridx = 1;
        panel.add(createStatCard("📈", "Performance", "View recent activity and metrics"), gbc);
        
        gbc.gridx = 2;
        panel.add(createStatCard("🔔", "Notifications", "Check for important updates"), gbc);
        
        return panel;
    }
    
    private JPanel createStatCard(String icon, String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setPreferredSize(new Dimension(200, 150));
        
        // Icon
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(LIGHT_TEXT_COLOR);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(iconLabel, BorderLayout.NORTH);
        card.add(titleLabel, BorderLayout.CENTER);
        card.add(descLabel, BorderLayout.SOUTH);
        
        // Add hover effect
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
                card.setBackground(new Color(250, 250, 250));
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(25, 25, 25, 25)
                ));
                card.setBackground(CARD_BACKGROUND);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        
        return card;
    }

    private JPanel createStudentsPanel() {
        logger.info("createStudentsPanel: Creating StudentManagementPanel...");
        JPanel panel = new JPanel(new BorderLayout());
        StudentManagementPanel studentPanel = new StudentManagementPanel();
        logger.info("createStudentsPanel: StudentManagementPanel created successfully");
        panel.add(studentPanel, BorderLayout.CENTER);
        logger.info("createStudentsPanel: StudentManagementPanel added to panel");
        return panel;
    }

    private JPanel createTeachersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Teacher Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Teacher data table
        String[] columnNames = {"Teacher ID", "Name", "Subject", "Email", "Phone", "Status"};
        Object[][] teacherData = {
            {"TCH-001", "Dr. Sarah Johnson", "Mathematics", "sarah.johnson@school.edu", "(555) 123-4567", "Active"},
            {"TCH-002", "Mr. Michael Chen", "Science", "michael.chen@school.edu", "(555) 234-5678", "Active"},
            {"TCH-003", "Ms. Emily Rodriguez", "English", "emily.rodriguez@school.edu", "(555) 345-6789", "Active"},
            {"TCH-004", "Mr. David Thompson", "History", "david.thompson@school.edu", "(555) 456-7890", "On Leave"},
            {"TCH-005", "Ms. Lisa Park", "Art", "lisa.park@school.edu", "(555) 567-8901", "Active"},
            {"TCH-006", "Mr. Robert Wilson", "Physical Education", "robert.wilson@school.edu", "(555) 678-9012", "Active"}
        };
        
        JTable teacherTable = new JTable(teacherData, columnNames);
        teacherTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        teacherTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        teacherTable.getTableHeader().setBackground(new Color(70, 70, 90));
        teacherTable.getTableHeader().setForeground(Color.WHITE);
        teacherTable.setRowHeight(25);
        teacherTable.setGridColor(new Color(90, 90, 110));
        teacherTable.setShowGrid(true);
        teacherTable.setShowHorizontalLines(true);
        teacherTable.setShowVerticalLines(false);
        teacherTable.setSelectionBackground(new Color(100, 100, 120));
        teacherTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(teacherTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 110), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(60, 60, 80));
        summaryPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel totalLabel = new JLabel("Total Teachers: 6");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(Color.WHITE);
        
        JLabel activeLabel = new JLabel("Active: 5");
        activeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        activeLabel.setForeground(new Color(46, 204, 113));
        
        JLabel inactiveLabel = new JLabel("On Leave: 1");
        inactiveLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        inactiveLabel.setForeground(new Color(241, 196, 15));
        
        summaryPanel.add(totalLabel);
        summaryPanel.add(activeLabel);
        summaryPanel.add(inactiveLabel);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createClassesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Class Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Class data table
        String[] columnNames = {"Class ID", "Class Name", "Teacher", "Room", "Schedule", "Students"};
        Object[][] classData = {
            {"CLS-001", "Mathematics 10A", "Dr. Sarah Johnson", "Room 201", "Mon-Wed-Fri 9:00-10:00", "28"},
            {"CLS-002", "Science 10B", "Mr. Michael Chen", "Lab 101", "Tue-Thu 10:30-12:00", "25"},
            {"CLS-003", "English 11A", "Ms. Emily Rodriguez", "Room 205", "Mon-Wed-Fri 10:30-11:30", "30"},
            {"CLS-004", "History 9A", "Mr. David Thompson", "Room 203", "Tue-Thu 9:00-10:30", "24"},
            {"CLS-005", "Art 12A", "Ms. Lisa Park", "Art Room", "Mon-Wed 13:00-14:30", "22"},
            {"CLS-006", "Physical Education", "Mr. Robert Wilson", "Gym", "Tue-Thu 14:00-15:00", "32"}
        };
        
        JTable classTable = new JTable(classData, columnNames);
        classTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        classTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        classTable.getTableHeader().setBackground(new Color(70, 70, 90));
        classTable.getTableHeader().setForeground(Color.WHITE);
        classTable.setRowHeight(25);
        classTable.setGridColor(new Color(90, 90, 110));
        classTable.setShowGrid(true);
        classTable.setShowHorizontalLines(true);
        classTable.setShowVerticalLines(false);
        classTable.setSelectionBackground(new Color(100, 100, 120));
        classTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(classTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 110), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(60, 60, 80));
        summaryPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel totalLabel = new JLabel("Total Classes: 6");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(Color.WHITE);
        
        JLabel activeLabel = new JLabel("Active: 6");
        activeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        activeLabel.setForeground(new Color(46, 204, 113));
        
        JLabel studentsLabel = new JLabel("Total Students: 161");
        studentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentsLabel.setForeground(new Color(52, 152, 219));
        
        summaryPanel.add(totalLabel);
        summaryPanel.add(activeLabel);
        summaryPanel.add(studentsLabel);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Reports & Analytics");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Reports panel with cards
        JPanel reportsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        reportsPanel.setBackground(new Color(240, 240, 245));
        
        // Create report cards
        JPanel attendanceReport = createReportCard("Attendance Report", "95.2%", "Overall attendance rate", "📊");
        JPanel performanceReport = createReportCard("Performance Report", "B+", "Average grade across all subjects", "📈");
        JPanel enrollmentReport = createReportCard("Enrollment Report", "161", "Total students enrolled", "👥");
        JPanel teacherReport = createReportCard("Teacher Report", "6", "Active teaching staff", "👨‍🏫");
        
        reportsPanel.add(attendanceReport);
        reportsPanel.add(performanceReport);
        reportsPanel.add(enrollmentReport);
        reportsPanel.add(teacherReport);
        
        panel.add(reportsPanel, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(240, 240, 245));
        summaryPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel lastUpdatedLabel = new JLabel("Last Updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        lastUpdatedLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lastUpdatedLabel.setForeground(new Color(120, 120, 140));
        
        summaryPanel.add(lastUpdatedLabel);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createReportCard(String title, String value, String description, String emoji) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BorderLayout());
        
        // Emoji icon
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(52, 152, 219));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(120, 120, 140));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel for text content
        JPanel textPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);
        textPanel.add(descLabel);
        
        card.add(emojiLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("System Settings");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Settings categories panel
        JPanel settingsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        settingsPanel.setBackground(new Color(240, 240, 245));
        
        // Create settings cards
        JPanel userSettings = createSettingsCard("User Management", "Manage users, roles, and permissions", "👤");
        JPanel systemSettings = createSettingsCard("System Configuration", "Configure system parameters", "⚙️");
        JPanel backupSettings = createSettingsCard("Backup & Restore", "Manage data backup and recovery", "💾");
        JPanel securitySettings = createSettingsCard("Security Settings", "Security policies and access control", "🔒");
        
        settingsPanel.add(userSettings);
        settingsPanel.add(systemSettings);
        settingsPanel.add(backupSettings);
        settingsPanel.add(securitySettings);
        
        panel.add(settingsPanel, BorderLayout.CENTER);
        
        // Status panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statusPanel.setBackground(new Color(240, 240, 245));
        statusPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel versionLabel = new JLabel("Version: 1.0.0");
        versionLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        versionLabel.setForeground(new Color(120, 120, 140));
        
        JLabel statusLabel = new JLabel("Status: Active");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(new Color(46, 204, 113));
        
        statusPanel.add(versionLabel);
        statusPanel.add(statusLabel);
        
        panel.add(statusPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createSettingsCard(String title, String description, String emoji) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        card.setLayout(new BorderLayout());
        
        // Emoji icon
        JLabel emojiLabel = new JLabel(emoji);
        emojiLabel.setFont(new Font("Segoe UI", Font.PLAIN, 32));
        emojiLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Description
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLabel.setForeground(new Color(120, 120, 140));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Panel for text content
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        
        card.add(emojiLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        
        return card;
    }

    private JPanel createMyClassesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Assigned Classes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Teacher's classes table
        String[] columnNames = {"Class ID", "Class Name", "Schedule", "Room", "Students", "Status"};
        Object[][] classData = {
            {"CLS-001", "Mathematics 10A", "Mon-Wed-Fri 9:00-10:00", "Room 201", "28", "Active"},
            {"CLS-007", "Mathematics 11B", "Tue-Thu 13:30-15:00", "Room 202", "25", "Active"},
            {"CLS-012", "Advanced Mathematics", "Mon-Wed 15:30-17:00", "Room 203", "18", "Active"},
            {"CLS-015", "Calculus 12A", "Tue-Thu 10:30-12:00", "Room 204", "22", "Active"}
        };
        
        JTable classTable = new JTable(classData, columnNames);
        classTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        classTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        classTable.getTableHeader().setBackground(new Color(70, 70, 90));
        classTable.getTableHeader().setForeground(Color.WHITE);
        classTable.setRowHeight(25);
        classTable.setGridColor(new Color(90, 90, 110));
        classTable.setShowGrid(true);
        classTable.setShowHorizontalLines(true);
        classTable.setShowVerticalLines(false);
        classTable.setSelectionBackground(new Color(100, 100, 120));
        classTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(classTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 110), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(60, 60, 80));
        summaryPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel totalLabel = new JLabel("Total Classes: 4");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(Color.WHITE);
        
        JLabel activeLabel = new JLabel("Active: 4");
        activeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        activeLabel.setForeground(new Color(46, 204, 113));
        
        JLabel studentsLabel = new JLabel("Total Students: 93");
        studentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentsLabel.setForeground(new Color(52, 152, 219));
        
        summaryPanel.add(totalLabel);
        summaryPanel.add(activeLabel);
        summaryPanel.add(studentsLabel);
        
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Attendance Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Attendance table
        String[] columnNames = {"Student ID", "Student Name", "Class", "Date", "Status", "Notes"};
        Object[][] attendanceData = {
            {"STU-001", "John Smith", "Mathematics 10A", "2024-01-15", "Present", ""},
            {"STU-002", "Sarah Johnson", "Mathematics 10A", "2024-01-15", "Present", ""},
            {"STU-003", "Mike Brown", "Mathematics 10A", "2024-01-15", "Absent", "Sick leave"},
            {"STU-004", "Emma Davis", "Mathematics 10A", "2024-01-15", "Present", ""},
            {"STU-005", "Alex Wilson", "Mathematics 10A", "2024-01-15", "Late", "10 minutes late"},
            {"STU-006", "Lisa Garcia", "Mathematics 10A", "2024-01-15", "Present", ""},
            {"STU-007", "Tom Miller", "Mathematics 10A", "2024-01-15", "Present", ""},
            {"STU-008", "Amy Rodriguez", "Mathematics 10A", "2024-01-15", "Absent", "Family emergency"}
        };
        
        JTable attendanceTable = new JTable(attendanceData, columnNames);
        attendanceTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        attendanceTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        attendanceTable.getTableHeader().setBackground(new Color(70, 70, 90));
        attendanceTable.getTableHeader().setForeground(Color.WHITE);
        attendanceTable.setRowHeight(25);
        attendanceTable.setGridColor(new Color(90, 90, 110));
        attendanceTable.setShowGrid(true);
        attendanceTable.setShowHorizontalLines(true);
        attendanceTable.setShowVerticalLines(false);
        attendanceTable.setSelectionBackground(new Color(100, 100, 120));
        attendanceTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 110), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(new Color(60, 60, 80));
        controlPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JLabel dateLabel = new JLabel("Date: 2024-01-15");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        dateLabel.setForeground(Color.WHITE);
        
        JButton markPresentButton = new JButton("Mark Present");
        markPresentButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        markPresentButton.setBackground(new Color(46, 204, 113));
        markPresentButton.setForeground(Color.WHITE);
        
        JButton markAbsentButton = new JButton("Mark Absent");
        markAbsentButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        markAbsentButton.setBackground(new Color(231, 76, 60));
        markAbsentButton.setForeground(Color.WHITE);
        
        JButton markLateButton = new JButton("Mark Late");
        markLateButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        markLateButton.setBackground(new Color(241, 196, 15));
        markLateButton.setForeground(Color.WHITE);
        
        controlPanel.add(dateLabel);
        controlPanel.add(markPresentButton);
        controlPanel.add(markAbsentButton);
        controlPanel.add(markLateButton);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Grade Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Grades table
        String[] columnNames = {"Student ID", "Student Name", "Class", "Assignment", "Grade", "Points", "Max Points", "Percentage"};
        Object[][] gradesData = {
            {"STU-001", "John Smith", "Mathematics 10A", "Algebra Quiz", "A", "95", "100", "95%"},
            {"STU-002", "Sarah Johnson", "Mathematics 10A", "Algebra Quiz", "A-", "92", "100", "92%"},
            {"STU-003", "Mike Brown", "Mathematics 10A", "Algebra Quiz", "B+", "87", "100", "87%"},
            {"STU-004", "Emma Davis", "Mathematics 10A", "Algebra Quiz", "A", "96", "100", "96%"},
            {"STU-005", "Alex Wilson", "Mathematics 10A", "Algebra Quiz", "B", "84", "100", "84%"},
            {"STU-006", "Lisa Garcia", "Mathematics 10A", "Algebra Quiz", "A-", "93", "100", "93%"},
            {"STU-007", "Tom Miller", "Mathematics 10A", "Algebra Quiz", "B+", "88", "100", "88%"},
            {"STU-008", "Amy Rodriguez", "Mathematics 10A", "Algebra Quiz", "A", "97", "100", "97%"}
        };
        
        JTable gradesTable = new JTable(gradesData, columnNames);
        gradesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gradesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        gradesTable.getTableHeader().setBackground(new Color(70, 70, 90));
        gradesTable.getTableHeader().setForeground(Color.WHITE);
        gradesTable.setRowHeight(25);
        gradesTable.setGridColor(new Color(90, 90, 110));
        gradesTable.setShowGrid(true);
        gradesTable.setShowHorizontalLines(true);
        gradesTable.setShowVerticalLines(false);
        gradesTable.setSelectionBackground(new Color(100, 100, 120));
        gradesTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 110), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        controlPanel.setBackground(new Color(60, 60, 80));
        controlPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        JButton addGradeButton = new JButton("Add Grade");
        addGradeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addGradeButton.setBackground(new Color(52, 152, 219));
        addGradeButton.setForeground(Color.WHITE);
        
        JButton editGradeButton = new JButton("Edit Grade");
        editGradeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        editGradeButton.setBackground(new Color(155, 89, 182));
        editGradeButton.setForeground(Color.WHITE);
        
        JButton exportButton = new JButton("Export Grades");
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        exportButton.setBackground(new Color(46, 204, 113));
        exportButton.setForeground(Color.WHITE);
        
        controlPanel.add(addGradeButton);
        controlPanel.add(editGradeButton);
        controlPanel.add(exportButton);
        
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Profile Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Profile information panel
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(new Color(240, 240, 245));
        profilePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Sample profile data
        String[][] profileData = {
            {"Username:", currentUser.getUsername()},
            {"Full Name:", currentUser.getFirstName() + " " + currentUser.getLastName()},
            {"Role:", currentUser.getRole().toString()},
            {"Email:", currentUser.getEmail() != null ? currentUser.getEmail() : "Not provided"},
            {"Phone:", "(555) 123-4567"}, // Placeholder phone since getPhone() method doesn't exist
            {"Status:", "Active"},
            {"Last Login:", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))},
            {"Member Since:", "2024-01-15"}
        };
        
        for (int i = 0; i < profileData.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(profileData[i][0]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setForeground(new Color(80, 80, 100));
            profilePanel.add(label, gbc);
            
            gbc.gridx = 1;
            JLabel value = new JLabel(profileData[i][1]);
            value.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            value.setForeground(new Color(60, 60, 80));
            profilePanel.add(value, gbc);
        }
        
        panel.add(profilePanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createStudentGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Academic Performance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Grades table
        String[] columnNames = {"Subject", "Assignment", "Grade", "Points", "Max Points", "Percentage"};
        Object[][] data = {
            {"Mathematics", "Algebra Quiz", "A", "95", "100", "95%"},
            {"Mathematics", "Geometry Test", "B+", "87", "100", "87%"},
            {"Science", "Physics Lab", "A-", "92", "100", "92%"},
            {"Science", "Chemistry Exam", "B", "85", "100", "85%"},
            {"English", "Essay Writing", "A", "96", "100", "96%"},
            {"English", "Literature Test", "A-", "90", "100", "90%"},
            {"History", "Research Paper", "B+", "88", "100", "88%"},
            {"History", "Final Exam", "A-", "91", "100", "91%"},
            {"Geography", "Map Skills Test", "B", "84", "100", "84%"},
            {"Art", "Portfolio Review", "A", "98", "100", "98%"}
        };
        
        JTable gradesTable = new JTable(data, columnNames);
        gradesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gradesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        gradesTable.getTableHeader().setBackground(new Color(70, 70, 90));
        gradesTable.getTableHeader().setForeground(Color.WHITE);
        gradesTable.setRowHeight(25);
        gradesTable.setGridColor(new Color(200, 200, 220));
        gradesTable.setShowGrid(true);
        gradesTable.setShowHorizontalLines(true);
        gradesTable.setShowVerticalLines(false);
        gradesTable.setSelectionBackground(new Color(100, 100, 120));
        gradesTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(245, 245, 250));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel gpaLabel = new JLabel("Current GPA: 3.7");
        gpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gpaLabel.setForeground(new Color(60, 60, 80));
        
        JLabel averageLabel = new JLabel("Average Grade: A-");
        averageLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        averageLabel.setForeground(new Color(60, 60, 80));
        
        summaryPanel.add(gpaLabel);
        summaryPanel.add(averageLabel);
        
        // Add components to panel
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createStudentAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("My Attendance Record");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(173, 216, 230));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Attendance summary
        JPanel summaryPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        summaryPanel.setBackground(new Color(60, 60, 80));
        
        // Create summary cards with sample data
        JPanel totalDaysCard = createSummaryCard("Total Days", "180", "Days");
        JPanel presentDaysCard = createSummaryCard("Present Days", "165", "Days");
        JPanel absentDaysCard = createSummaryCard("Absent Days", "15", "Days");
        JPanel percentageCard = createSummaryCard("Attendance %", "91.7", "%");
        
        summaryPanel.add(totalDaysCard);
        summaryPanel.add(presentDaysCard);
        summaryPanel.add(absentDaysCard);
        summaryPanel.add(percentageCard);
        
        panel.add(summaryPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createSummaryCard(String title, String value, String unit) {
        JPanel card = new JPanel();
        card.setBackground(new Color(70, 70, 90));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 110), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setLayout(new BorderLayout());
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        titleLabel.setForeground(new Color(200, 200, 220));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Value
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Unit
        JLabel unitLabel = new JLabel(unit);
        unitLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        unitLabel.setForeground(new Color(180, 180, 200));
        unitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(unitLabel, BorderLayout.SOUTH);
        
        return card;
    }

    private JPanel createChildProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 240, 245));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Child's Profile Information");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Profile information panel
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(new Color(240, 240, 245));
        profilePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Sample student data (in a real app, this would come from the database)
        String[][] studentData = {
            {"Student Name:", "John Smith"},
            {"Student ID:", "STU-2024-001"},
            {"Grade Level:", "10th Grade"},
            {"Date of Birth:", "2008-03-15"},
            {"Gender:", "Male"},
            {"Email:", "john.smith@school.edu"},
            {"Phone:", "(555) 123-4567"},
            {"Address:", "123 Main St, Springfield, IL 62701"},
            {"Parent Contact:", "Mary Smith (555) 987-6543"},
            {"Enrollment Date:", "2021-08-15"},
            {"Status:", "Active"}
        };
        
        for (int i = 0; i < studentData.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            JLabel label = new JLabel(studentData[i][0]);
            label.setFont(new Font("Segoe UI", Font.BOLD, 14));
            label.setForeground(new Color(80, 80, 100));
            profilePanel.add(label, gbc);
            
            gbc.gridx = 1;
            JLabel value = new JLabel(studentData[i][1]);
            value.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            value.setForeground(new Color(60, 60, 80));
            profilePanel.add(value, gbc);
        }
        
        panel.add(profilePanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createChildGradesPanel() {
         JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 250));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Child's Academic Performance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Grades table
        String[] columnNames = {"Subject", "Assignment", "Grade", "Points", "Max Points", "Percentage"};
        Object[][] data = {
            {"Mathematics", "Algebra Quiz", "A", "95", "100", "95%"},
            {"Mathematics", "Geometry Test", "B+", "87", "100", "87%"},
            {"Science", "Physics Lab", "A-", "92", "100", "92%"},
            {"Science", "Chemistry Exam", "B", "85", "100", "85%"},
            {"English", "Essay Writing", "A", "96", "100", "96%"},
            {"English", "Literature Test", "A-", "90", "100", "90%"},
            {"History", "Research Paper", "B+", "88", "100", "88%"},
            {"History", "Final Exam", "A-", "91", "100", "91%"},
            {"Geography", "Map Skills Test", "B", "84", "100", "84%"},
            {"Art", "Portfolio Review", "A", "98", "100", "98%"}
        };
        
        JTable gradesTable = new JTable(data, columnNames);
        gradesTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        gradesTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        gradesTable.getTableHeader().setBackground(new Color(70, 70, 90));
        gradesTable.getTableHeader().setForeground(Color.WHITE);
        gradesTable.setRowHeight(25);
        gradesTable.setGridColor(new Color(200, 200, 220));
        gradesTable.setShowGrid(true);
        gradesTable.setShowHorizontalLines(true);
        gradesTable.setShowVerticalLines(false);
        gradesTable.setSelectionBackground(new Color(100, 100, 120));
        gradesTable.setSelectionForeground(Color.WHITE);
        
        JScrollPane scrollPane = new JScrollPane(gradesTable);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(new Color(245, 245, 250));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JLabel gpaLabel = new JLabel("Current GPA: 3.7");
        gpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gpaLabel.setForeground(new Color(60, 60, 80));
        
        JLabel avgGradeLabel = new JLabel("Average Grade: A-");
        avgGradeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        avgGradeLabel.setForeground(new Color(60, 60, 80));
        
        summaryPanel.add(gpaLabel);
        summaryPanel.add(avgGradeLabel);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(summaryPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createChildAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Child's Attendance Overview");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 80));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(240, 248, 255));
        
        // Monthly attendance chart (simplified as a table for now)
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel chartTitle = new JLabel("Monthly Attendance Summary");
        chartTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        chartTitle.setForeground(new Color(60, 60, 80));
        chartTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        chartPanel.add(chartTitle, BorderLayout.NORTH);
        
        // Monthly data table
        String[] monthColumns = {"Month", "School Days", "Present", "Absent", "Late", "Attendance %"};
        Object[][] monthData = {
            {"September", "20", "18", "2", "0", "90.0%"},
            {"October", "22", "20", "1", "1", "90.9%"},
            {"November", "18", "17", "1", "0", "94.4%"},
            {"December", "15", "14", "1", "0", "93.3%"},
            {"January", "21", "19", "2", "0", "90.5%"},
            {"February", "19", "18", "1", "0", "94.7%"}
        };
        
        JTable monthTable = new JTable(monthData, monthColumns);
        monthTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        monthTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        monthTable.getTableHeader().setBackground(new Color(70, 70, 90));
        monthTable.getTableHeader().setForeground(Color.WHITE);
        monthTable.setRowHeight(25);
        monthTable.setGridColor(new Color(220, 220, 240));
        monthTable.setShowGrid(true);
        monthTable.setShowHorizontalLines(true);
        monthTable.setShowVerticalLines(false);
        
        JScrollPane monthScrollPane = new JScrollPane(monthTable);
        monthScrollPane.setPreferredSize(new Dimension(600, 200));
        chartPanel.add(monthScrollPane, BorderLayout.CENTER);
        
        // Summary statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        statsPanel.setBackground(new Color(240, 248, 255));
        statsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        statsPanel.add(createAttendanceStatCard("Total Days", "115", "days"));
        statsPanel.add(createAttendanceStatCard("Present", "106", "days"));
        statsPanel.add(createAttendanceStatCard("Absent", "8", "days"));
        statsPanel.add(createAttendanceStatCard("Overall %", "92.2%", ""));
        
        contentPanel.add(chartPanel, BorderLayout.CENTER);
        contentPanel.add(statsPanel, BorderLayout.SOUTH);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAttendanceStatCard(String label, String value, String unit) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(100, 100, 120));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setForeground(new Color(60, 60, 80));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel unitLabel = new JLabel(unit);
        unitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        unitLabel.setForeground(new Color(120, 120, 140));
        unitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(unitLabel, BorderLayout.SOUTH);
        
        return card;
    }

    private void setupMenuBar() {
        // File menu
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        
        fileMenu.addSeparator();
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }

    private void setupEventHandlers() {
        // Window close handler
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                logout();
            }
        });
    }

    private void configureWindow() {
        setTitle("School Management System - " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Set window icon
        setIconImage(createIconImage());
    }
    
    private void startClock() {
        clockTimer.start();
        updateDateTime();
    }
    
    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy • h:mm:ss a");
        dateTimeLabel.setText(now.format(formatter));
    }
    
    private Image createIconImage() {
        try {
            // Create a simple icon using BufferedImage
            int size = 64;
            BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Draw graduation cap
            g2.setColor(PRIMARY_COLOR);
            
            // Cap base
            g2.fillArc(10, 20, size-20, 30, 0, 180);
            
            // Cap body
            g2.fillRect(8, size/2, size-16, 8);
            
            // Tassel
            g2.setColor(ACCENT_COLOR);
            g2.fillOval(size/2-2, size/2+8, 4, 4);
            g2.fillRect(size/2-1, size/2+12, 2, 8);
            
            g2.dispose();
            return image;
        } catch (Exception e) {
            return null;
        }
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            logger.info("User {} logged out", currentUser.getUsername());
            dispose();
            
            // Show login window again
            SwingUtilities.invokeLater(() -> {
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
            });
        }
    }

    private JPanel createPlaceholderPanel(String title, String description) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create subtle gradient background
                GradientPaint gradient = new GradientPaint(0, 0, BACKGROUND_COLOR, 
                                                         getWidth(), getHeight(), CARD_BACKGROUND);
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                g2.dispose();
            }
        };
        
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        
        // Icon
        JLabel iconLabel = new JLabel("🚧");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 64));
        iconLabel.setForeground(PRIMARY_COLOR);
        
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(iconLabel, gbc);
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 1;
        panel.add(titleLabel, gbc);
        
        // Description
        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(LIGHT_TEXT_COLOR);
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);
        descLabel.setPreferredSize(new Dimension(400, 60));
        
        gbc.gridy = 2;
        panel.add(descLabel, gbc);
        
        // Coming soon badge
        JLabel comingSoonLabel = new JLabel("Coming Soon");
        comingSoonLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        comingSoonLabel.setForeground(Color.WHITE);
        comingSoonLabel.setBackground(ACCENT_COLOR);
        comingSoonLabel.setOpaque(true);
        comingSoonLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR.darker(), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        comingSoonLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 3;
        panel.add(comingSoonLabel, gbc);
        
        return panel;
     }
 
     private void showAboutDialog() {
         String aboutText = "<html><h2>" + AppConfig.getAppName() + "</h2>" +
                 "<p>Version: " + AppConfig.getAppVersion() + "</p>" +
                 "<p>A comprehensive school management system built with Java Swing, JDBC, and MySQL.</p>" +
                 "<p>Current User: " + currentUser.getFullName() + " (" + currentUser.getRole() + ")</p></html>";
         
         JOptionPane.showMessageDialog(this, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
     }
 }
