package tn.esprit.easytripdesktopapp.audio;

import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class AudioClient {
    private static final String SERVER_IP = "127.0.0.1"; // Adresse IP du serveur (localhost pour test)
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;
    private SourceDataLine line;

    public void start() {
        try {
            // Configuration du format audio (doit correspondre au serveur)
            AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Ligne audio non supportée.");
                return;
            }

            // Initialisation de la ligne de lecture
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // Connexion au serveur
            Socket socket = new Socket(SERVER_IP, PORT);
            System.out.println("Connecté au serveur : " + SERVER_IP);

            // Flux d'entrée depuis le serveur
            InputStream in = socket.getInputStream();
            byte[] buffer = new byte[BUFFER_SIZE];

            // Réception et lecture de l'audio
            while (true) {
                int bytesRead = in.read(buffer, 0, buffer.length);
                if (bytesRead == -1) break;
                line.write(buffer, 0, bytesRead);
            }
        } catch (LineUnavailableException e) {
            System.out.println("Erreur d'accès à la ligne audio : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur réseau : " + e.getMessage());
        } finally {
            if (line != null) {
                line.drain();
                line.stop();
                line.close();
            }
        }
    }

    public static void main(String[] args) {
        AudioClient client = new AudioClient();
        client.start();
    }
}