package com.caslandev.Utils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public static String getPublicIP() {
        String ipServiceURL = "https://api.ipify.org"; // You can also use "https://checkip.amazonaws.com"
        try {
            URL url = new URL(ipServiceURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String publicIP = in.readLine(); // Service returns the IP as plain text
            in.close();
            return publicIP;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

