package com.schoolmanagement;

import com.schoolmanagement.config.DatabaseConfig;
import com.schoolmanagement.ui.LoginWindow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Main application class for School Management System
 */
public class SchoolManagementSystem {
    private static final Logger logger = LoggerFactory.getLogger(SchoolManagementSystem.class);

    public static void main(String[] args) {
        // Set system properties for better UI
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        // Initialize logging
        logger.info("Starting School Management System...");
        
        // Test database connection
        boolean dbConnected = DatabaseConfig.testConnection();
        if (!dbConnected) {
            logger.warn("Database connection failed. Running in offline mode.");
            int result = JOptionPane.showConfirmDialog(null, 
                "Database connection failed!\n" +
                "Would you like to continue in offline mode for testing?\n" +
                "(Some features may not work without database)",
                "Database Error", 
                JOptionPane.YES_NO_OPTION, 
                JOptionPane.WARNING_MESSAGE);
            
            if (result != JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
        
        logger.info("Database connection successful");
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warn("Could not set system look and feel", e);
        }
        
        // Create and show login window
        SwingUtilities.invokeLater(() -> {
            try {
                LoginWindow loginWindow = new LoginWindow();
                loginWindow.setVisible(true);
                logger.info("Login window displayed successfully");
            } catch (Exception e) {
                logger.error("Error starting application", e);
                JOptionPane.showMessageDialog(null, 
                    "Error starting application: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        });
        
        // Add shutdown hook to close database connections
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutting down School Management System...");
            DatabaseConfig.closeDataSource();
            logger.info("Application shutdown complete");
        }));
    }
}
