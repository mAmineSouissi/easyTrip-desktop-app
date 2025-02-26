package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class AccueilController {

    @FXML
    private Button btnHotels, btnTickets;

    @FXML
    private Text welcomeText;

    @FXML
    private void initialize() {

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2), welcomeText);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    @FXML
    private void goToHotels() {
        loadScene("/tn/esprit/easytripdesktopapp/FXML/Agent/AffichageHotel.fxml", "Gestion des Hôtels");
    }

    @FXML
    private void goToTickets() {
        loadScene("/tn/esprit/easytripdesktopapp/FXML/Agent/AffichageTicket.fxml", "Gestion des Tickets");
    }

    private void loadScene(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, 800, 600));
            stage.show();


            ((Stage) btnHotels.getScene().getWindow()).close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void hoverIn(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #00f2fe; -fx-text-fill: #4facfe; -fx-scale-x: 1.05; -fx-scale-y: 1.05;");
    }

    @FXML
    private void hoverOut(MouseEvent event) {
        Button button = (Button) event.getSource();
        button.setStyle("-fx-background-color: #4facfe; -fx-text-fill: white; -fx-scale-x: 1.0; -fx-scale-y: 1.0;");
    }
}