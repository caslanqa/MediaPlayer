package com.caslandev.Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {

    public static String getProperty(String key) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/configurations/config.properties")) {
            properties.load(input);
            return properties.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}

