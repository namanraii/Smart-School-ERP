package com.schoolmanagement.ui;

import com.schoolmanagement.dao.StudentDAO;
import com.schoolmanagement.model.Student;
import com.schoolmanagement.model.Student.Gender;
import com.schoolmanagement.config.DatabaseConfig;
import com.schoolmanagement.util.PasswordUtil;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class StudentManagementPanel extends JPanel {
    private JTextField firstNameField, lastNameField, emailField, usernameField;
    private JTextField studentNumberField, phoneField, addressField, parentContactField;
    private JComboBox<String> genderComboBox;
    private JTextField dobField, enrollmentDateField, graduationDateField;
    private JCheckBox activeCheckBox;

    private JButton saveButton, clearButton, refreshButton;
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private JLabel statusLabel;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Colors for modern UI
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color BORDER_COLOR = new Color(221, 221, 221);
    
    public StudentManagementPanel() {
        this.studentDAO = new StudentDAO();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadStudents();
    }
    
    private void initializeComponents() {
        // Set panel properties
        setBackground(BACKGROUND_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Initialize form fields with modern styling
        firstNameField = createStyledTextField("Enter first name");
        lastNameField = createStyledTextField("Enter last name");
        emailField = createStyledTextField("Enter email address");
        usernameField = createStyledTextField("Enter username");
        studentNumberField = createStyledTextField("Enter student number");
        phoneField = createStyledTextField("Enter phone number");
        addressField = createStyledTextField("Enter address");
        parentContactField = createStyledTextField("Enter parent contact");
        
        // Gender combo box
        String[] genders = {"Select Gender", "MALE", "FEMALE", "OTHER"};
        genderComboBox = createStyledComboBox(genders);
        
        // Date fields
        dobField = createStyledTextField("YYYY-MM-DD");
        enrollmentDateField = createStyledTextField("YYYY-MM-DD");
        graduationDateField = createStyledTextField("YYYY-MM-DD");
        
        // Check box
        activeCheckBox = new JCheckBox("Active Student");
        activeCheckBox.setSelected(true);
        activeCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        activeCheckBox.setForeground(SECONDARY_COLOR);
        

        
        // Buttons
        saveButton = createStyledButton("Save Student", ACCENT_COLOR);
        clearButton = createStyledButton("Clear Form", SECONDARY_COLOR);
        refreshButton = createStyledButton("Refresh", PRIMARY_COLOR);
        
        // Status label
        statusLabel = new JLabel("Ready");
        statusLabel.setForeground(SECONDARY_COLOR);
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        
        // Table setup
        setupTable();
        
        // Card layout for switching between form and table
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);
    }
    
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !hasFocus()) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setColor(new Color(100, 100, 100)); // Darker placeholder for better visibility
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    g2.drawString(placeholder, 12, getHeight() / 2 + 5);
                    g2.dispose();
                }
            }
        };
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(CARD_BACKGROUND);
        field.setForeground(SECONDARY_COLOR); // Set text color for better visibility
        field.setCaretColor(SECONDARY_COLOR); // Set cursor color
        field.setPreferredSize(new Dimension(250, 40));
        
        // Add focus listener for better UX
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_COLOR, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return field;
    }
    
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboBox.setBackground(CARD_BACKGROUND);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setPreferredSize(new Dimension(250, 40));
        
        // Custom renderer for better appearance
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                        boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                return this;
            }
        });
        
        return comboBox;
    }
    

    
    private JTextArea createStyledTextArea() {
        JTextArea textArea = new JTextArea(3, 20);
        textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textArea.setBackground(CARD_BACKGROUND);
        
        return textArea;
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(backgroundColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(240, 240, 240));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);
            }
        });
        
        return button;
    }
    
    private void setupTable() {
        String[] columnNames = {"ID", "Student #", "Name", "Email", "Phone", "Gender", "DOB", "Status", "Actions"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only actions column is editable
            }
        };
        
        studentsTable = new JTable(tableModel);
        studentsTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        studentsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        studentsTable.getTableHeader().setBackground(PRIMARY_COLOR);
        studentsTable.getTableHeader().setForeground(Color.WHITE);
        studentsTable.setRowHeight(35);
        studentsTable.setGridColor(BORDER_COLOR);
        studentsTable.setShowGrid(true);
        studentsTable.setShowHorizontalLines(true);
        studentsTable.setShowVerticalLines(false);
        
        // Custom renderer for status column
        studentsTable.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                         boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if ("Active".equals(value)) {
                    setForeground(ACCENT_COLOR);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(ERROR_COLOR);
                    setFont(getFont().deriveFont(Font.BOLD));
                }
                
                return this;
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(20, 20));
        
        // Header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content area
        JPanel formPanel = createFormPanel();
        JScrollPane tableScrollPane = new JScrollPane(studentsTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());
        tableScrollPane.getViewport().setBackground(CARD_BACKGROUND);
        
        mainPanel.add(formPanel, "FORM");
        mainPanel.add(tableScrollPane, "TABLE");
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(BACKGROUND_COLOR);
        statusPanel.add(statusLabel);
        add(statusPanel, BorderLayout.SOUTH);
        
        // Show table by default
        cardLayout.show(mainPanel, "TABLE");
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BACKGROUND);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel titleLabel = new JLabel("Student Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(SECONDARY_COLOR);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Add, edit, and manage student information");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        JPanel titlePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        titlePanel.setBackground(CARD_BACKGROUND);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        // Action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_BACKGROUND);
        
        JButton addNewButton = createStyledButton("Add New Student", ACCENT_COLOR);
        addNewButton.addActionListener(e -> showFormPanel());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(addNewButton);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new BorderLayout(20, 20));
        formPanel.setBackground(CARD_BACKGROUND);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Form title
        JLabel formTitle = new JLabel("Student Information Form");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formTitle.setForeground(SECONDARY_COLOR);
        formTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Form content with scroll pane
        JPanel formContent = new JPanel(new GridBagLayout());
        formContent.setBackground(CARD_BACKGROUND);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Personal Information Section
        addSectionTitle(formContent, "Personal Information", 0, gbc);
        
        addFormField(formContent, "First Name:*", firstNameField, 1, gbc);
        addFormField(formContent, "Last Name:*", lastNameField, 2, gbc);
        addFormField(formContent, "Email:*", emailField, 3, gbc);
        addFormField(formContent, "Username:*", usernameField, 4, gbc);
        
        // Student Details Section
        addSectionTitle(formContent, "Student Details", 5, gbc);
        
        addFormField(formContent, "Student Number:*", studentNumberField, 6, gbc);
        addFormField(formContent, "Gender:*", genderComboBox, 7, gbc);
        addFormField(formContent, "Date of Birth (YYYY-MM-DD):*", dobField, 8, gbc);
        addFormField(formContent, "Phone Number:", phoneField, 9, gbc);
        addFormField(formContent, "Address:", addressField, 10, gbc);
        addFormField(formContent, "Parent Contact:", parentContactField, 11, gbc);
        
        // Academic Information Section
        addSectionTitle(formContent, "Academic Information", 12, gbc);
        
        addFormField(formContent, "Enrollment Date (YYYY-MM-DD):", enrollmentDateField, 13, gbc);
        addFormField(formContent, "Expected Graduation (YYYY-MM-DD):", graduationDateField, 14, gbc);
        
        // Status and Notes
        gbc.gridx = 0; gbc.gridy = 15;
        gbc.gridwidth = 2;
        formContent.add(activeCheckBox, gbc);
        
        // Notes section removed - not in Student model
        
        // Wrap form content in scroll pane
        JScrollPane scrollPane = new JScrollPane(formContent);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(CARD_BACKGROUND);
        scrollPane.setPreferredSize(new Dimension(600, 400)); // Set reasonable size
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(CARD_BACKGROUND);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton backButton = createStyledButton("Back to List", SECONDARY_COLOR);
        backButton.addActionListener(e -> showTablePanel());
        
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);
        
        formPanel.add(formTitle, BorderLayout.NORTH);
        formPanel.add(scrollPane, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return formPanel;
    }
    
    private void addSectionTitle(JPanel panel, String title, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 10, 5);
        
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sectionLabel.setForeground(PRIMARY_COLOR);
        panel.add(sectionLabel, gbc);
        
        gbc.insets = new Insets(5, 5, 5, 5);
    }
    
    private void addFormField(JPanel panel, String label, JComponent field, int row, GridBagConstraints gbc) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fieldLabel.setForeground(SECONDARY_COLOR);
        panel.add(fieldLabel, gbc);
        
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    private void setupEventListeners() {
        saveButton.addActionListener(e -> saveStudent());
        clearButton.addActionListener(e -> clearForm());
        refreshButton.addActionListener(e -> loadStudents());
    }
    
    private void showFormPanel() {
        cardLayout.show(mainPanel, "FORM");
        clearForm();
    }
    
    private void showTablePanel() {
        cardLayout.show(mainPanel, "TABLE");
        loadStudents();
    }
    
    private void saveStudent() {
        if (!validateForm()) {
            return;
        }
        
        try {
            Student student = new Student();
            
            // Set user information
            student.setFirstName(firstNameField.getText().trim());
            student.setLastName(lastNameField.getText().trim());
            student.setEmail(emailField.getText().trim());
            student.setUsername(usernameField.getText().trim());
            
            // Set student information
            student.setStudentNumber(studentNumberField.getText().trim());
            student.setPhoneNumber(phoneField.getText().trim());
            student.setAddress(addressField.getText().trim());
            student.setParentContact(parentContactField.getText().trim());
            
            // Set gender
            String selectedGender = (String) genderComboBox.getSelectedItem();
            if (!"Select Gender".equals(selectedGender)) {
                student.setGender(Student.Gender.valueOf(selectedGender));
            }
            
            // Set dates - parse from text fields
            String dobText = dobField.getText().trim();
            if (!dobText.isEmpty()) {
                try {
                    student.setDateOfBirth(java.time.LocalDate.parse(dobText));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid date format for Date of Birth. Use YYYY-MM-DD format.");
                }
            }
            
            String enrollmentText = enrollmentDateField.getText().trim();
            if (!enrollmentText.isEmpty()) {
                try {
                    student.setEnrollmentDate(java.time.LocalDate.parse(enrollmentText));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid date format for Enrollment Date. Use YYYY-MM-DD format.");
                }
            }
            
            String graduationText = graduationDateField.getText().trim();
            if (!graduationText.isEmpty()) {
                try {
                    student.setGraduationDate(java.time.LocalDate.parse(graduationText));
                } catch (Exception e) {
                    throw new IllegalArgumentException("Invalid date format for Graduation Date. Use YYYY-MM-DD format.");
                }
            }
            
            // Set status
            student.setActive(activeCheckBox.isSelected());
            
            // Create student in database - need to provide username, password, and email
            String password = PasswordUtil.generateRandomPassword(8); // Generate a secure random password
            boolean success = studentDAO.createStudent(student, usernameField.getText().trim(), password, emailField.getText().trim());
            
            if (success) {
                showStatus("Student saved successfully!", ACCENT_COLOR);
                clearForm();
                showTablePanel();
            } else {
                showStatus("Failed to save student. Please try again.", ERROR_COLOR);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Error: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    private boolean validateForm() {
        List<String> errors = new ArrayList<>();
        
        // Required field validation
        if (firstNameField.getText().trim().isEmpty()) {
            errors.add("First name is required");
        }
        
        if (lastNameField.getText().trim().isEmpty()) {
            errors.add("Last name is required");
        }
        
        if (emailField.getText().trim().isEmpty()) {
            errors.add("Email is required");
        } else if (!isValidEmail(emailField.getText().trim())) {
            errors.add("Invalid email format");
        }
        
        if (usernameField.getText().trim().isEmpty()) {
            errors.add("Username is required");
        }
        
        if (studentNumberField.getText().trim().isEmpty()) {
            errors.add("Student number is required");
        }
        
        if ("Select Gender".equals(genderComboBox.getSelectedItem())) {
            errors.add("Gender is required");
        }
        
        if (dobField.getText().trim().isEmpty()) {
            errors.add("Date of birth is required");
        }
        
        // Date validation
        String dobText = dobField.getText().trim();
        String enrollmentText = enrollmentDateField.getText().trim();
        String graduationText = graduationDateField.getText().trim();
        
        if (dobText.isEmpty()) {
            errors.add("Date of birth is required");
        }
        
        // Parse dates for validation
        java.time.LocalDate dob = null;
        java.time.LocalDate enrollment = null;
        java.time.LocalDate graduation = null;
        
        try {
            if (!dobText.isEmpty()) dob = java.time.LocalDate.parse(dobText);
            if (!enrollmentText.isEmpty()) enrollment = java.time.LocalDate.parse(enrollmentText);
            if (!graduationText.isEmpty()) graduation = java.time.LocalDate.parse(graduationText);
        } catch (Exception e) {
            errors.add("Invalid date format. Use YYYY-MM-DD format.");
        }
        
        if (dob != null && enrollment != null && enrollment.isBefore(dob)) {
            errors.add("Enrollment date cannot be before date of birth");
        }
        
        if (graduation != null && enrollment != null && graduation.isBefore(enrollment)) {
            errors.add("Graduation date cannot be before enrollment date");
        }
        
        if (!errors.isEmpty()) {
            String errorMessage = String.join("\n• ", errors);
            JOptionPane.showMessageDialog(this, "Please fix the following errors:\n• " + errorMessage,
                    "Validation Errors", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
    
    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        usernameField.setText("");
        studentNumberField.setText("");
        phoneField.setText("");
        addressField.setText("");
        parentContactField.setText("");
        genderComboBox.setSelectedIndex(0);
        dobField.setText("");
        enrollmentDateField.setText("");
        graduationDateField.setText("");
        activeCheckBox.setSelected(true);
        showStatus("Form cleared", SECONDARY_COLOR);
    }
    
    private void loadStudents() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            updateTable(students);
            showStatus("Loaded " + students.size() + " students", PRIMARY_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
            showStatus("Error loading students: " + e.getMessage(), ERROR_COLOR);
        }
    }
    
    private void updateTable(List<Student> students) {
        tableModel.setRowCount(0); // Clear existing data
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getStudentNumber(),
                student.getFullName(),
                student.getEmail(),
                student.getPhoneNumber(),
                student.getGender() != null ? student.getGender().name() : "",
                student.getDateOfBirth() != null ? student.getDateOfBirth().toString() : "",
                student.isActive() ? "Active" : "Inactive",
                "Edit | Delete"
            };
            tableModel.addRow(row);
        }
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
        
        // Auto-clear status after 5 seconds
        Timer timer = new Timer(5000, e -> {
            statusLabel.setText("Ready");
            statusLabel.setForeground(SECONDARY_COLOR);
        });
        timer.setRepeats(false);
        timer.start();
    }
}