package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.MediaException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import tn.esprit.easytripdesktopapp.models.Webinaire;

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

        // Charger la vidéo du webinaire depuis les ressources
        String videoFile = getClass().getResource("/hotel.mp4").toString(); // Chemin relatif depuis le dossier resources
        if (videoFile != null) {
            try {
                Media media = new Media(videoFile);
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.play();
            } catch (MediaException e) {
                showErrorDialog("Erreur de média", "Le fichier vidéo est corrompu ou incompatible.");
                e.printStackTrace();
            }
        } else {
            showErrorDialog("Fichier introuvable", "Le fichier vidéo spécifié est introuvable dans les ressources.");
        }
    }

    @FXML
    private void toggleMicro() {
        isMicroEnabled = !isMicroEnabled;
        microButton.setText(isMicroEnabled ? "Désactiver Micro" : "Activer Micro");

        // Logique pour activer/désactiver le micro
        if (isMicroEnabled) {
            enableMicrophone();
        } else {
            disableMicrophone();
        }
    }

    private void enableMicrophone() {
        // Logique pour activer le micro
        System.out.println("Microphone activé");
        // Ici, vous pouvez ajouter le code pour démarrer la capture audio
    }

    private void disableMicrophone() {
        // Logique pour désactiver le micro
        System.out.println("Microphone désactivé");
        // Ici, vous pouvez ajouter le code pour arrêter la capture audio
    }

    @FXML
    private void quitWebinaire() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        quitButton.getScene().getWindow().hide();
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}