package tn.esprit.easytripdesktopapp.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Charger le fichier FXML pour la gestion des hôtels
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/ClientWebinaire.fxml"));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Gestion des Hôtels");
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de l'interface utilisateur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
