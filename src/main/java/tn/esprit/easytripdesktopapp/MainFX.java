package tn.esprit.easytripdesktopapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainFX extends Application {

    private static Stage primaryStage;
    private static Locale currentLocale = new Locale("en");
    public static final String CURRENCY = "$";
    @Override
    public void start(Stage primaryStage) {
        MainFX.primaryStage = primaryStage;
        loadUI();
    }

    public static void setLocale(Locale locale) {
        currentLocale = locale;
        loadUI();
    }

    public static void loadUI() {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", currentLocale);
            FXMLLoader loader = new FXMLLoader(MainFX.class.getResource("/tn/esprit/easytripdesktopapp/FXML/Login.fxml"), bundle);
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(bundle.getString("greeting"));
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("General error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
