package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import tn.esprit.easytripdesktopapp.models.Webinaire;

import java.io.File;

public class WebinaireViewController {

    @FXML
    private Label titleLabel;

    @FXML
    private MediaView mediaView;

    @FXML
    private Button microButton;

    @FXML
    private Button quitButton;

    private Webinaire webinaire;
    private MediaPlayer mediaPlayer;
    private boolean isMicroEnabled = true;

    public void setWebinaire(Webinaire webinaire) {
        this.webinaire = webinaire;
        titleLabel.setText(webinaire.getTitle());

        // Utiliser le roomId pour configurer la salle spécifique
        String roomId = webinaire.getRoomId();
        System.out.println("Rejoindre la salle : " + roomId);


    }

    @FXML
    private void toggleMicro() {
        isMicroEnabled = !isMicroEnabled;
        microButton.setText(isMicroEnabled ? "Désactiver Micro" : "Activer Micro");
        // Ajoutez ici la logique pour activer/désactiver le micro
    }

    @FXML
    private void quitWebinaire() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        quitButton.getScene().getWindow().hide();
    }
}