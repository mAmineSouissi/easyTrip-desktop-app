package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Webinaire;
import tn.esprit.easytripdesktopapp.services.ServiceWebinaire;

import java.io.IOException;
import java.util.List;

public class ClientWebinaireController {

    @FXML
    private FlowPane cardContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> hotelFilter;

    @FXML
    private Button backButton;

    private final ServiceWebinaire webinaireService = new ServiceWebinaire();
    private List<Webinaire> webinaires;

    @FXML
    public void initialize() {
        loadWebinaires();
    }

    @FXML
    private void loadWebinaires() {
        cardContainer.getChildren().clear();
        webinaires = webinaireService.getAll();

        for (Webinaire webinaire : webinaires) {
            VBox card = new VBox(10);
            card.setStyle("-fx-background-color: #ffffff; -fx-padding: 20; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 10, 0, 0);");

            Label titleLabel = new Label("Titre: " + webinaire.getTitle());
            titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            Label descriptionLabel = new Label("Description: " + webinaire.getDescription());
            Label dateLabel = new Label("Date: " + webinaire.getDebutDateTime() + " - " + webinaire.getFinitDateTime());
            Label linkLabel = new Label("Lien: " + webinaire.getLink());
            Label hotelLabel = new Label("Hôtel: " + webinaire.getHotel().getName());

            HBox buttonBox = new HBox(10);
            Button joinButton = new Button("Rejoindre");
            joinButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");
            joinButton.setOnAction(e -> joinWebinaire(webinaire));

            Button leaveButton = new Button("Quitter");
            leaveButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");
            leaveButton.setOnAction(e -> leaveWebinaire(webinaire));

            buttonBox.getChildren().addAll(joinButton, leaveButton);

            card.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, linkLabel, hotelLabel, buttonBox);

            cardContainer.getChildren().add(card);
        }
    }

    @FXML
    private void joinWebinaire(Webinaire webinaire) {
        // Logic to join the webinar (e.g., open the link in a browser)
        System.out.println("Rejoindre le webinaire: " + webinaire.getLink());
    }

    @FXML
    private void leaveWebinaire(Webinaire webinaire) {
        // Logic to leave the webinar
        System.out.println("Quitter le webinaire: " + webinaire.getTitle());
    }

    @FXML
    private void goToAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/AccueilClient.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface d'accueil.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}