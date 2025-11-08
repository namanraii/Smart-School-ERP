package com.schoolmanagement.ui;

import com.schoolmanagement.config.AppConfig;
import com.schoolmanagement.dao.UserDAO;
import com.schoolmanagement.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Optional;

/**
 * Enhanced Login Window with modern UI design
 */
public class LoginWindow extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(LoginWindow.class);
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton loginButton;
    private JLabel statusLabel;
    private UserDAO userDAO;
    
    // Modern UI Colors
    private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
    private static final Color ACCENT_COLOR = new Color(46, 204, 113);
    private static final Color ERROR_COLOR = new Color(231, 76, 60);
    private static final Color BACKGROUND_COLOR = new Color(248, 249, 250);
    private static final Color CARD_BACKGROUND = Color.WHITE;
    private static final Color TEXT_COLOR = new Color(44, 62, 80);
    private static final Color LIGHT_TEXT_COLOR = new Color(127, 140, 141);
    private static final Color BORDER_COLOR = new Color(221, 221, 221);

    public LoginWindow() {
        this.userDAO = new UserDAO();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        configureWindow();
    }

    private void initializeComponents() {
        // Create components with modern styling
        usernameField = createStyledTextField("Enter your username");
        passwordField = createStyledPasswordField("Enter your password");
        
        // Role dropdown
        String[] roles = {"student", "teacher", "admin", "guest"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedItem("student");
        styleComboBox(roleComboBox);
        
        // Login button
        loginButton = new JButton("Login");
        styleLoginButton(loginButton);
        
        // Status label
        statusLabel = new JLabel("Sign in to continue");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(100, 100, 100));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
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

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);
        field.setEchoChar((char) 0);
        field.setText(placeholder);
        field.setForeground(new Color(150, 150, 150));
        
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setEchoChar('•');
                    field.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (field.getPassword().length == 0) {
                    field.setEchoChar((char) 0);
                    field.setText(placeholder);
                    field.setForeground(new Color(150, 150, 150));
                }
            }
        });
        
        return field;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setForeground(Color.BLACK);
    }

    private void styleLoginButton(JButton button) {
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(200, 50));
        
        // Simple text without emojis for better compatibility
        button.setText("Login");
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(240, 240, 240));
        
        // Main panel with form
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Graduation cap icon (using Unicode)
        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(iconLabel, gbc);
        
        // Title
        JLabel titleLabel = new JLabel("School Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(60, 60, 60));
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 10, 0);
        mainPanel.add(titleLabel, gbc);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to continue");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        subtitleLabel.setForeground(new Color(120, 120, 120));
        gbc.gridy = 2; gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(subtitleLabel, gbc);
        
        // Separator line
        JSeparator separator1 = new JSeparator();
        separator1.setForeground(new Color(200, 200, 200));
        gbc.gridy = 3; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(separator1, gbc);
        
        // Username field
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 4; gbc.insets = new Insets(0, 0, 5, 0);
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        usernameLabel.setForeground(new Color(80, 80, 80));
        mainPanel.add(usernameLabel, gbc);
        
        gbc.gridy = 5; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(usernameField, gbc);
        
        // Password field
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 6; gbc.insets = new Insets(0, 0, 5, 0);
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        passwordLabel.setForeground(new Color(80, 80, 80));
        mainPanel.add(passwordLabel, gbc);
        
        gbc.gridy = 7; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 20, 0);
        mainPanel.add(passwordField, gbc);
        
        // Role selection
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 8; gbc.insets = new Insets(0, 0, 5, 0);
        JLabel roleLabel = new JLabel("Select Role");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        roleLabel.setForeground(new Color(80, 80, 80));
        mainPanel.add(roleLabel, gbc);
        
        gbc.gridy = 9; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(roleComboBox, gbc);
        
        // Login button
        gbc.gridy = 10; gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 30, 0);
        mainPanel.add(loginButton, gbc);
        
        // Separator line
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 11; gbc.insets = new Insets(0, 0, 20, 0);
        JSeparator separator2 = new JSeparator();
        separator2.setForeground(new Color(200, 200, 200));
        mainPanel.add(separator2, gbc);
        
        // Footer links
        gbc.fill = GridBagConstraints.NONE;
        gbc.gridy = 12; gbc.insets = new Insets(0, 0, 20, 0);
        JPanel footerPanel = new JPanel(new FlowLayout());
        footerPanel.setBackground(Color.WHITE);
        
        JLabel forgotPassword = new JLabel("Forgot Password?");
        forgotPassword.setForeground(new Color(70, 130, 180));
        forgotPassword.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        
        JLabel separator = new JLabel("|");
        separator.setForeground(new Color(150, 150, 150));
        
        JLabel guestLogin = new JLabel("Guest Login");
        guestLogin.setForeground(new Color(70, 130, 180));
        guestLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        guestLogin.setFont(new Font("Arial", Font.PLAIN, 12));
        
        footerPanel.add(forgotPassword);
        footerPanel.add(separator);
        footerPanel.add(guestLogin);
        
        mainPanel.add(footerPanel, gbc);
        
        // Copyright
        gbc.gridy = 13; gbc.insets = new Insets(0, 0, 0, 0);
        JLabel copyrightLabel = new JLabel("© 2024 School Management System. All rights reserved.");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        copyrightLabel.setForeground(new Color(150, 150, 150));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(copyrightLabel, gbc);
        
        // Center the main panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints centerGbc = new GridBagConstraints();
        centerPanel.add(mainPanel, centerGbc);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        // Enter key handling
        KeyAdapter enterKeyHandler = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        };
        
        usernameField.addKeyListener(enterKeyHandler);
        passwordField.addKeyListener(enterKeyHandler);
        roleComboBox.addKeyListener(enterKeyHandler);
    }

    private void configureWindow() {
        setTitle("School Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            logger.warn("Could not set system look and feel", e);
        }
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String selectedRole = (String) roleComboBox.getSelectedItem();
        
        // Validation
        if (username.isEmpty() || username.equals("Enter your username")) {
            showStatus("Please enter username", Color.RED);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty() || password.equals("Enter your password")) {
            showStatus("Please enter password", Color.RED);
            passwordField.requestFocus();
            return;
        }
        
        // Show loading status
        showStatus("Authenticating...", Color.BLUE);
        loginButton.setEnabled(false);
        
        // Perform authentication in a separate thread to avoid blocking UI
        new Thread(() -> {
            try {
                Optional<User> userOpt = userDAO.authenticateUser(username, password);
                
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    
                    // Check if user role matches selected role
                    if (!user.getRole().name().toLowerCase().equals(selectedRole)) {
                        SwingUtilities.invokeLater(() -> {
                            showStatus("Invalid role for this user", Color.RED);
                            passwordField.setText("");
                            passwordField.requestFocus();
                        });
                        return;
                    }
                    
                    logger.info("User {} logged in successfully", user.getUsername());
                    
                    // Clear password field for security
                    SwingUtilities.invokeLater(() -> passwordField.setText(""));
                    
                    // Close login window and open main application
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                        openMainApplication(user);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        showStatus("Invalid username or password", Color.RED);
                        passwordField.setText("");
                        passwordField.requestFocus();
                    });
                }
            } catch (Exception e) {
                logger.error("Login error", e);
                SwingUtilities.invokeLater(() -> {
                    showStatus("Login failed. Please try again.", Color.RED);
                });
            } finally {
                SwingUtilities.invokeLater(() -> loginButton.setEnabled(true));
            }
        }).start();
    }

    private void openMainApplication(User user) {
        logger.info("openMainApplication: Creating MainWindow for user {}", user.getUsername());
        try {
            MainWindow mainWindow = new MainWindow(user);
            mainWindow.setVisible(true);
            logger.info("openMainApplication: MainWindow created and set visible");
        } catch (Exception e) {
            logger.error("Error opening main application", e);
            JOptionPane.showMessageDialog(null, "Could not open main application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }

    public static void main(String[] args) {
        // Set system properties for better UI
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Create and show login window
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginWindow().setVisible(true);
            } catch (Exception e) {
                logger.error("Error starting application", e);
                JOptionPane.showMessageDialog(null, 
                    "Error starting application: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
    }
}