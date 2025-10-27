package com.schoolmanagement.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple Student Dashboard for testing
 */
public class StudentDashboard extends JFrame {
    private MockUser currentUser;
    private JTabbedPane tabbedPane;
    private JButton logoutButton;

    public StudentDashboard(MockUser user) {
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
        logoutButton.setBackground(new Color(255, 140, 0)); // Orange color
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(45, 45, 60)); // Dark blue background
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content area
        add(tabbedPane, BorderLayout.CENTER);
        
        // Add tabs
        addTabs();
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(70, 70, 90)); // Darker blue
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
        
        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.SOUTH);
        
        return headerPanel;
    }

    private void addTabs() {
        // Marks tab
        JPanel marksPanel = createMarksPanel();
        tabbedPane.addTab("Marks", marksPanel);
        
        // Attendance tab
        JPanel attendancePanel = createAttendancePanel();
        tabbedPane.addTab("Attendance", attendancePanel);
        
        // Attendance Predictor tab
        JPanel predictorPanel = createAttendancePredictorPanel();
        tabbedPane.addTab("Attendance Predictor", predictorPanel);
        
        // Class Feedback tab
        JPanel feedbackPanel = createClassFeedbackPanel();
        tabbedPane.addTab("Class Feedback", feedbackPanel);
        
        // Style the tabbed pane
        tabbedPane.setBackground(new Color(60, 60, 80));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private JPanel createMarksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Your Latest Marks");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(173, 216, 230)); // Light blue
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Marks table
        String[] columnNames = {"Subject", "Exam Type", "Marks", "Grade"};
        Object[][] data = {
            {"Mathematics", "Midterm", "85", "A"},
            {"English", "Quiz", "92", "A+"},
            {"Science", "Final", "78", "B+"},
            {"History", "Assignment", "88", "A"},
            {"Computer Science", "Project", "95", "A+"}
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
        JLabel titleLabel = new JLabel("Your Attendance Record");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(173, 216, 230));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Attendance summary
        JPanel summaryPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        summaryPanel.setBackground(new Color(60, 60, 80));
        
        // Create summary cards
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
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(80, 80, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 120), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(200, 200, 200));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel unitLabel = new JLabel(unit);
        unitLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        unitLabel.setForeground(new Color(200, 200, 200));
        unitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(unitLabel, BorderLayout.SOUTH);
        
        return card;
    }

    private JPanel createAttendancePredictorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Attendance Predictor");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(173, 216, 230));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Prediction content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(60, 60, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel predictionLabel = new JLabel("Based on your current attendance pattern:");
        predictionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        predictionLabel.setForeground(Color.WHITE);
        predictionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.insets = new Insets(0, 0, 20, 0);
        contentPanel.add(predictionLabel, gbc);
        
        JLabel resultLabel = new JLabel("You are likely to maintain 90%+ attendance this semester");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 16));
        resultLabel.setForeground(new Color(144, 238, 144)); // Light green
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 0, 0);
        contentPanel.add(resultLabel, gbc);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createClassFeedbackPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Class Feedback");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(173, 216, 230));
        titleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Feedback content
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(60, 60, 80));
        GridBagConstraints gbc = new GridBagConstraints();
        
        JLabel feedbackLabel = new JLabel("<html><div style='text-align: center; width: 400px;'>" +
            "Your teachers have provided the following feedback:<br><br>" +
            "<b>Mathematics:</b> Excellent problem-solving skills<br>" +
            "<b>English:</b> Great improvement in writing<br>" +
            "<b>Science:</b> Very good lab participation<br>" +
            "<b>Overall:</b> Keep up the good work!</div></html>");
        feedbackLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        feedbackLabel.setForeground(Color.WHITE);
        feedbackLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0;
        contentPanel.add(feedbackLabel, gbc);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        
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
        setTitle("Student Dashboard - School Management System");
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