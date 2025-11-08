package com.schoolmanagement.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Simple Student Update Form for testing
 */
public class StudentUpdateForm extends JFrame {
    private MockUser currentUser;
    private JTabbedPane tabbedPane;
    private JButton logoutButton;
    
    // Form fields
    private JTextField registrationNumberField;
    private JTextField nameField;
    private JTextField attendanceField;
    private JTextField phoneNumberField;
    private JTextField addressField;
    private JTextField ageField;
    private JButton saveButton;

    public StudentUpdateForm(MockUser user) {
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
        
        // Initialize form fields
        initializeFormFields();
    }

    private void initializeFormFields() {
        // Registration Number field
        registrationNumberField = createStyledTextField("Enter Registration Number");
        
        // Name field
        nameField = createStyledTextField("Enter Name");
        
        // Attendance field
        attendanceField = createStyledTextField("Enter Attendance %");
        
        // Phone Number field
        phoneNumberField = createStyledTextField("Enter Phone Number");
        
        // Address field
        addressField = createStyledTextField("Enter Address");
        
        // Age field
        ageField = createStyledTextField("Enter Age");
        
        // Save button
        saveButton = new JButton("Save");
        saveButton.setBackground(new Color(34, 139, 34)); // Green color
        saveButton.setForeground(Color.WHITE);
        saveButton.setFont(new Font("Arial", Font.BOLD, 14));
        saveButton.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        saveButton.setFocusPainted(false);
        saveButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setText(placeholder);
        field.setForeground(new Color(150, 150, 150));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        return field;
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
        // Students tab
        JPanel studentsPanel = createStudentsPanel();
        tabbedPane.addTab("Students", studentsPanel);
        
        // Teachers tab
        JPanel teachersPanel = createTeachersPanel();
        tabbedPane.addTab("Teachers", teachersPanel);
        
        // Style the tabbed pane
        tabbedPane.setBackground(new Color(60, 60, 80));
        tabbedPane.setForeground(Color.WHITE);
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 14));
    }

    private JPanel createStudentsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Update Student Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setBorder(new EmptyBorder(0, 0, 30, 0));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Form panel
        JPanel formPanel = createFormPanel();
        panel.add(formPanel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = createButtonPanel();
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(15, 0, 15, 0);
        
        // Instructions
        JLabel instructionLabel = new JLabel("Fill in the student details below:");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        instructionLabel.setForeground(new Color(100, 100, 100));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(instructionLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 0, 15, 0);
        
        // Registration Number
        JLabel regLabel = createFormLabel("Registration Number:");
        gbc.gridx = 0; gbc.gridy = 1; gbc.fill = GridBagConstraints.NONE;
        panel.add(regLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(registrationNumberField, gbc);
        
        // Name
        JLabel nameLabel = createFormLabel("Name:");
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(nameField, gbc);
        
        // Attendance
        JLabel attendanceLabel = createFormLabel("Attendance:");
        gbc.gridx = 0; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE;
        panel.add(attendanceLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(attendanceField, gbc);
        
        // Phone Number
        JLabel phoneLabel = createFormLabel("Phone Number:");
        gbc.gridx = 0; gbc.gridy = 4; gbc.fill = GridBagConstraints.NONE;
        panel.add(phoneLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(phoneNumberField, gbc);
        
        // Address
        JLabel addressLabel = createFormLabel("Address:");
        gbc.gridx = 0; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE;
        panel.add(addressLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(addressField, gbc);
        
        // Age
        JLabel ageLabel = createFormLabel("Age:");
        gbc.gridx = 0; gbc.gridy = 6; gbc.fill = GridBagConstraints.NONE;
        panel.add(ageLabel, gbc);
        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(ageField, gbc);
        
        return panel;
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        label.setPreferredSize(new Dimension(150, 30));
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(new Color(60, 60, 80));
        panel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        panel.add(saveButton);
        panel.add(logoutButton);
        
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

    private void setupEventHandlers() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudentInfo();
            }
        });
        
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
    }

    private void saveStudentInfo() {
        // Validate form fields
        if (isFieldEmpty(registrationNumberField, "Enter Registration Number") ||
            isFieldEmpty(nameField, "Enter Name") ||
            isFieldEmpty(attendanceField, "Enter Attendance %") ||
            isFieldEmpty(phoneNumberField, "Enter Phone Number") ||
            isFieldEmpty(addressField, "Enter Address") ||
            isFieldEmpty(ageField, "Enter Age")) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill in all fields", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validate numeric fields
        try {
            Integer.parseInt(ageField.getText());
            Double.parseDouble(attendanceField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Please enter valid numbers for Age and Attendance", 
                "Validation Error", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Save the information (in a real application, this would save to database)
        JOptionPane.showMessageDialog(this, 
            "Student information saved successfully!", 
            "Success", 
            JOptionPane.INFORMATION_MESSAGE);
        
        System.out.println("Student information saved for user: " + currentUser.getUsername());
    }

    private boolean isFieldEmpty(JTextField field, String placeholder) {
        return field.getText().trim().isEmpty() || field.getText().equals(placeholder);
    }

    private void configureWindow() {
        setTitle("Student Update Form - School Management System");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
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