package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesHandler {
    private String value;

    public String getProperty(String key) {
        try (InputStream input = getClass().getResource("/application.properties").openStream()) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Cannot read properties file");
                return "";
            }
            prop.load(input);
            value = prop.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
