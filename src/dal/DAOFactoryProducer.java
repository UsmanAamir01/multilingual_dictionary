package dal;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DAOFactoryProducer {
    private static final String CONFIG_FILE = "config.properties";

    public static AbstractDAOFactory getFactory() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
            String factoryType = properties.getProperty("dao.factory");

            switch (factoryType) {
                case "MySQLDAOFactory":
                    return new MySQLDAOFactory();
                case "FileDAOFactory":
                    return new FileDAOFactory();
                default:
                    throw new IllegalArgumentException("Unknown factory type: " + factoryType);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not read config file", e);
        }
    }
}