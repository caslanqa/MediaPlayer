package com.caslandev.Utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ReadJson {
    public static String getFile(String fileId) {

        try (InputStream inputStream = GoogleDriveService.driveService.files().get(fileId).executeMediaAsInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return stringBuilder.toString();
        } catch (IOException e) {
            System.out.println("Error reading JSON file with ID: " + fileId);
            e.printStackTrace();  // Hatanın detayını yazdırır
            return null;
        }
    }

    public static JsonObject getJsonObject(String fileId){
        String fileContent = getFile(fileId);

        JsonObject jsonObject = JsonParser.parseString(fileContent).getAsJsonObject();

        return jsonObject;
    }
}
