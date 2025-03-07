package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Webinaire;
import tn.esprit.easytripdesktopapp.services.ServiceWebinaire;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdminWebinaireController {

    private final ServiceWebinaire webinaireService = new ServiceWebinaire();
    @FXML
    private ListView<VBox> webinaireList;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> hotelFilter;
    @FXML
    private Button refreshButton;
    @FXML
    private Button backButton;
    private List<Webinaire> webinaires;

    @FXML
    public void initialize() {
        loadWebinaires();
        setupSearch();

    }


    @FXML
    private void loadWebinaires() {
        webinaireList.getItems().clear();
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


        Label hotelLabel = new Label("Hôtel: " + webinaire.getHotel().getName());
        hotelLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #34495e;");

        HBox buttonBox = new HBox(10);
        Button updateButton = new Button("Modifier");
        updateButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5;");
        updateButton.setOnAction(e -> updateWebinaire(webinaire));

        Button deleteButton = new Button("Supprimer");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5;");
        deleteButton.setOnAction(e -> deleteWebinaire(webinaire));

        buttonBox.getChildren().addAll(updateButton, deleteButton);
        listItem.getChildren().addAll(titleLabel, descriptionLabel, dateLabel, hotelLabel, buttonBox);

        return listItem;
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
            loadWebinaires(); // Recharger les webinaires après suppression
        }
    }


    @FXML
    private void updateWebinaire(Webinaire webinaire) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/ModifierWebinaire.fxml"));
            Parent root = loader.load();

            ModifierWebinaireController controller = loader.getController();
            controller.setWebinaire(webinaire, this::loadWebinaires); // Passer la méthode de rafraîchissement

            Stage stage = new Stage();
            stage.setTitle("Modifier un Webinaire");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface Modifier un Webinaire.");
        }
    }

    private void loadWebinaires(Void unused) {
    }


    @FXML
    private void goToAddWebinaire() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/AjouterWebinaire.fxml"));
            Parent root = loader.load();

            AjouterWebinaireController controller = loader.getController();
            controller.setRefreshCallback(this::loadWebinaires);

            Stage stage = new Stage();
            stage.setTitle("Ajouter un Webinaire");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface Ajouter un Webinaire.");
        }
    }


    @FXML
    private void refreshPage() {
        loadWebinaires(); // Recharger les webinaires
    }


    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            webinaireList.getItems().clear();
            webinaires.stream().filter(webinaire -> webinaire.getTitle().toLowerCase().contains(newValue.toLowerCase())).forEach(webinaire -> webinaireList.getItems().add(createWebinaireListItem(webinaire)));
        });
    }


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void goToAccueil(javafx.event.ActionEvent actionEvent) {
        Stage stage;
        try {
            ResourceBundle resourcesBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/TablesAdmin.fxml"), resourcesBundle);
            Parent root = loader.load();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface d'accueil.");
        }
    }
}