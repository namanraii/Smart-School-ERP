package com.schoolmanagement.test;

import com.schoolmanagement.dao.UserDAO;
import com.schoolmanagement.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test class to verify login functionality for all user roles
 */
public class LoginTest {
    private static final Logger logger = LoggerFactory.getLogger(LoginTest.class);
    
    public static void main(String[] args) {
        logger.info("Starting login functionality tests...");
        
        UserDAO userDAO = new UserDAO();
        
        // Test credentials for different user roles
        String[][] testUsers = {
            {"admin", "admin123", "ADMIN"},
            {"teacher1", "admin123", "TEACHER"},
            {"student1", "admin123", "STUDENT"},
            {"parent1", "admin123", "PARENT"}
        };
        
        boolean allTestsPassed = true;
        
        for (String[] userData : testUsers) {
            String username = userData[0];
            String password = userData[1];
            String expectedRole = userData[2];
            
            logger.info("Testing login for user: {} (expected role: {})", username, expectedRole);
            
            try {
                var userOptional = userDAO.authenticateUser(username, password);
                
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    logger.info("✓ User {} authenticated successfully", username);
                    logger.info("  - Role: {}", user.getRole());
                    logger.info("  - Full Name: {}", user.getFullName());
                    logger.info("  - Email: {}", user.getEmail());
                    
                    if (user.getRole().name().equals(expectedRole)) {
                        logger.info("✓ Role matches expected: {}", expectedRole);
                    } else {
                        logger.error("✗ Role mismatch! Expected: {}, Actual: {}", expectedRole, user.getRole());
                        allTestsPassed = false;
                    }
                } else {
                    logger.error("✗ Authentication failed for user: {}", username);
                    allTestsPassed = false;
                }
            } catch (Exception e) {
                logger.error("✗ Error testing user {}: {}", username, e.getMessage(), e);
                allTestsPassed = false;
            }
            
            logger.info(""); // Empty line for readability
        }
        
        if (allTestsPassed) {
            logger.info("🎉 All login tests PASSED! All user roles are working correctly.");
        } else {
            logger.error("❌ Some login tests FAILED! Please check the issues above.");
        }
        
        logger.info("Login functionality tests completed.");
    }
}