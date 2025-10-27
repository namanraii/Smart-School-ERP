package com.schoolmanagement.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple Teacher Profile Viewer for testing
 */
public class TeacherProfileViewer extends JFrame {
    private MockUser currentUser;
    private JTabbedPane tabbedPane;
    private JButton logoutButton;

    public TeacherProfileViewer(MockUser user) {
        this.currentUser = user;
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureWindow();
    }

    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        logoutButton = new JButton("Logout");
        
        // Style the logout button
        logoutButton.setBackground(new Color(70, 130, 180)); // Blue color
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(45, 45, 60)); // Dark blue background
        
        // Main content area
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add tabs
        addTabs();
    }

    private void addTabs() {
        // Profile tab
        JPanel profilePanel = createProfilePanel();
        tabbedPane.addTab("Profile", profilePanel);
        
        // Marks tab
        JPanel marksPanel = createMarksPanel();
        tabbedPane.addTab("Marks", marksPanel);
        
        // Attendance tab
        JPanel attendancePanel = createAttendancePanel();
        tabbedPane.addTab("Attendance", attendancePanel);
        
        // Style the tabbed pane
        tabbedPane.setBackground(new Color(60, 60, 80));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private JPanel createProfilePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(70, 70, 90));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 120), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Title
        JLabel titleLabel = new JLabel("Profile Viewer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Profile content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(70, 70, 90));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Name
        JLabel nameLabel = createProfileLabel("Name:");
        JLabel nameValue = createProfileValue("Dr. Sarah Johnson");
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(nameValue, gbc);
        
        // Contact
        JLabel contactLabel = createProfileLabel("Contact:");
        JLabel contactValue = createProfileValue("sarah.johnson@school.edu | +1 (555) 123-4567");
        gbc.gridx = 0; gbc.gridy = 1;
        contentPanel.add(contactLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(contactValue, gbc);
        
        // Subject
        JLabel subjectLabel = createProfileLabel("Subject:");
        JLabel subjectValue = createProfileValue("Mathematics");
        gbc.gridx = 0; gbc.gridy = 2;
        contentPanel.add(subjectLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(subjectValue, gbc);
        
        // Performance Score
        JLabel performanceLabel = createProfileLabel("Performance Score:");
        JLabel performanceValue = createProfileValue("4.8/5.0");
        gbc.gridx = 0; gbc.gridy = 3;
        contentPanel.add(performanceLabel, gbc);
        gbc.gridx = 1;
        contentPanel.add(performanceValue, gbc);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Recent Feedback section
        JPanel feedbackPanel = createFeedbackPanel();
        mainPanel.add(feedbackPanel, BorderLayout.SOUTH);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        
        // Logout button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBackground(new Color(60, 60, 80));
        buttonPanel.add(logoutButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JLabel createProfileLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JLabel createProfileValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setForeground(new Color(200, 200, 200));
        return label;
    }

    private JPanel createFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(80, 80, 100));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 120), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel("Recent Feedback");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        JLabel feedbackLabel = new JLabel("<html><div style='width: 400px;'>" +
            "Excellent teaching methods and very approachable. Students have shown significant improvement in problem-solving skills. Highly recommended for advanced mathematics courses.</div></html>");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        feedbackLabel.setForeground(new Color(200, 200, 200));
        panel.add(feedbackLabel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createMarksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Student Marks Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(173, 216, 230));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Marks table
        String[] columnNames = {"Student Name", "Subject", "Exam Type", "Marks", "Grade"};
        Object[][] data = {
            {"John Smith", "Mathematics", "Midterm", "85", "A"},
            {"Jane Doe", "Mathematics", "Quiz", "92", "A+"},
            {"Mike Johnson", "Mathematics", "Final", "78", "B+"},
            {"Sarah Wilson", "Mathematics", "Assignment", "88", "A"},
            {"Tom Brown", "Mathematics", "Project", "95", "A+"}
        };
        
        JTable marksTable = new JTable(data, columnNames);
        marksTable.setBackground(new Color(80, 80, 100));
        marksTable.setForeground(Color.WHITE);
        marksTable.setFont(new Font("Arial", Font.PLAIN, 14));
        marksTable.setRowHeight(30);
        marksTable.setGridColor(new Color(100, 100, 120));
        marksTable.setSelectionBackground(new Color(100, 100, 120));
        marksTable.setSelectionForeground(Color.WHITE);
        
        // Style table header
        marksTable.getTableHeader().setBackground(new Color(70, 70, 90));
        marksTable.getTableHeader().setForeground(Color.WHITE);
        marksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(marksTable);
        scrollPane.setBackground(new Color(60, 60, 80));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createAttendancePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Student Attendance Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(173, 216, 230));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Attendance table
        String[] columnNames = {"Student Name", "Total Days", "Present", "Absent", "Percentage"};
        Object[][] data = {
            {"John Smith", "180", "165", "15", "91.7%"},
            {"Jane Doe", "180", "175", "5", "97.2%"},
            {"Mike Johnson", "180", "160", "20", "88.9%"},
            {"Sarah Wilson", "180", "170", "10", "94.4%"},
            {"Tom Brown", "180", "178", "2", "98.9%"}
        };
        
        JTable attendanceTable = new JTable(data, columnNames);
        attendanceTable.setBackground(new Color(80, 80, 100));
        attendanceTable.setForeground(Color.WHITE);
        attendanceTable.setFont(new Font("Arial", Font.PLAIN, 14));
        attendanceTable.setRowHeight(30);
        attendanceTable.setGridColor(new Color(100, 100, 120));
        attendanceTable.setSelectionBackground(new Color(100, 100, 120));
        attendanceTable.setSelectionForeground(Color.WHITE);
        
        // Style table header
        attendanceTable.getTableHeader().setBackground(new Color(70, 70, 90));
        attendanceTable.getTableHeader().setForeground(Color.WHITE);
        attendanceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        
        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBackground(new Color(60, 60, 80));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private void setupEventHandlers() {
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }

    private void configureWindow() {
        setTitle("Teacher Profile - School Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.out.println("Could not set system look and feel: " + e.getMessage());
        }
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", 
            "Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            System.out.println("User " + currentUser.getUsername() + " logged out");
            dispose();
            
            // Show login window again
            SwingUtilities.invokeLater(() -> {
                SimpleLoginWindow loginWindow = new SimpleLoginWindow();
                loginWindow.setVisible(true);
            });
        }
    }
}