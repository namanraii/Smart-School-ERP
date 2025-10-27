package com.schoolmanagement.dao;

import com.schoolmanagement.config.DatabaseConfig;
import com.schoolmanagement.model.User;
import com.schoolmanagement.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Data Access Object for User operations
 */
public class UserDAO {
    private static final Logger logger = LoggerFactory.getLogger(UserDAO.class);

    /**
     * Authenticate user with username and password
     * @param username the username
     * @param password the plain text password
     * @return Optional containing User if authentication successful
     */
    public Optional<User> authenticateUser(String username, String password) {
        String sql = "SELECT user_id, username, password_hash, email, first_name, last_name, role, is_active " +
                    "FROM users WHERE username = ? AND is_active = true";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    
                    if (PasswordUtil.verifyPassword(password, storedHash)) {
                        User user = new User();
                        user.setUserId(rs.getInt("user_id"));
                        user.setUsername(rs.getString("username"));
                        user.setPasswordHash(rs.getString("password_hash"));
                        user.setEmail(rs.getString("email"));
                        user.setFirstName(rs.getString("first_name"));
                        user.setLastName(rs.getString("last_name"));
                        user.setRole(User.UserRole.valueOf(rs.getString("role")));
                        user.setActive(rs.getBoolean("is_active"));
                        
                        logger.info("User {} authenticated successfully", username);
                        return Optional.of(user);
                    } else {
                        logger.warn("Invalid password for user: {}", username);
                    }
                } else {
                    logger.warn("User not found: {}", username);
                }
            }
        } catch (SQLException e) {
            logger.error("Error authenticating user: {}", username, e);
        }
        
        return Optional.empty();
    }

    /**
     * Get user by username
     * @param username the username
     * @return Optional containing User if found
     */
    public Optional<User> getUserByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, email, first_name, last_name, role, is_active " +
                    "FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPasswordHash(rs.getString("password_hash"));
                    user.setEmail(rs.getString("email"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setRole(User.UserRole.valueOf(rs.getString("role")));
                    user.setActive(rs.getBoolean("is_active"));
                    
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            logger.error("Error getting user by username: {}", username, e);
        }
        
        return Optional.empty();
    }

    /**
     * Create a new user
     * @param user the user to create
     * @param password the plain text password
     * @return true if user created successfully
     */
    public boolean createUser(User user, String password) {
        String sql = "INSERT INTO users (username, password_hash, email, first_name, last_name, role) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, PasswordUtil.hashPassword(password));
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getRole().name());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("User created successfully: {}", user.getUsername());
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error creating user: {}", user.getUsername(), e);
        }
        
        return false;
    }

    /**
     * Update user password
     * @param userId the user ID
     * @param newPassword the new plain text password
     * @return true if password updated successfully
     */
    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password_hash = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, PasswordUtil.hashPassword(newPassword));
            stmt.setInt(2, userId);
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                logger.info("Password updated for user ID: {}", userId);
                return true;
            }
        } catch (SQLException e) {
            logger.error("Error updating password for user ID: {}", userId, e);
        }
        
        return false;
    }

    /**
     * Check if username exists
     * @param username the username to check
     * @return true if username exists
     */
    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking username existence: {}", username, e);
        }
        
        return false;
    }

    /**
     * Check if email exists
     * @param email the email to check
     * @return true if email exists
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.error("Error checking email existence: {}", email, e);
        }
        
        return false;
    }
}
