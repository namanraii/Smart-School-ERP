import com.schoolmanagement.config.DatabaseConfig;
import com.schoolmanagement.ui.StudentManagementPanel;

import javax.swing.*;

public class TestStudentPanel {
    public static void main(String[] args) {
        try {
            // Test database connection
            boolean dbConnected = DatabaseConfig.testConnection();
            System.out.println("Database connected: " + dbConnected);
            
            // Create frame
            JFrame frame = new JFrame("Test Student Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            
            // Try to create StudentManagementPanel
            System.out.println("Creating StudentManagementPanel...");
            StudentManagementPanel panel = new StudentManagementPanel();
            System.out.println("StudentManagementPanel created successfully!");
            
            frame.add(panel);
            frame.setVisible(true);
            System.out.println("Frame displayed successfully");
            
        } catch (Exception e) {
            System.err.println("Error creating StudentManagementPanel:");
            e.printStackTrace();
        }
    }
}