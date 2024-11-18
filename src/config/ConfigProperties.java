package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigProperties {
//    DATABASE
    public static String URL_MYSQL;
    public static String USER_DB;
    public static String PASSWORD_DB;
    public static String DATABASE_WH;
    public static String DATABASE_STAGING;
    public static String DATABASE_MART;
    public static String DATABASE_CONTROL;
//    Email
    public static String EMAIL_SERVICE;
    public static String PASSWORD_EMAIL_SERVICE;
    public static String EMAIL_TO;
//    SQL Query
    public static String SELECT_CONFIG_FLAG_TRUE;
    public static String UPDATE_STATUS_CONFIG;
    public static String PROCEDURE_TRUNCATE_STAGIGN;
    public static String LOAD_DATA_TO_STAGING;
    public static String UPDATE_PATH_CONFIG;
    public static String PROCEDURE_TRANSFORM_STAGING;
    public static String LOAD_DATA_TO_WH;
    public static String LOAD_DATA_TO_AGGREGATE;
    public static String LOAD_DATA_TO_MART;
    public static String INSERT_LOG_CONFIG;
    public static String UPDATE_LOG_CONFIG;
    public static String SELECT_LOG_CONFIG;
    public static String SELECT_LOG_BY_RUNNING;
    
//    Config
    public static int TIMEOUT_WAITING;
    public static int MAX_ITERATOR;



//    Test
    static {
    	loadConfiguration();
    }
    
    public static boolean loadConfiguration() {
    	final String FILE_CONFIG = "\\config.properties";
    	Properties properties = new Properties();
        InputStream inputStream = null;
        System.out.println("Loading Configuration");
        try {
            String currentDir = System.getProperty("user.dir");
            inputStream = new FileInputStream(currentDir + FILE_CONFIG);
            ConfigProperties configInstance = new ConfigProperties();

            // load properties from file
            properties.load(inputStream);
 
            // get property by name
            Field[] declaredFields = ConfigProperties.class.getDeclaredFields();
            for (Field field : declaredFields) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                	 String propertyValue = properties.getProperty(field.getName());
                	 System.out.println(field.getName() + ": " + propertyValue);
                     if (field.getType() == String.class) {
                         field.set(null, propertyValue);
                     } else if (field.getType() == int.class) {
                         field.set(null, Integer.parseInt(propertyValue));
                     } else if (field.getType() == boolean.class) {
                         field.set(null, Boolean.parseBoolean(propertyValue));
                     }
					
                }
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } finally {
            // close objects
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Loaded Configuration");

        return true;
    }

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
    	System.out.println(ConfigProperties.EMAIL_SERVICE);
    }
}
