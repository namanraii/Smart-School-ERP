package com.schoolmanagement.util;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database connection utility class for the School Management System
 */
public class DatabaseConnection {
    // Database credentials
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/school_management_system";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    static {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "MySQL JDBC Driver not found!");
        }
    }

    // Method to establish and return a database connection
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database connection failed. Please check your settings.");
        }
        return connection;
    }
}
