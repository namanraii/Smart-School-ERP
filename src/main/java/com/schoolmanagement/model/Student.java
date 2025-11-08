package com.schoolmanagement.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Student model class representing a student in the school management system
 */
public class Student {
    private int studentId;
    private int userId;
    private String studentNumber;
    private LocalDate dateOfBirth;
    private Gender gender;
    private String address;
    private String phoneNumber;
    private String parentContact;
    private LocalDate enrollmentDate;
    private LocalDate graduationDate;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // User information (joined from users table)
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    
    public enum Gender {
        MALE("Male"),
        FEMALE("Female"),
        OTHER("Other");
        
        private final String displayName;
        
        Gender(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    public Student() {
        this.isActive = true;
        this.enrollmentDate = LocalDate.now();
    }
    
    // Constructor with all fields
    public Student(int studentId, int userId, String studentNumber, LocalDate dateOfBirth, 
                   Gender gender, String address, String phoneNumber, String parentContact,
                   LocalDate enrollmentDate, LocalDate graduationDate, boolean isActive,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.studentId = studentId;
        this.userId = userId;
        this.studentNumber = studentNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.parentContact = parentContact;
        this.enrollmentDate = enrollmentDate;
        this.graduationDate = graduationDate;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    
    // Getters and Setters
    public int getStudentId() {
        return studentId;
    }
    
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getStudentNumber() {
        return studentNumber;
    }
    
    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getParentContact() {
        return parentContact;
    }
    
    public void setParentContact(String parentContact) {
        this.parentContact = parentContact;
    }
    
    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }
    
    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
    
    public LocalDate getGraduationDate() {
        return graduationDate;
    }
    
    public void setGraduationDate(LocalDate graduationDate) {
        this.graduationDate = graduationDate;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // User information getters and setters
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    // Utility methods
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return "Unknown Student";
    }
    
    public int getAge() {
        if (dateOfBirth != null) {
            return LocalDate.now().getYear() - dateOfBirth.getYear();
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return getFullName() + " (" + studentNumber + ")";
    }
}