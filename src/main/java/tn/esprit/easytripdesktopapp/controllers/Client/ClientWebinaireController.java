package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import tn.esprit.easytripdesktopapp.models.Webinaire;
import tn.esprit.easytripdesktopapp.services.ServiceWebinaire;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ClientWebinaireController {

    @FXML
    private ListView<VBox> webinaireList;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> hotelFilter;

    @FXML
    private Button backButton;

    private final ServiceWebinaire webinaireService = new ServiceWebinaire();
    private List<Webinaire> webinaires;

    private Timeline timeline; // Timeline pour rafraîchir automatiquement la page

    @FXML
    public void initialize() {
        loadWebinaires(); // Charger les webinaires au démarrage
        setupSearch(); // Configurer la recherche dynamique

        // Configurer la mise à jour automatique de la page
        setupAutoRefresh();
    }

    /**
     * Configure la mise à jour automatique de la page.
     */
    private void setupAutoRefresh() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> loadWebinaires())); // Rafraîchir toutes les 10 secondes
        timeline.setCycleCount(Timeline.INDEFINITE); // Répéter indéfiniment
        timeline.play(); // Démarrer la timeline
    }

    @FXML
    private void loadWebinaires() {
        webinaireList.getItems().clear();
        webinaireService.deleteExpiredWebinaires(); // Supprimer les webinaires expirés
        webinaires = webinaireService.getAll();

        for (Webinaire webinaire : webinaires) {
            VBox listItem = createWebinaireListItem(webinaire);
            webinaireList.getItems().add(listItem);
        }
    }

    private VBox createWebinaireListItem(Webinaire webinaire) {
        VBox listItem = new VBox(10);
        listItem.setStyle("-fx-background-color: #ffffff; -fx-padding: 15; -fx-border-radius: 10; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 10, 0, 0);");

        Label titleLabel = new Label("Titre: " + webinaire.getTitle());
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        Label descriptionLabel = new Label("Description: " + webinaire.getDescription());
        descriptionLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label dateLabel = new Label("Date: " + webinaire.getDebutDateTime() + " - " + webinaire.getFinitDateTime());
        dateLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label linkLabel = new Label("Lien: " + webinaire.getLink());
        linkLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        Label hotelLabel = new Label("Hôtel: " + webinaire.getHotel().getName());
        hotelLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        HBox buttonBox = new HBox(10);
        Button joinButton = new Button("Rejoindre");
        joinButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Vérifier si le webinaire est en cours
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(webinaire.getDebutDateTime()) && now.isBefore(webinaire.getFinitDateTime())) {
            joinButton.setOnAction(e -> joinWebinaire(webinaire));
        } else {
            joinButton.setDisable(true);
            joinButton.setStyle("-fx-background-color: #cccccc; -fx-text-fill: #666666; -fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5;");
        }

        buttonBox.getChildren().add(joinButton);

        listItem.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, linkLabel, hotelLabel, buttonBox);

        return listItem;
    }

    @FXML
    private void joinWebinaire(Webinaire webinaire) {
        try {
            // Ouvrir le lien du webinar dans le navigateur par défaut
            java.awt.Desktop.getDesktop().browse(new java.net.URI(webinaire.getLink()));
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d'ouvrir le lien du webinaire.");
        }
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

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            webinaireList.getItems().clear();
            webinaires.stream()
                    .filter(webinaire -> webinaire.getTitle().toLowerCase().contains(newValue.toLowerCase()))
                    .forEach(webinaire -> webinaireList.getItems().add(createWebinaireListItem(webinaire)));
        });
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}