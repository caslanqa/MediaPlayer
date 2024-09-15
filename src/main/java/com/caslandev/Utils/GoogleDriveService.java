package com.caslandev.Utils;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveRequestInitializer;
import javafx.scene.control.Alert;

public class GoogleDriveService {

    public static Drive driveService; // Google Drive API servisi

    private static final String APPLICATION_NAME = "Google Drive Video Player";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String API_KEY = "AIzaSyAMi67pMAVX4eccRfzKJs01r4OnDtSGtVQ";

    public static void initializeGoogleDriveService() {
        try {
            HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            driveService = new Drive.Builder(httpTransport, JSON_FACTORY, null)
                    .setApplicationName(APPLICATION_NAME)
                    .setDriveRequestInitializer(new DriveRequestInitializer(API_KEY))
                    .build();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DB Connection Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot connect to Google Drive Services");
            alert.showAndWait();
        }
    }
}

