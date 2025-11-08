package com.schoolmanagement;

import com.schoolmanagement.config.DatabaseConfig;
import com.schoolmanagement.dao.UserDAO;
import com.schoolmanagement.model.User;
import com.schoolmanagement.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class to setup and verify database data
 */
public class DatabaseSetup {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSetup.class);
    
    public static void main(String[] args) {
        try {
            logger.info("Starting database setup verification...");
            
            // Test the password hash first
            String expectedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
            String testPassword = "admin123";
            
            logger.info("Testing password verification...");
            logger.info("Expected hash: {}", expectedHash);
            logger.info("Test password: '{}'", testPassword);
            
            boolean isValid = PasswordUtil.verifyPassword(testPassword, expectedHash);
            logger.info("Password verification result: {}", isValid);
            
            // Generate correct hash
            String correctHash = PasswordUtil.hashPassword(testPassword);
            logger.info("Correct hash for 'admin123': {}", correctHash);
            
            // Check if admin user exists
            UserDAO userDAO = new UserDAO();
            var adminUser = userDAO.getUserByUsername("admin");
            
            if (adminUser.isPresent()) {
                logger.info("Admin user found: {}", adminUser.get().getUsername());
                logger.info("Admin role: {}", adminUser.get().getRole());
                logger.info("Admin password hash: {}", adminUser.get().getPasswordHash());
                
                // Test password verification
                boolean passwordValid = PasswordUtil.verifyPassword(testPassword, adminUser.get().getPasswordHash());
                logger.info("Password 'admin123' valid: {}", passwordValid);
                
                if (!passwordValid) {
                    logger.warn("Password verification failed! Updating admin password...");
                    boolean updated = userDAO.updatePassword(adminUser.get().getUserId(), testPassword);
                    logger.info("Admin password updated: {}", updated);
                }
            } else {
                logger.info("Admin user not found, creating...");
                
                // Create admin user
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@school.com");
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setRole(User.UserRole.ADMIN);
                admin.setActive(true);
                
                boolean created = userDAO.createUser(admin, "admin123");
                logger.info("Admin user created: {}", created);
            }
            
            // Check other users
            checkUser(userDAO, "teacher1", "admin123");
            checkUser(userDAO, "student1", "admin123");
            checkUser(userDAO, "parent1", "admin123");
            
            logger.info("Database setup verification completed!");
            
        } catch (Exception e) {
            logger.error("Error during database setup", e);
        }
    }
    
    private static void checkUser(UserDAO userDAO, String username, String expectedPassword) {
        try {
            var user = userDAO.getUserByUsername(username);
            if (user.isPresent()) {
                boolean passwordValid = PasswordUtil.verifyPassword(expectedPassword, user.get().getPasswordHash());
                logger.info("User {} - Password valid: {}", username, passwordValid);
                
                if (!passwordValid) {
                    logger.warn("Password verification failed for {}! Updating password...", username);
                    boolean updated = userDAO.updatePassword(user.get().getUserId(), expectedPassword);
                    logger.info("User {} password updated: {}", username, updated);
                }
            } else {
                logger.warn("User {} not found", username);
            }
        } catch (Exception e) {
            logger.error("Error checking user {}", username, e);
        }
    }
}