package com.schoolmanagement.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application configuration management
 */
public class AppConfig {
    private static final Properties properties = new Properties();
    private static final String CONFIG_FILE = "application.properties";

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            } else {
                // Set default values if config file doesn't exist
                setDefaultProperties();
            }
        } catch (IOException e) {
            System.err.println("Error loading application properties: " + e.getMessage());
            setDefaultProperties();
        }
    }

    private static void setDefaultProperties() {
        properties.setProperty("app.name", "School Management System");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("app.theme", "default");
        properties.setProperty("database.host", "localhost");
        properties.setProperty("database.port", "3306");
        properties.setProperty("database.name", "school_management_system");
        properties.setProperty("database.username", "root");
        properties.setProperty("database.password", "password");
        properties.setProperty("ui.lookandfeel", "system");
        properties.setProperty("logging.level", "INFO");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        return Boolean.parseBoolean(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    // Convenience methods for common properties
    public static String getAppName() {
        return getProperty("app.name", "School Management System");
    }

    public static String getAppVersion() {
        return getProperty("app.version", "1.0.0");
    }

    public static String getDatabaseHost() {
        return getProperty("database.host", "localhost");
    }

    public static int getDatabasePort() {
        return getIntProperty("database.port", 3306);
    }

    public static String getDatabaseName() {
        return getProperty("database.name", "school_management_system");
    }

    public static String getDatabaseUsername() {
        return getProperty("database.username", "root");
    }

    public static String getDatabasePassword() {
        return getProperty("database.password", "password");
    }
}
