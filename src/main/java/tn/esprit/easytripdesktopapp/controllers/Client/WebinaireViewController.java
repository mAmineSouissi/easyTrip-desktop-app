package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import tn.esprit.easytripdesktopapp.models.Webinaire;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

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
    private boolean isMicroEnabled = false; // Initialement désactivé
    private TargetDataLine audioLine; // Ligne pour capturer l'audio
    private Socket socket; // Connexion au serveur audio
    private OutputStream audioOut; // Flux pour envoyer l'audio
    private Thread audioThread; // Thread pour la capture et l'envoi
    private static final String SERVER_IP = "127.0.0.1"; // IP du serveur audio (localhost pour test)
    private static final int SERVER_PORT = 5000; // Port du serveur audio
    private static final int BUFFER_SIZE = 1024;

    public void setWebinaire(Webinaire webinaire) {
        this.webinaire = webinaire;
        titleLabel.setText(webinaire.getTitle());

        String roomId = webinaire.getRoomId();
        System.out.println("Rejoindre la salle : " + roomId);

        String videoFile = getClass().getResource("/hotel.mp4").toString();
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

        if (isMicroEnabled) {
            enableMicrophone();
        } else {
            disableMicrophone();
        }
    }

    private void enableMicrophone() {
        try {
            // Configuration du format audio : mono, 44.1 kHz, 16 bits
            AudioFormat format = new AudioFormat(44100.0f, 16, 1, true, false); // Mono au lieu de stéréo
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Formats audio disponibles sur votre système :");
                Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
                for (Mixer.Info mixerInfo : mixerInfos) {
                    Mixer mixer = AudioSystem.getMixer(mixerInfo);
                    Line.Info[] lineInfos = mixer.getTargetLineInfo();
                    for (Line.Info lineInfo : lineInfos) {
                        System.out.println("Ligne supportée : " + lineInfo);
                    }
                }
                showErrorDialog("Erreur Audio", "Ligne audio non supportée. Essayez un autre format (par ex. 48 kHz ou mono).");
                isMicroEnabled = false;
                microButton.setText("Activer Micro");
                return;
            }


            audioLine = (TargetDataLine) AudioSystem.getLine(info);
            audioLine.open(format);
            audioLine.start();


            System.out.println("Tentative de connexion au serveur audio : " + SERVER_IP + ":" + SERVER_PORT);
            socket = new Socket(SERVER_IP, SERVER_PORT);
            audioOut = socket.getOutputStream();
            System.out.println("Connexion au serveur audio réussie.");


            audioThread = new Thread(() -> {
                byte[] buffer = new byte[BUFFER_SIZE];
                try {
                    while (isMicroEnabled && audioLine.isOpen()) {
                        int bytesRead = audioLine.read(buffer, 0, buffer.length);
                        audioOut.write(buffer, 0, bytesRead);
                        audioOut.flush();
                    }
                } catch (IOException e) {
                    System.out.println("Erreur lors de l'envoi audio : " + e.getMessage());
                }
            });
            audioThread.start();

            System.out.println("Microphone activé et transmission démarrée.");
        } catch (LineUnavailableException e) {
            showErrorDialog("Erreur Audio", "Impossible d'accéder au microphone : " + e.getMessage());
            isMicroEnabled = false;
            microButton.setText("Activer Micro");
        } catch (IOException e) {
            showErrorDialog("Erreur Réseau", "Impossible de se connecter au serveur audio : " + e.getMessage());
            isMicroEnabled = false;
            microButton.setText("Activer Micro");
        }
    }

    private void disableMicrophone() {
        if (audioLine != null && audioLine.isOpen()) {
            audioLine.stop();
            audioLine.close();
        }
        if (audioThread != null && audioThread.isAlive()) {
            audioThread.interrupt();
        }
        if (audioOut != null) {
            try {
                audioOut.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture du flux audio : " + e.getMessage());
            }
        }
        if (socket != null && !socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Erreur lors de la fermeture de la socket : " + e.getMessage());
            }
        }
        System.out.println("Microphone désactivé et transmission arrêtée.");
    }

    @FXML
    private void quitWebinaire() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        disableMicrophone(); // Arrêter la transmission audio si active
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