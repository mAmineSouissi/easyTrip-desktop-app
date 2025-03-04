package tn.esprit.easytripdesktopapp.audio;

import javax.sound.sampled.*;
import java.io.*;
import java.net.*;

public class AudioServer {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;
    private TargetDataLine line;

    public void start() {
        try {
            // Configuration du format audio
            AudioFormat format = new AudioFormat(44100.0f, 16, 2, true, false);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                System.out.println("Ligne audio non supportée.");
                return;
            }

            // Initialisation de la ligne de capture
            line = (TargetDataLine) AudioSystem.getLine(info);
            line.open(format);
            line.start();

            // Création du serveur
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Serveur démarré, en attente de connexion sur le port " + PORT + "...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connecté : " + clientSocket.getInetAddress().getHostAddress());

            // Flux de sortie vers le client
            OutputStream out = clientSocket.getOutputStream();
            byte[] buffer = new byte[BUFFER_SIZE];

            // Capture et envoi de l'audio
            while (true) {
                int bytesRead = line.read(buffer, 0, buffer.length);
                out.write(buffer, 0, bytesRead);
                out.flush();
            }
        } catch (LineUnavailableException e) {
            System.out.println("Erreur d'accès à la ligne audio : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Erreur réseau : " + e.getMessage());
        } finally {
            if (line != null) {
                line.stop();
                line.close();
            }
        }
    }

    public static void main(String[] args) {
        AudioServer server = new AudioServer();
        server.start();
    }
}