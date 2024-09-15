module com.caslandev.mediaplayer_v4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires com.google.gson;
    requires google.api.client;
    requires com.google.api.client;
    requires com.google.api.client.json.gson;
    requires com.google.api.services.drive;


    opens com.caslandev.mediaplayer_v4 to javafx.fxml;
    exports com.caslandev.mediaplayer_v4;
}