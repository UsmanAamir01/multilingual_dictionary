package dal;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class AbstractDAOFactory implements IDAOFactory {
    private static IDAOFactory instance = null;
    private static final String CONFIG_FILE = "config.properties";

    public static IDAOFactory getInstance() {
        if (instance == null) {
            synchronized (AbstractDAOFactory.class) {
                if (instance == null) {
                    try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
                        Properties properties = new Properties();
                        properties.load(input);
                        String factoryClassName = properties.getProperty("dao.factory");

                        Class<?> c = Class.forName(factoryClassName);
                        instance = (IDAOFactory) c.getDeclaredConstructor().newInstance();
                    } catch (IOException e) {
                        throw new RuntimeException("Could not read config file", e);
                    } catch (Exception e) {
                        throw new RuntimeException("Could not instantiate factory class", e);
                    }
                }
            }
        }
        return instance;
    }
}
