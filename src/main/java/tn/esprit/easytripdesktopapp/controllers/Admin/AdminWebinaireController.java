package tn.esprit.easytripdesktopapp.controllers.Admin;

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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class AdminWebinaireController {

    @FXML
    private FlowPane cardContainer;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> hotelFilter;

    @FXML
    private Button refreshButton;

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
            Button updateButton = new Button("Modifier");
            updateButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-cursor: hand;");


            Button deleteButton = new Button("Supprimer");
            deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-cursor: hand;");
            deleteButton.setOnAction(e -> deleteWebinaire(webinaire));

            buttonBox.getChildren().addAll(updateButton, deleteButton);

            card.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, linkLabel, hotelLabel, buttonBox);

            cardContainer.getChildren().add(card);
        }
    }



    @FXML
    private void deleteWebinaire(Webinaire webinaire) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer ce webinaire ?");
        alert.setContentText("Webinaire sélectionné : " + webinaire.getTitle());
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            webinaireService.delete(webinaire);
            loadWebinaires();
        }
    }

    @FXML
    private void goToAddWebinaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Admin/AjouterWebinaire.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Webinaire");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
            loadWebinaires();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface Ajouter un Webinaire.");
        }
    }

    @FXML
    private void refreshPage() {
        loadWebinaires();
    }

    @FXML
    private void goToAccueil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/agent/Accueil.fxml"));
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