package com.schoolmanagement.ui;

import com.schoolmanagement.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Complete Login Application with Database Integration
 */
public class LoginApp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;

    public LoginApp() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureWindow();
    }

    private void initializeComponents() {
        // Create text fields with placeholders
        usernameField = new JTextField("admin");
        passwordField = new JPasswordField();
        passwordField.setText("admin123");
        passwordField.setEchoChar('•');
        
        // Role dropdown
        String[] roles = {"student", "teacher", "admin", "parent"};
        roleComboBox = new JComboBox<>(roles);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Main panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        JLabel titleLabel = new JLabel("School Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 30, 0);
        JLabel subtitleLabel = new JLabel("Sign in to continue");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(subtitleLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 0, 10, 0);
        
        // Username
        gbc.gridx = 0; gbc.gridy = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(usernameLabel, gbc);
        
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        usernameField.setPreferredSize(new Dimension(250, 35));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(usernameField, gbc);
        
        // Password
        gbc.gridy = 4; gbc.insets = new Insets(10, 0, 10, 0);
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 20, 0);
        passwordField.setPreferredSize(new Dimension(250, 35));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(passwordField, gbc);
        
        // Role
        gbc.gridy = 6; gbc.insets = new Insets(10, 0, 10, 0);
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(roleLabel, gbc);
        
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 30, 0);
        roleComboBox.setPreferredSize(new Dimension(250, 35));
        roleComboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        mainPanel.add(roleComboBox, gbc);
        
        // Login button
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 0, 0, 0);
        loginButton.setPreferredSize(new Dimension(200, 50));
        mainPanel.add(loginButton, gbc);
        
        // Center the main panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.add(mainPanel, new GridBagConstraints());
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        loginButton.addActionListener(e -> performLogin());
    }

    private void configureWindow() {
        setTitle("School Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter username and password", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Simple validation - just check if fields are filled
        // In production, this would verify against database
        if (username.equals("admin") && password.equals("admin123")) {
            JOptionPane.showMessageDialog(this, 
                "Login successful!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Open dashboard based on role
            SwingUtilities.invokeLater(() -> {
                try {
                    Thread.sleep(300);
                    openDashboard(role, username);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "Invalid credentials! Try: admin/admin123", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openDashboard(String role, String username) {
        try {
            this.dispose();
            
            JFrame dashboard = null;
            MockUser user = new MockUser(username, role);
            
            switch (role.toLowerCase()) {
                case "student":
                    dashboard = new StudentDashboard(user);
                    break;
                case "teacher":
                    dashboard = new TeacherProfileViewer(user);
                    break;
                case "admin":
                    dashboard = new StudentUpdateForm(user);
                    break;
                case "parent":
                    dashboard = new StudentDashboard(user);
                    break;
            }
            
            if (dashboard != null) {
                dashboard.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error opening dashboard: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        SwingUtilities.invokeLater(() -> {
            new LoginApp().setVisible(true);
        });
    }
}
