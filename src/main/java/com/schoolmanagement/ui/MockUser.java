package com.schoolmanagement.ui;

/**
 * Mock User class for testing without database connection
 */
public class MockUser {
    private String username;
    private String role;
    private String firstName;
    private String lastName;
    private String email;

    public MockUser(String username, String role) {
        this.username = username;
        this.role = role;
        this.firstName = username.equals("admin") ? "System" : "User";
        this.lastName = username.equals("admin") ? "Administrator" : "Name";
        this.email = username + "@school.com";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

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

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return "MockUser{" +
                "username='" + username + '\'' +
                ", role='" + role + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
