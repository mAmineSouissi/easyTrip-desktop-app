package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.AccountType;
import tn.esprit.easytripdesktopapp.models.Reclamation;
import tn.esprit.easytripdesktopapp.services.ServiceReclamation;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.sql.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReclamationUser {


    UserSession session=UserSession.getInstance();

    private int userId = session.getUser().getId();

    @FXML
    private TextField categoryField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private HBox cardContainer; // Conteneur horizontal pour les cartes

    @FXML
    private TextField issueField;

    @FXML
    private TextField searchField;

    @FXML
    private Label issueError; // Message d'erreur pour le champ "Problème"

    @FXML
    private Label categoryError; // Message d'erreur pour le champ "Catégorie"

    @FXML
    private Label dateError; // Message d'erreur pour le champ "Date"

    private ServiceReclamation reclamationService = new ServiceReclamation();
    private ResourceBundle bundle;

    @FXML
    void initialize() {
        bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
        loadReclamations();

        // Gestion de la recherche en temps réel
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    private void loadReclamations() {
        ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList(reclamationService.getAll());
        ObservableList<Reclamation> userReclamations = allReclamations.filtered(rec -> rec.getUserId() == userId);
        displayReclamations(userReclamations);
    }

    private void displayReclamations(ObservableList<Reclamation> reclamations) {
        cardContainer.getChildren().clear();
        for (Reclamation reclamation : reclamations) {
            cardContainer.getChildren().add(createReclamationCard(reclamation));
        }
    }

    private HBox createReclamationCard(Reclamation reclamation) {
        HBox card = new HBox(10);
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");

        Label issueLabel = new Label("Problème: " + reclamation.getIssue());
        Label categoryLabel = new Label("Catégorie: " + reclamation.getCategory());
        Label statusLabel = new Label("Statut: " + reclamation.getStatus());
        Label dateLabel = new Label("Date: " + reclamation.getDate().toString());

        VBox labelsVBox = new VBox(5, issueLabel, categoryLabel, statusLabel, dateLabel);
        card.getChildren().add(labelsVBox);

        return card;
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // Réinitialiser les messages d'erreur
        issueError.setText("");
        categoryError.setText("");
        dateError.setText("");

        // Validation du champ "Problème"
        if (issueField.getText().trim().isEmpty()) {
            issueError.setText("Le champ Problème ne peut pas être vide.");
            isValid = false;
        }

        // Validation du champ "Catégorie"
        if (categoryField.getText().trim().isEmpty()) {
            categoryError.setText("Le champ Catégorie ne peut pas être vide.");
            isValid = false;
        }

        // Validation du champ "Date"
        if (datePicker.getValue() == null) {
            dateError.setText("La date doit être sélectionnée.");
            isValid = false;
        } else if (datePicker.getValue().isAfter(java.time.LocalDate.now())) {
            dateError.setText("La date ne peut pas être dans le futur.");
            isValid = false;
        }

        return isValid;
    }

    @FXML
    void backToHome(ActionEvent event) {
        Stage stage;
        Scene scene;
        try {
            FXMLLoader loader;
            switch (AccountType.valueOf(session.getUser().getRole())) {
                case Admin:
                    loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/TablesAdmin.fxml"), bundle);
                    break;
                case Agent:
                    loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"), bundle);
                    break;
                case Client:
                    loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"), bundle);
                    break;
                default:
                    showAlert(bundle.getString("error"), bundle.getString("unknown_role"), Alert.AlertType.ERROR);
                    return;
            }

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle(bundle.getString(session.getUser().getRole().toLowerCase() + "_dashboard"));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList(reclamationService.getAll());
        ObservableList<Reclamation> filteredList = allReclamations
                .filtered(reclamation -> reclamation.getUserId() == userId &&
                        (reclamation.getIssue().toLowerCase().contains(searchText) ||
                                reclamation.getCategory().toLowerCase().contains(searchText) ||
                                reclamation.getStatus().toLowerCase().contains(searchText)));
        displayReclamations(filteredList);
    }

    @FXML
    void submitReclamation(ActionEvent event) {
        if (!validateInputs()) {
            return; // Arrêter la soumission si la validation échoue
        }

        try {
            Reclamation reclamation = new Reclamation();
            reclamation.setUserId(userId);
            reclamation.setIssue(issueField.getText());
            reclamation.setCategory(categoryField.getText());
            reclamation.setStatus("En attente"); // Statut par défaut
            reclamation.setDate(Date.valueOf(datePicker.getValue()));

            reclamationService.add(reclamation);
            showAlert2(Alert.AlertType.CONFIRMATION, "Réclamation soumise avec succès");
            loadReclamations();
            clearFields();
        } catch (Exception e) {
            showAlert2(Alert.AlertType.ERROR, "Erreur lors de la soumission de la réclamation : " + e.getMessage());
        }
    }

    private void clearFields() {
        issueField.clear();
        categoryField.clear();
        datePicker.setValue(null);
        issueError.setText("");
        categoryError.setText("");
        dateError.setText("");
    }

    private void showAlert2(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Gestion des réclamations");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}