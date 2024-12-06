import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private Properties properties;

    // Constructor to load the properties file
    public Config() {
        properties = new Properties();
        loadProperties();
    }

    // Load properties from config.properties file
    private void loadProperties() {
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            // Load the properties file
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.err.println("Failed to load config.properties file.");
        }
    }

    // Method to get property by key
    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    // Method to get property by key with a default value
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public static void main(String[] args) {
        // Create Config object to load properties
        Config config = new Config();

        // Test loading properties
        System.out.println("Database URL: " + config.getProperty("url"));
        System.out.println("Database Username: " + config.getProperty("username"));
        System.out.println("CSV File Path: " + config.getProperty("csvFilePath"));
        System.out.println("Module Logs Error: " + config.getProperty("Module.LogsError"));
    }
}
