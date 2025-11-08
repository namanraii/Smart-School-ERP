package com.schoolmanagement.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database configuration and connection management
 */
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);
    private static HikariDataSource dataSource;
    private static final String CONFIG_FILE = "database.properties";

    static {
        try {
            initializeDataSource();
        } catch (Exception e) {
            logger.warn("Database initialization failed - running in offline mode", e);
            dataSource = null; // Will be handled gracefully by the application
        }
    }

    private static void initializeDataSource() {
        try {
            Properties props = new Properties();
            
            // Default configuration - can be overridden by properties file
            props.setProperty("dataSourceClassName", "com.mysql.cj.jdbc.MysqlDataSource");
            props.setProperty("dataSource.serverName", "localhost");
            props.setProperty("dataSource.port", "3306");
            props.setProperty("dataSource.databaseName", "school_management_system");
            props.setProperty("dataSource.user", "root");
            props.setProperty("dataSource.password", "root1234");
            props.setProperty("dataSource.cachePrepStmts", "true");
            props.setProperty("dataSource.prepStmtCacheSize", "250");
            props.setProperty("dataSource.prepStmtCacheSqlLimit", "2048");
            props.setProperty("dataSource.useServerPrepStmts", "true");
            props.setProperty("dataSource.useLocalSessionState", "true");
            props.setProperty("dataSource.rewriteBatchedStatements", "true");
            props.setProperty("dataSource.cacheResultSetMetadata", "true");
            props.setProperty("dataSource.cacheServerConfiguration", "true");
            props.setProperty("dataSource.elideSetAutoCommits", "true");
            props.setProperty("dataSource.maintainTimeStats", "false");
            
            // Connection pool settings
            props.setProperty("maximumPoolSize", "10");
            props.setProperty("minimumIdle", "2");
            props.setProperty("connectionTimeout", "30000");
            props.setProperty("idleTimeout", "600000");
            props.setProperty("maxLifetime", "1800000");
            props.setProperty("leakDetectionThreshold", "60000");

            HikariConfig config = new HikariConfig(props);
            dataSource = new HikariDataSource(config);
            
            logger.info("Database connection pool initialized successfully");
            
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Get a database connection from the pool
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Test database connection
     * @return true if connection is successful
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            logger.error("Database connection test failed", e);
            return false;
        }
    }

    /**
     * Close the data source and all connections
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }

    /**
     * Get connection pool status
     * @return String representation of pool status
     */
    public static String getPoolStatus() {
        if (dataSource == null) {
            return "DataSource not initialized";
        }
        return String.format("Active: %d, Idle: %d, Total: %d, Waiting: %d",
                dataSource.getHikariPoolMXBean().getActiveConnections(),
                dataSource.getHikariPoolMXBean().getIdleConnections(),
                dataSource.getHikariPoolMXBean().getTotalConnections(),
                dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
    }
}
