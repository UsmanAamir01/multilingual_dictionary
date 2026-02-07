package dal.pool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import config.ConfigurationManager;

/**
 * Connection Pool Manager - Provides efficient database connection reuse.
 * 
 * This is a lightweight connection pool implementation compatible with JDK 8.
 * For production use with more features, consider adding HikariCP dependency:
 *   - Add HikariCP-4.0.3.jar to lib/ folder
 *   - Uncomment the HikariCP implementation section below
 * 
 * Current implementation uses a simple blocking queue pool that works without
 * external dependencies.
 */
public class ConnectionPoolManager {
    private static final Logger LOGGER = Logger.getLogger(ConnectionPoolManager.class.getName());
    
    // Pool configuration
    private static final int POOL_SIZE = ConfigurationManager.getDbPropertyInt("db.pool.maximumPoolSize", 10);
    private static final int MIN_IDLE = ConfigurationManager.getDbPropertyInt("db.pool.minimumIdle", 2);
    private static final long CONNECTION_TIMEOUT = ConfigurationManager.getDbPropertyLong("db.pool.connectionTimeout", 30000);
    
    // Connection parameters from config
    private static final String URL = ConfigurationManager.getDbProperty("db.url", "jdbc:mysql://localhost:3306/Dictionarydb");
    private static final String USER = ConfigurationManager.getDbProperty("db.user", "root");
    private static final String PASSWORD = ConfigurationManager.getDbProperty("db.password", "");
    
    // Connection pool
    private static BlockingQueue<Connection> connectionPool;
    private static volatile boolean initialized = false;
    private static final Object INIT_LOCK = new Object();
    
    private ConnectionPoolManager() {
        // Private constructor - utility class
    }
    
    /**
     * Initialize the connection pool.
     * Called lazily on first getConnection() call.
     */
    private static void initialize() {
        synchronized (INIT_LOCK) {
            if (initialized) {
                return;
            }
            
            try {
                // Load JDBC driver
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                connectionPool = new ArrayBlockingQueue<>(POOL_SIZE);
                
                // Pre-create minimum idle connections
                for (int i = 0; i < MIN_IDLE; i++) {
                    Connection conn = createNewConnection();
                    if (conn != null) {
                        connectionPool.offer(conn);
                    }
                }
                
                initialized = true;
                LOGGER.log(Level.INFO, "Connection pool initialized with {0} connections", 
                           connectionPool.size());
                
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
                throw new RuntimeException("MySQL JDBC Driver not found", e);
            }
        }
    }
    
    /**
     * Create a new database connection.
     */
    private static Connection createNewConnection() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.log(Level.FINE, "Created new database connection");
            return conn;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to create database connection", e);
            return null;
        }
    }
    
    /**
     * Get a connection from the pool.
     * If no connections are available, creates a new one if pool not at max capacity.
     * 
     * @return A database connection
     * @throws SQLException if unable to get a connection
     */
    public static Connection getConnection() throws SQLException {
        if (!initialized) {
            initialize();
        }
        
        // Try to get from pool first
        Connection conn = connectionPool.poll();
        
        if (conn != null) {
            // Validate connection is still alive
            try {
                if (conn.isValid(2)) {
                    LOGGER.log(Level.FINE, "Reusing pooled connection");
                    return conn;
                } else {
                    LOGGER.log(Level.WARNING, "Pooled connection invalid, creating new one");
                    conn.close();
                }
            } catch (SQLException e) {
                // Connection is dead, create a new one
                LOGGER.log(Level.WARNING, "Pooled connection test failed", e);
            }
        }
        
        // Create new connection
        conn = createNewConnection();
        if (conn == null) {
            throw new SQLException("Unable to create database connection");
        }
        
        return conn;
    }
    
    /**
     * Release a connection back to the pool.
     * Call this instead of connection.close() to enable reuse.
     * 
     * @param conn The connection to release
     */
    public static void releaseConnection(Connection conn) {
        if (conn == null) {
            return;
        }
        
        try {
            if (!conn.isClosed() && conn.isValid(1)) {
                // Try to add back to pool
                if (!connectionPool.offer(conn)) {
                    // Pool is full, close the connection
                    conn.close();
                    LOGGER.log(Level.FINE, "Pool full, closed excess connection");
                } else {
                    LOGGER.log(Level.FINE, "Connection returned to pool");
                }
            } else {
                conn.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error releasing connection", e);
            try {
                conn.close();
            } catch (SQLException ex) {
                // Ignore close errors
            }
        }
    }
    
    /**
     * Get pool statistics for monitoring.
     * @return Array with [availableConnections, poolCapacity]
     */
    public static int[] getPoolStats() {
        return new int[] { 
            connectionPool != null ? connectionPool.size() : 0, 
            POOL_SIZE 
        };
    }
    
    /**
     * Shutdown the connection pool and close all connections.
     * Call this on application exit.
     */
    public static void shutdown() {
        if (!initialized || connectionPool == null) {
            return;
        }
        
        LOGGER.log(Level.INFO, "Shutting down connection pool...");
        
        Connection conn;
        while ((conn = connectionPool.poll()) != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Error closing pooled connection", e);
            }
        }
        
        initialized = false;
        LOGGER.log(Level.INFO, "Connection pool shutdown complete");
    }
    
    // ==============================================================
    // HikariCP Implementation (Uncomment when HikariCP jar is added)
    // ==============================================================
    /*
    import com.zaxxer.hikari.HikariConfig;
    import com.zaxxer.hikari.HikariDataSource;
    
    private static HikariDataSource dataSource;
    
    private static void initializeHikari() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(ConfigurationManager.getDbProperty("db.url"));
        config.setUsername(ConfigurationManager.getDbProperty("db.user"));
        config.setPassword(ConfigurationManager.getDbProperty("db.password"));
        config.setMaximumPoolSize(ConfigurationManager.getDbPropertyInt("db.pool.maximumPoolSize", 10));
        config.setMinimumIdle(ConfigurationManager.getDbPropertyInt("db.pool.minimumIdle", 2));
        config.setConnectionTimeout(ConfigurationManager.getDbPropertyLong("db.pool.connectionTimeout", 30000));
        config.setIdleTimeout(ConfigurationManager.getDbPropertyLong("db.pool.idleTimeout", 600000));
        config.setMaxLifetime(ConfigurationManager.getDbPropertyLong("db.pool.maxLifetime", 1800000));
        config.setPoolName(ConfigurationManager.getDbProperty("db.pool.poolName", "DictionaryPool"));
        
        dataSource = new HikariDataSource(config);
    }
    
    public static Connection getConnectionHikari() throws SQLException {
        if (dataSource == null) {
            initializeHikari();
        }
        return dataSource.getConnection();
    }
    */
}
