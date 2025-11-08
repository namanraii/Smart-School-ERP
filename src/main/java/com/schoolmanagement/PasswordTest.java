package com.schoolmanagement;

import com.schoolmanagement.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test password hashing and verification
 */
public class PasswordTest {
    private static final Logger logger = LoggerFactory.getLogger(PasswordTest.class);
    
    public static void main(String[] args) {
        String password = "admin123";
        String expectedHash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        logger.info("Testing password: '{}'", password);
        logger.info("Expected hash from schema: {}", expectedHash);
        
        // Generate new hash
        String newHash = PasswordUtil.hashPassword(password);
        logger.info("New hash generated: {}", newHash);
        
        // Test verification with expected hash
        boolean verifyExpected = PasswordUtil.verifyPassword(password, expectedHash);
        logger.info("Verification with expected hash: {}", verifyExpected);
        
        // Test verification with new hash
        boolean verifyNew = PasswordUtil.verifyPassword(password, newHash);
        logger.info("Verification with new hash: {}", verifyNew);
        
        // Test with different BCrypt versions
        String altHash = "$2y$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        boolean verifyAlt = PasswordUtil.verifyPassword(password, altHash);
        logger.info("Verification with 2y version: {}", verifyAlt);
        
        // Let's try to understand what's in the database
        logger.info("Let's check what password the hash actually represents...");
        
        // Common test passwords that might match
        String[] testPasswords = {"admin123", "admin", "password", "123456", "admin1234", "admin12"};
        for (String testPass : testPasswords) {
            if (PasswordUtil.verifyPassword(testPass, expectedHash)) {
                logger.info("FOUND IT! The password is: '{}'", testPass);
                break;
            }
        }
    }
}