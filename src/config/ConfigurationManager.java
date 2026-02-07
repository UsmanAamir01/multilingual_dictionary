package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Centralized configuration manager for the application.
 * Loads configuration from external properties files to avoid hardcoding sensitive data.
 */
public class ConfigurationManager {
    private static final Logger LOGGER = Logger.getLogger(ConfigurationManager.class.getName());
    private static final String DATABASE_CONFIG = "config/database.properties";
    
    private static Properties databaseConfig;
    private static boolean initialized = false;
    
    private ConfigurationManager() {
        // Private constructor - utility class
    }
    
    /**
     * Initialize the configuration manager by loading all config files.
     * This is called lazily on first access.
     */
    private static synchronized void initialize() {
        if (initialized) {
            return;
        }
        
        databaseConfig = loadProperties(DATABASE_CONFIG);
        initialized = true;
        LOGGER.log(Level.INFO, "ConfigurationManager initialized successfully");
    }
    
    private static Properties loadProperties(String filename) {
        Properties props = new Properties();
        
        // Try loading from file system first (allows external configuration)
        try (InputStream input = new FileInputStream(filename)) {
            props.load(input);
            LOGGER.log(Level.INFO, "Loaded configuration from: {0}", filename);
            return props;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not load from file system: {0}, trying classpath", filename);
        }
        
        // Fallback to classpath
        try (InputStream input = ConfigurationManager.class.getClassLoader()
                .getResourceAsStream(filename)) {
            if (input != null) {
                props.load(input);
                LOGGER.log(Level.INFO, "Loaded configuration from classpath: {0}", filename);
            } else {
                LOGGER.log(Level.SEVERE, "Configuration file not found: {0}", filename);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading configuration: {0}", e.getMessage());
        }
        
        return props;
    }
    
    /**
     * Get a database configuration property.
     * @param key The property key (e.g., "db.url", "db.user")
     * @return The property value, or null if not found
     */
    public static String getDbProperty(String key) {
        if (!initialized) {
            initialize();
        }
        return databaseConfig.getProperty(key);
    }
    
    /**
     * Get a database configuration property with a default value.
     * @param key The property key
     * @param defaultValue The default value if key not found
     * @return The property value, or defaultValue if not found
     */
    public static String getDbProperty(String key, String defaultValue) {
        String value = getDbProperty(key);
        return value != null ? value : defaultValue;
    }
    
    /**
     * Get a database configuration property as an integer.
     * @param key The property key
     * @param defaultValue The default value if key not found or invalid
     * @return The property value as int, or defaultValue if not found/invalid
     */
    public static int getDbPropertyInt(String key, int defaultValue) {
        String value = getDbProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid integer value for {0}: {1}", 
                           new Object[]{key, value});
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a database configuration property as a long.
     * @param key The property key
     * @param defaultValue The default value if key not found or invalid
     * @return The property value as long, or defaultValue if not found/invalid
     */
    public static long getDbPropertyLong(String key, long defaultValue) {
        String value = getDbProperty(key);
        if (value != null) {
            try {
                return Long.parseLong(value.trim());
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Invalid long value for {0}: {1}", 
                           new Object[]{key, value});
            }
        }
        return defaultValue;
    }
    
    /**
     * Reload all configuration files.
     * Useful for hot-reloading configuration changes without restarting.
     */
    public static synchronized void reload() {
        initialized = false;
        initialize();
        LOGGER.log(Level.INFO, "Configuration reloaded");
    }
}
