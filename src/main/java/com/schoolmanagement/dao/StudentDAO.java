package com.schoolmanagement.dao;

import com.schoolmanagement.config.DatabaseConfig;
import com.schoolmanagement.model.Student;
import com.schoolmanagement.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data Access Object for Student operations
 */
public class StudentDAO {
    private static final Logger logger = LoggerFactory.getLogger(StudentDAO.class);
    
    // SQL Queries
    private static final String INSERT_STUDENT = 
        "INSERT INTO students (user_id, student_number, date_of_birth, gender, address, " +
        "phone_number, parent_contact, enrollment_date, graduation_date, is_active) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String INSERT_USER = 
        "INSERT INTO users (username, password_hash, email, first_name, last_name, role, is_active) " +
        "VALUES (?, ?, ?, ?, ?, 'STUDENT', true)";
    
    private static final String SELECT_ALL_STUDENTS = 
        "SELECT s.*, u.username, u.email, u.first_name, u.last_name " +
        "FROM students s JOIN users u ON s.user_id = u.user_id " +
        "WHERE s.is_active = true ORDER BY s.student_id DESC";
    
    private static final String SELECT_STUDENT_BY_ID = 
        "SELECT s.*, u.username, u.email, u.first_name, u.last_name " +
        "FROM students s JOIN users u ON s.user_id = u.user_id " +
        "WHERE s.student_id = ? AND s.is_active = true";
    
    private static final String SELECT_STUDENT_BY_STUDENT_NUMBER = 
        "SELECT s.*, u.username, u.email, u.first_name, u.last_name " +
        "FROM students s JOIN users u ON s.user_id = u.user_id " +
        "WHERE s.student_number = ? AND s.is_active = true";
    
    private static final String UPDATE_STUDENT = 
        "UPDATE students SET student_number = ?, date_of_birth = ?, gender = ?, " +
        "address = ?, phone_number = ?, parent_contact = ?, graduation_date = ? " +
        "WHERE student_id = ?";
    
    private static final String UPDATE_USER_FOR_STUDENT = 
        "UPDATE users SET email = ?, first_name = ?, last_name = ? " +
        "WHERE user_id = (SELECT user_id FROM students WHERE student_id = ?)";
    
    private static final String DELETE_STUDENT = 
        "UPDATE students SET is_active = false WHERE student_id = ?";
    
    private static final String COUNT_STUDENTS = 
        "SELECT COUNT(*) FROM students WHERE is_active = true";
    
    private static final String COUNT_STUDENTS_BY_DATE = 
        "SELECT COUNT(*) FROM students WHERE is_active = true AND enrollment_date >= ?";
    
    /**
     * Create a new student with associated user account
     */
    public boolean createStudent(Student student, String username, String password, String email) {
        Connection conn = null;
        PreparedStatement userStmt = null;
        PreparedStatement studentStmt = null;
        ResultSet generatedKeys = null;
        
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            
            // First, create the user account
            userStmt = conn.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            userStmt.setString(1, username);
            userStmt.setString(2, PasswordUtil.hashPassword(password)); // Hash the password before storing
            userStmt.setString(3, email);
            userStmt.setString(4, student.getFirstName());
            userStmt.setString(5, student.getLastName());
            
            int userRows = userStmt.executeUpdate();
            if (userRows == 0) {
                conn.rollback();
                return false;
            }
            
            // Get the generated user_id
            generatedKeys = userStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                student.setUserId(generatedKeys.getInt(1));
            } else {
                conn.rollback();
                return false;
            }
            
            // Then, create the student record
            studentStmt = conn.prepareStatement(INSERT_STUDENT);
            studentStmt.setInt(1, student.getUserId());
            studentStmt.setString(2, student.getStudentNumber());
            studentStmt.setDate(3, student.getDateOfBirth() != null ? Date.valueOf(student.getDateOfBirth()) : null);
            studentStmt.setString(4, student.getGender() != null ? student.getGender().name() : null);
            studentStmt.setString(5, student.getAddress());
            studentStmt.setString(6, student.getPhoneNumber());
            studentStmt.setString(7, student.getParentContact());
            studentStmt.setDate(8, student.getEnrollmentDate() != null ? Date.valueOf(student.getEnrollmentDate()) : Date.valueOf(LocalDate.now()));
            studentStmt.setDate(9, student.getGraduationDate() != null ? Date.valueOf(student.getGraduationDate()) : null);
            studentStmt.setBoolean(10, student.isActive());
            
            int studentRows = studentStmt.executeUpdate();
            if (studentRows == 0) {
                conn.rollback();
                return false;
            }
            
            conn.commit();
            logger.info("Student created successfully: {}", student.getStudentNumber());
            return true;
            
        } catch (SQLException e) {
            logger.error("Error creating student: {}", e.getMessage(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.error("Error rolling back transaction: {}", ex.getMessage(), ex);
                }
            }
            return false;
        } finally {
            // Close resources
            if (generatedKeys != null) {
                try {
                    generatedKeys.close();
                } catch (SQLException e) {
                    logger.error("Error closing generated keys: {}", e.getMessage(), e);
                }
            }
            if (userStmt != null) {
                try {
                    userStmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing user statement: {}", e.getMessage(), e);
                }
            }
            if (studentStmt != null) {
                try {
                    studentStmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing student statement: {}", e.getMessage(), e);
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection: {}", e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Get all active students
     */
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_STUDENTS);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
            
        } catch (SQLException e) {
            logger.error("Error getting all students: {}", e.getMessage(), e);
        }
        
        return students;
    }
    
    /**
     * Get student by ID
     */
    public Optional<Student> getStudentById(int studentId) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_STUDENT_BY_ID)) {
            
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToStudent(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting student by ID: {}", e.getMessage(), e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Get student by student number
     */
    public Optional<Student> getStudentByStudentNumber(String studentNumber) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_STUDENT_BY_STUDENT_NUMBER)) {
            
            stmt.setString(1, studentNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToStudent(rs));
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting student by student number: {}", e.getMessage(), e);
        }
        
        return Optional.empty();
    }
    
    /**
     * Update student information
     */
    public boolean updateStudent(Student student) {
        Connection conn = null;
        PreparedStatement studentStmt = null;
        PreparedStatement userStmt = null;
        
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false);
            
            // Update student record
            studentStmt = conn.prepareStatement(UPDATE_STUDENT);
            studentStmt.setString(1, student.getStudentNumber());
            studentStmt.setDate(2, student.getDateOfBirth() != null ? Date.valueOf(student.getDateOfBirth()) : null);
            studentStmt.setString(3, student.getGender() != null ? student.getGender().name() : null);
            studentStmt.setString(4, student.getAddress());
            studentStmt.setString(5, student.getPhoneNumber());
            studentStmt.setString(6, student.getParentContact());
            studentStmt.setDate(7, student.getGraduationDate() != null ? Date.valueOf(student.getGraduationDate()) : null);
            studentStmt.setInt(8, student.getStudentId());
            
            int studentRows = studentStmt.executeUpdate();
            
            // Update user information
            userStmt = conn.prepareStatement(UPDATE_USER_FOR_STUDENT);
            userStmt.setString(1, student.getEmail());
            userStmt.setString(2, student.getFirstName());
            userStmt.setString(3, student.getLastName());
            userStmt.setInt(4, student.getStudentId());
            
            int userRows = userStmt.executeUpdate();
            
            if (studentRows > 0 && userRows > 0) {
                conn.commit();
                logger.info("Student updated successfully: {}", student.getStudentNumber());
                return true;
            } else {
                conn.rollback();
                return false;
            }
            
        } catch (SQLException e) {
            logger.error("Error updating student: {}", e.getMessage(), e);
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    logger.error("Error rolling back transaction: {}", ex.getMessage(), ex);
                }
            }
            return false;
        } finally {
            // Close resources
            if (userStmt != null) {
                try {
                    userStmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing user statement: {}", e.getMessage(), e);
                }
            }
            if (studentStmt != null) {
                try {
                    studentStmt.close();
                } catch (SQLException e) {
                    logger.error("Error closing student statement: {}", e.getMessage(), e);
                }
            }
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    logger.error("Error closing connection: {}", e.getMessage(), e);
                }
            }
        }
    }
    
    /**
     * Soft delete a student (set is_active = false)
     */
    public boolean deleteStudent(int studentId) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_STUDENT)) {
            
            stmt.setInt(1, studentId);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                logger.info("Student deleted successfully: {}", studentId);
                return true;
            }
            
        } catch (SQLException e) {
            logger.error("Error deleting student: {}", e.getMessage(), e);
        }
        
        return false;
    }
    
    /**
     * Get total number of active students
     */
    public int getTotalStudents() {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_STUDENTS);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            logger.error("Error getting total students: {}", e.getMessage(), e);
        }
        
        return 0;
    }
    
    /**
     * Get number of students enrolled after a specific date
     */
    public int getStudentsEnrolledAfter(LocalDate date) {
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(COUNT_STUDENTS_BY_DATE)) {
            
            stmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException e) {
            logger.error("Error getting students by enrollment date: {}", e.getMessage(), e);
        }
        
        return 0;
    }
    
    /**
     * Map ResultSet to Student object
     */
    private Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        
        student.setStudentId(rs.getInt("student_id"));
        student.setUserId(rs.getInt("user_id"));
        student.setStudentNumber(rs.getString("student_number"));
        
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) {
            student.setDateOfBirth(dob.toLocalDate());
        }
        
        String genderStr = rs.getString("gender");
        if (genderStr != null) {
            student.setGender(Student.Gender.valueOf(genderStr));
        }
        
        student.setAddress(rs.getString("address"));
        student.setPhoneNumber(rs.getString("phone_number"));
        student.setParentContact(rs.getString("parent_contact"));
        
        Date enrollmentDate = rs.getDate("enrollment_date");
        if (enrollmentDate != null) {
            student.setEnrollmentDate(enrollmentDate.toLocalDate());
        }
        
        Date graduationDate = rs.getDate("graduation_date");
        if (graduationDate != null) {
            student.setGraduationDate(graduationDate.toLocalDate());
        }
        
        student.setActive(rs.getBoolean("is_active"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            student.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            student.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        // Set user information
        student.setUsername(rs.getString("username"));
        student.setEmail(rs.getString("email"));
        student.setFirstName(rs.getString("first_name"));
        student.setLastName(rs.getString("last_name"));
        
        return student;
    }
}