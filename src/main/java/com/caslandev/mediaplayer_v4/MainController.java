package com.caslandev.mediaplayer_v4;

import com.caslandev.Utils.ConfigUtil;
import com.caslandev.Utils.GoogleDriveService;
import com.caslandev.Utils.JsonModel;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MainController implements Initializable {

    String baseUrl = "https://www.googleapis.com/drive/v3/files/%s?alt=media&key=AIzaSyAMi67pMAVX4eccRfzKJs01r4OnDtSGtVQ";

    JsonModel jsonModel = new JsonModel();

    private MediaPlayer mediaPlayer;

    private Media media;

    AtomicBoolean flag = new AtomicBoolean(false);
    AtomicInteger pixel = new AtomicInteger(0);
    private double seekRate = 0.1;
    DecimalFormat decimalFormat = new DecimalFormat("#.0");
    boolean durationFlag = false;

    @FXML
    private MediaView mediaView;

    @FXML
    private TextField txtBoxPassword;

    @FXML
    private TextField txtBoxUsername;

    @FXML
    private Button btnFaster;

    @FXML
    private Button btnPaylist;

    @FXML
    private Button btnPlay;

    @FXML
    private Button btnSlow;

    @FXML
    private Button btnStop;

    @FXML
    private Label lblDuration;

    @FXML
    private Slider playerSlider;

    @FXML
    private AnchorPane playlistPane;

    @FXML
    private TreeView<String> playlistView;

    @FXML
    private ImageView volumeLabel;

    @FXML
    private Slider volumeSlider;


    @FXML
    private Button seekback10;

    @FXML
    private Button seekforward10;

    @FXML
    private AnchorPane loginPane;


    @FXML
    void seekback10(MouseEvent event) {
        setSeekBackward10();
    }

    @FXML
    void seekforward10(MouseEvent event) {
        setSeekForward10();
    }

    @FXML
    void btnSlow(MouseEvent event) {

        if (mediaPlayer.getRate() > 1.1) {
            mediaPlayer.setRate(mediaPlayer.getRate() - seekRate);
            btnFaster.setText(decimalFormat.format(mediaPlayer.getRate()));
        }

        if (mediaPlayer.getRate() > 0.5 && mediaPlayer.getRate() <= 1) {
            mediaPlayer.setRate(mediaPlayer.getRate() - seekRate);
            btnSlow.setText(decimalFormat.format(mediaPlayer.getRate()));
        } else if (mediaPlayer.getRate() <= 0.5) {
            mediaPlayer.setRate(1);
            btnSlow.setText("<<");
        }

        if (mediaPlayer.getRate() >= 0.91 && mediaPlayer.getRate() <= 1.1) {
            btnSlow.setText("<<");
            btnFaster.setText(">>");
        }
    }

    @FXML
    void btnFaster(MouseEvent event) {

        if (mediaPlayer.getRate() < 1) {
            mediaPlayer.setRate(mediaPlayer.getRate() + seekRate);
            btnSlow.setText(decimalFormat.format(mediaPlayer.getRate()));
        }

        if (mediaPlayer.getRate() >= 1 && mediaPlayer.getRate() < 3) {
            mediaPlayer.setRate(mediaPlayer.getRate() + seekRate);
            btnFaster.setText(decimalFormat.format(mediaPlayer.getRate()));
        } else if (mediaPlayer.getRate() >= 3) {
            mediaPlayer.setRate(1);
            btnFaster.setText(">>");
        }

        if (mediaPlayer.getRate() >= 0.91 && mediaPlayer.getRate() <= 1.1) {
            btnSlow.setText("<<");
            btnFaster.setText(">>");
        }
    }

    @FXML
    void btnPaylist(MouseEvent event) {
        if (!loginPane.isVisible()) {
            if (!flag.get() && pixel.get() == 0) {
                pixel.set(600);

                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), playlistPane);
                tt1.setByX(pixel.get());
                tt1.play();
                flag.set(true);
            } else {
                pixel.set(-600);

                TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), playlistPane);
                tt1.setByX(pixel.get());
                tt1.play();
                flag.set(false);
                pixel.set(0);
            }
        }

    }

    @FXML
    void btnPlay(MouseEvent event) {
        try {
            if (!loginPane.isVisible()) {
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    btnPlay.setText("Play");
                }
                TreeItem<String> treeItem = playlistView.getSelectionModel().getSelectedItem();

                String selectedVideo = String.format(baseUrl, treeItem.getValue());

                if (!selectedVideo.isEmpty()) {

                    media = new Media(selectedVideo);
                    mediaPlayer = new MediaPlayer(media);
                    mediaView.setMediaPlayer(mediaPlayer);

                    mediaView.setPreserveRatio(true);

                    Scene scene = mediaView.getScene();
                    mediaView.fitWidthProperty().bind(scene.widthProperty());
                    mediaView.fitHeightProperty().bind(scene.heightProperty());

                    countForwardDuration();

                    mediaPlayer.setOnReady(() -> {
                        Duration totalDuration = media.getDuration();
                        playerSlider.setMax(totalDuration.toSeconds());
                        lblDuration.setText(formatDuration(totalDuration));
                    });

                    mediaPlayer.play();
                    btnPlay.setText("Pause");
                }
                if (!flag.get() && pixel.get() == 0) {
                    pixel.set(600);

                    TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), playlistPane);
                    tt1.setByX(pixel.get());
                    tt1.play();
                    flag.set(true);
                } else {
                    pixel.set(-600);

                    TranslateTransition tt1 = new TranslateTransition(Duration.seconds(0.5), playlistPane);
                    tt1.setByX(pixel.get());
                    tt1.play();
                    flag.set(false);
                    pixel.set(0);
                }
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Media Player Message");
            alert.setHeaderText(null);
            alert.setContentText("Please select a video link from playlist");
            alert.showAndWait();
        }
    }

    @FXML
    void btnStop(MouseEvent event) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaView.setMediaPlayer(null);
            btnPlay.setText("Play");
        }
    }

    @FXML
    void lblDuration(MouseEvent event) {
        if (!durationFlag) {
            countForwardDuration();
            durationFlag = true;
        } else {
            countBackwardDuration();
            durationFlag = false;
        }
    }

    @FXML
    void btnLogin(MouseEvent event) {
        String username = txtBoxUsername.getText();
        String password = txtBoxPassword.getText();
        String deviceIp = ConfigUtil.getPublicIP();

        if (jsonModel.checkLogin(username, password,deviceIp)) {
            loginPane.setVisible(false);
            setPlayerItemsVisibility(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Message");
            alert.setHeaderText(null);
            alert.setContentText("Please provide valid credentials");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setPlayerItemsVisibility(false);
        GoogleDriveService.initializeGoogleDriveService();

        playlistView.setRoot(jsonModel.getTreeItems());

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.1), playlistPane);
        tt.setByX(-600);
        tt.play();

        openVolume();

        volumeSlider.valueProperty().addListener((obs, oldVolume, newVolume) -> {
            if (mediaPlayer != null) {
                volumeSlider.setMax(100.0);
                mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
            }
        });

        playerSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (mediaPlayer != null && playerSlider.isValueChanging()) {
                mediaPlayer.seek(Duration.seconds(newValue.doubleValue()));
            }
        });

    }

    @FXML
    void volumeLabel(MouseEvent event) {
        if (volumeSlider.getValue() == 0) {
            openVolume();
            volumeLabel.setImage(new Image("file:src/main/resources/images/pngwing.png"));
        } else {
            muteVolume();
            volumeLabel.setImage(new Image("file:src/main/resources/images/mute.png"));
        }
    }

    void openVolume() {
        volumeSlider.setMax(100.0);
        volumeSlider.setValue(100.0);
    }

    void muteVolume() {
        volumeSlider.setMax(0);
        volumeSlider.setValue(0);
    }

    @FXML
    void sliderPressed(MouseEvent event) {
        mediaPlayer.seek(Duration.seconds(playerSlider.getValue()));
    }

    String formatDuration(Duration duration) {
        long totalSeconds = (long) duration.toSeconds();
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    void countForwardDuration() {
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            playerSlider.setValue(newTime.toSeconds());
            lblDuration.setText(formatDuration(newTime)); // Assuming you have a label for current time
        });
    }

    void countBackwardDuration() {
        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            Duration totalDuration = mediaPlayer.getTotalDuration();
            Duration remainingTime = totalDuration.subtract(newTime);
            playerSlider.setValue(newTime.toSeconds());
            lblDuration.setText(formatDuration(remainingTime));
        });
    }

    void setSeekForward10() {
        Duration currentTime = mediaPlayer.getCurrentTime();

        Duration newTime = currentTime.add(Duration.seconds(10));

        if (newTime.lessThanOrEqualTo(mediaPlayer.getTotalDuration())) {
            mediaPlayer.seek(newTime);
        } else {
            mediaPlayer.seek(mediaPlayer.getTotalDuration());
        }
    }

    void setSeekBackward10() {
        Duration currentTime = mediaPlayer.getCurrentTime();

        Duration newTime = currentTime.subtract(Duration.seconds(10));

        if (newTime.greaterThanOrEqualTo(Duration.ZERO)) {
            mediaPlayer.seek(newTime);
        } else {
            mediaPlayer.seek(mediaPlayer.getTotalDuration());
        }
    }

    void setPlayerItemsVisibility(boolean flag){
        playerSlider.setVisible(flag);
        volumeSlider.setVisible(flag);
        lblDuration.setVisible(flag);
        btnPaylist.setVisible(flag);
        btnPlay.setVisible(flag);
        btnStop.setVisible(flag);
        btnFaster.setVisible(flag);
        btnSlow.setVisible(flag);
        seekforward10.setVisible(flag);
        seekback10.setVisible(flag);
        volumeLabel.setVisible(flag);
    }
}