-- School Management System Database Schema
-- This file will be updated as we develop the project

-- Create database
CREATE DATABASE IF NOT EXISTS school_management_system;
USE school_management_system;

-- Users table for authentication
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    role ENUM('ADMIN', 'TEACHER', 'STUDENT', 'PARENT') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Students table
CREATE TABLE IF NOT EXISTS students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    student_number VARCHAR(20) UNIQUE NOT NULL,
    date_of_birth DATE,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    address TEXT,
    phone_number VARCHAR(20),
    parent_contact VARCHAR(20),
    enrollment_date DATE DEFAULT (CURRENT_DATE),
    graduation_date DATE NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Teachers table
CREATE TABLE IF NOT EXISTS teachers (
    teacher_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    employee_number VARCHAR(20) UNIQUE NOT NULL,
    department VARCHAR(50),
    specialization VARCHAR(100),
    hire_date DATE DEFAULT (CURRENT_DATE),
    salary DECIMAL(10,2),
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- Classes/Grades table
CREATE TABLE IF NOT EXISTS classes (
    class_id INT PRIMARY KEY AUTO_INCREMENT,
    class_name VARCHAR(50) NOT NULL,
    grade_level INT NOT NULL,
    academic_year VARCHAR(10) NOT NULL,
    teacher_id INT,
    max_students INT DEFAULT 30,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id)
);

-- Subjects table
CREATE TABLE IF NOT EXISTS subjects (
    subject_id INT PRIMARY KEY AUTO_INCREMENT,
    subject_name VARCHAR(100) NOT NULL,
    subject_code VARCHAR(20) UNIQUE NOT NULL,
    description TEXT,
    credits INT DEFAULT 1,
    is_active BOOLEAN DEFAULT TRUE
);

-- Class-Subject mapping
CREATE TABLE IF NOT EXISTS class_subjects (
    class_subject_id INT PRIMARY KEY AUTO_INCREMENT,
    class_id INT NOT NULL,
    subject_id INT NOT NULL,
    teacher_id INT NOT NULL,
    UNIQUE KEY unique_class_subject (class_id, subject_id),
    FOREIGN KEY (class_id) REFERENCES classes(class_id),
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id),
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id)
);

-- Student-Class enrollment
CREATE TABLE IF NOT EXISTS student_classes (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    class_id INT NOT NULL,
    enrollment_date DATE DEFAULT (CURRENT_DATE),
    status ENUM('ACTIVE', 'TRANSFERRED', 'GRADUATED', 'DROPPED') DEFAULT 'ACTIVE',
    UNIQUE KEY unique_student_class (student_id, class_id),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (class_id) REFERENCES classes(class_id)
);

-- Grades/Results table
CREATE TABLE IF NOT EXISTS grades (
    grade_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    subject_id INT NOT NULL,
    class_id INT NOT NULL,
    exam_type ENUM('QUIZ', 'MIDTERM', 'FINAL', 'ASSIGNMENT', 'PROJECT') NOT NULL,
    marks_obtained DECIMAL(5,2),
    total_marks DECIMAL(5,2),
    grade_letter VARCHAR(2),
    semester VARCHAR(20),
    academic_year VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (subject_id) REFERENCES subjects(subject_id),
    FOREIGN KEY (class_id) REFERENCES classes(class_id)
);

-- Attendance table
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    class_id INT NOT NULL,
    attendance_date DATE NOT NULL,
    status ENUM('PRESENT', 'ABSENT', 'LATE', 'EXCUSED') NOT NULL,
    remarks TEXT,
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (class_id) REFERENCES classes(class_id),
    UNIQUE KEY unique_student_date (student_id, attendance_date)
);

-- Fees table
CREATE TABLE IF NOT EXISTS fees (
    fee_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT NOT NULL,
    fee_type ENUM('TUITION', 'TRANSPORT', 'LIBRARY', 'LAB', 'SPORTS', 'OTHER') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    due_date DATE NOT NULL,
    paid_date DATE NULL,
    payment_method ENUM('CASH', 'CARD', 'BANK_TRANSFER', 'CHEQUE') NULL,
    status ENUM('PENDING', 'PAID', 'OVERDUE', 'WAIVED') DEFAULT 'PENDING',
    remarks TEXT,
    FOREIGN KEY (student_id) REFERENCES students(student_id)
);

-- Notices/Announcements table
CREATE TABLE IF NOT EXISTS notices (
    notice_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    target_audience ENUM('ALL', 'STUDENTS', 'TEACHERS', 'PARENTS', 'ADMIN') NOT NULL,
    priority ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') DEFAULT 'MEDIUM',
    created_by INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (created_by) REFERENCES users(user_id)
);

-- Insert default users (password: admin123)
INSERT INTO users (username, password_hash, email, first_name, last_name, role) VALUES 
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'admin@school.com', 'System', 'Administrator', 'ADMIN'),
('teacher1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'sarah.johnson@school.edu', 'Dr. Sarah', 'Johnson', 'TEACHER'),
('student1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'student@school.edu', 'Student', 'Name', 'STUDENT'),
('parent1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'parent@school.edu', 'Parent', 'Name', 'PARENT')
ON DUPLICATE KEY UPDATE username = username;

-- Insert some sample subjects
INSERT INTO subjects (subject_name, subject_code, description, credits) VALUES
('Mathematics', 'MATH101', 'Basic Mathematics', 3),
('English Language', 'ENG101', 'English Language and Literature', 3),
('Science', 'SCI101', 'General Science', 3),
('History', 'HIST101', 'World History', 2),
('Physical Education', 'PE101', 'Physical Education and Sports', 1),
('Computer Science', 'CS101', 'Introduction to Computer Science', 3),
('Art', 'ART101', 'Visual Arts', 2),
('Music', 'MUS101', 'Music Theory and Practice', 2)
ON DUPLICATE KEY UPDATE subject_name = subject_name;

-- Insert sample teachers
INSERT INTO teachers (user_id, employee_number, department, specialization, salary) VALUES
(2, 'T001', 'Mathematics', 'Advanced Mathematics', 75000.00)
ON DUPLICATE KEY UPDATE employee_number = employee_number;

-- Insert sample students
INSERT INTO students (user_id, student_number, date_of_birth, gender, address, phone_number, parent_contact) VALUES
(3, 'S001', '2005-03-15', 'MALE', '123 Main St, City', '555-0123', '555-0124')
ON DUPLICATE KEY UPDATE student_number = student_number;

-- Insert sample classes
INSERT INTO classes (class_name, grade_level, academic_year, teacher_id, max_students) VALUES
('Grade 10A', 10, '2024-2025', 1, 30)
ON DUPLICATE KEY UPDATE class_name = class_name;

-- Insert sample grades
INSERT INTO grades (student_id, subject_id, class_id, exam_type, marks_obtained, total_marks, grade_letter, semester, academic_year) VALUES
(1, 1, 1, 'MIDTERM', 85.00, 100.00, 'A', 'Fall 2024', '2024-2025'),
(1, 2, 1, 'QUIZ', 92.00, 100.00, 'A+', 'Fall 2024', '2024-2025'),
(1, 3, 1, 'FINAL', 78.00, 100.00, 'B+', 'Fall 2024', '2024-2025')
ON DUPLICATE KEY UPDATE marks_obtained = marks_obtained;

-- Insert sample attendance
INSERT INTO attendance (student_id, class_id, attendance_date, status) VALUES
(1, 1, '2024-10-01', 'PRESENT'),
(1, 1, '2024-10-02', 'PRESENT'),
(1, 1, '2024-10-03', 'ABSENT'),
(1, 1, '2024-10-04', 'PRESENT'),
(1, 1, '2024-10-05', 'PRESENT')
ON DUPLICATE KEY UPDATE status = status;

-- Create indexes for better performance
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_students_student_number ON students(student_number);
CREATE INDEX idx_teachers_employee_number ON teachers(employee_number);
CREATE INDEX idx_grades_student_subject ON grades(student_id, subject_id);
CREATE INDEX idx_attendance_student_date ON attendance(student_id, attendance_date);
CREATE INDEX idx_fees_student_status ON fees(student_id, status);
