package com.schoolmanagement.ui;

import com.schoolmanagement.config.AppConfig;
import com.schoolmanagement.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main application window for the School Management System
 */
public class MainWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(MainWindow.class);
    
    private User currentUser;
    private JMenuBar menuBar;
    private JTabbedPane tabbedPane;
    private JLabel statusBar;

    public MainWindow(User user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        setupMenuBar();
        setupEventHandlers();
        configureWindow();
    }

    private void initializeComponents() {
        // Create main components
        menuBar = new JMenuBar();
        tabbedPane = new JTabbedPane();
        statusBar = new JLabel("Welcome, " + currentUser.getFullName() + " (" + currentUser.getRole() + ")");
        
        // Configure status bar
        statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Add components
        add(tabbedPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
        
        // Add initial tabs based on user role
        addRoleBasedTabs();
    }

    private void addRoleBasedTabs() {
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
    }

    private void addAdminTabs() {
        // Dashboard tab
        JPanel dashboardPanel = createDashboardPanel();
        tabbedPane.addTab("Dashboard", dashboardPanel);
        
        // Students Management tab
        JPanel studentsPanel = createStudentsPanel();
        tabbedPane.addTab("Students", studentsPanel);
        
        // Teachers Management tab
        JPanel teachersPanel = createTeachersPanel();
        tabbedPane.addTab("Teachers", teachersPanel);
        
        // Classes Management tab
        JPanel classesPanel = createClassesPanel();
        tabbedPane.addTab("Classes", classesPanel);
        
        // Reports tab
        JPanel reportsPanel = createReportsPanel();
        tabbedPane.addTab("Reports", reportsPanel);
        
        // Settings tab
        JPanel settingsPanel = createSettingsPanel();
        tabbedPane.addTab("Settings", settingsPanel);
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
        JPanel panel = new JPanel(new BorderLayout());
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("<html><h2>Welcome to " + AppConfig.getAppName() + "</h2>" +
                "<p>You are logged in as: " + currentUser.getFullName() + " (" + currentUser.getRole() + ")</p>" +
                "<p>Last login: " + java.time.LocalDateTime.now().toString() + "</p></html>");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        panel.add(welcomeLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Students Management - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTeachersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Teachers Management - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createClassesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Classes Management - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Reports - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createSettingsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Settings - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createMyClassesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("My Classes - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Attendance Management - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Grades Management - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("My Profile - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStudentGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("My Grades - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStudentAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("My Attendance - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChildProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Child's Profile - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChildGradesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Child's Grades - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createChildAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Child's Attendance - Coming Soon");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
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
        setTitle(AppConfig.getAppName() + " - " + currentUser.getFullName());
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            logger.warn("Could not set system look and feel", e);
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

    private void showAboutDialog() {
        String aboutText = "<html><h2>" + AppConfig.getAppName() + "</h2>" +
                "<p>Version: " + AppConfig.getAppVersion() + "</p>" +
                "<p>A comprehensive school management system built with Java Swing, JDBC, and MySQL.</p>" +
                "<p>Current User: " + currentUser.getFullName() + " (" + currentUser.getRole() + ")</p></html>";
        
        JOptionPane.showMessageDialog(this, aboutText, "About", JOptionPane.INFORMATION_MESSAGE);
    }
}
