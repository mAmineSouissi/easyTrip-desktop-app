package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Reclamation;
import tn.esprit.easytripdesktopapp.services.ServiceReclamation;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;
import java.util.ResourceBundle;

public class ReclamationAdmin {

    @FXML
    private HBox cardContainer;
    @FXML
    private TextField userIdField;
    @FXML
    private Label userIdError;
    @FXML
    private TextField issueField;
    @FXML
    private Label issueError;
    @FXML
    private TextField categoryField;
    @FXML
    private Label categoryError;
    @FXML
    private ComboBox<String> statusComboBox;
    @FXML
    private Label statusError;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Label dateError;
    @FXML
    private TextField searchField;

    private Reclamation selectedReclamation;

    @FXML
    void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList("En attente", "En cours", "Résolue", "Fermée"));
        loadReclamations();
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    @FXML
    void backToHome(ActionEvent event) {
        try {
            ResourceBundle loginBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/TablesAdmin.fxml"), loginBundle);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadReclamations() {
        ServiceReclamation reclamationService = new ServiceReclamation();
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(reclamationService.getAll());
        displayReclamations(reclamations);
    }

    private void displayReclamations(ObservableList<Reclamation> reclamations) {
        cardContainer.getChildren().clear();

        for (Reclamation rec : reclamations) {
            VBox card = new VBox();
            card.getStyleClass().add("card");

            Label userIdLabel = new Label("ID Utilisateur: " + rec.getUserId());
            userIdLabel.getStyleClass().add("card-label");

            Label issueLabel = new Label("Problème: " + rec.getIssue());
            issueLabel.getStyleClass().add("card-label");

            Label categoryLabel = new Label("Catégorie: " + rec.getCategory());
            categoryLabel.getStyleClass().add("card-label");

            Label statusLabel = new Label("Statut: " + rec.getStatus());
            statusLabel.getStyleClass().add("card-label");

            Label dateLabel = new Label("Date: " + rec.getDate());
            dateLabel.getStyleClass().add("card-label");

            card.getChildren().addAll(userIdLabel, issueLabel, categoryLabel, statusLabel, dateLabel);
            card.setOnMouseClicked(event -> handleCardClick(rec));

            cardContainer.getChildren().add(card);
        }
    }

    private void handleCardClick(Reclamation reclamation) {
        selectedReclamation = reclamation;
        fillFormWithReclamation(reclamation);
    }

    private void fillFormWithReclamation(Reclamation reclamation) {
        userIdField.setText(String.valueOf(reclamation.getUserId()));
        issueField.setText(reclamation.getIssue());
        categoryField.setText(reclamation.getCategory());
        statusComboBox.setValue(reclamation.getStatus());
        datePicker.setValue(reclamation.getDate().toLocalDate());
    }

    @FXML
    void deleteReclamation(ActionEvent event) {
        try {
            if (selectedReclamation == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une réclamation à supprimer.");
                return;
            }
            ServiceReclamation reclamationService = new ServiceReclamation();
            reclamationService.delete(selectedReclamation);
            showAlert(Alert.AlertType.CONFIRMATION, "Réclamation supprimée avec succès.");
            loadReclamations();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @FXML
    void handleSearch() {
        String query = searchField.getText().toLowerCase();
        ServiceReclamation reclamationService = new ServiceReclamation();
        ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList(reclamationService.getAll());
        ObservableList<Reclamation> filteredList = allReclamations
                .filtered(reclamation -> reclamation.getIssue().toLowerCase().contains(query) ||
                        reclamation.getCategory().toLowerCase().contains(query) ||
                        reclamation.getStatus().toLowerCase().contains(query) ||
                        String.valueOf(reclamation.getUserId()).contains(query));
        displayReclamations(filteredList);
    }

    @FXML
    void updateReclamation(ActionEvent event) {
        try {
            if (!validateInputs()) {
                return;
            }

            if (selectedReclamation == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une réclamation à mettre à jour.");
                return;
            }

            selectedReclamation.setUserId(Integer.parseInt(userIdField.getText()));
            selectedReclamation.setIssue(issueField.getText());
            selectedReclamation.setCategory(categoryField.getText());
            selectedReclamation.setStatus(statusComboBox.getValue());
            selectedReclamation.setDate(Date.valueOf(datePicker.getValue()));

            ServiceReclamation reclamationService = new ServiceReclamation();
            reclamationService.update(selectedReclamation);

            showAlert(Alert.AlertType.CONFIRMATION, "Réclamation mise à jour avec succès.");
            loadReclamations();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;

        userIdError.setText("");
        issueError.setText("");
        categoryError.setText("");
        statusError.setText("");
        dateError.setText("");

        try {
            int userId = Integer.parseInt(userIdField.getText());
            if (userId <= 0) {
                userIdError.setText("L'ID utilisateur doit être un nombre positif.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            userIdError.setText("L'ID utilisateur doit être un nombre valide.");
            isValid = false;
        }

        if (issueField.getText().trim().isEmpty()) {
            issueError.setText("Le problème ne peut pas être vide.");
            isValid = false;
        }

        if (categoryField.getText().trim().isEmpty()) {
            categoryError.setText("La catégorie ne peut pas être vide.");
            isValid = false;
        }

        if (statusComboBox.getValue() == null || statusComboBox.getValue().trim().isEmpty()) {
            statusError.setText("Le statut doit être sélectionné.");
            isValid = false;
        }

        if (datePicker.getValue() == null) {
            dateError.setText("La date doit être sélectionnée.");
            isValid = false;
        }

        return isValid;
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Gestion des Réclamations");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void sendEmail(ActionEvent event) {
        if (selectedReclamation == null) {
            showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner une réclamation pour envoyer un e-mail.");
            return;
        }

        // Utiliser l'adresse e-mail de test (omsehli@gmail.com)
        String clientEmail = "omsehli@gmail.com"; // Adresse e-mail de test

        // Simuler les informations de l'utilisateur (nom, prénom)
        String clientName = "Om"; // Nom de l'utilisateur
        String clientSurname = "Sehli"; // Prénom de l'utilisateur

        // Envoyer l'e-mail
        String subject = "Votre réclamation a été traitée";
        String body = "Bonjour " + clientName + " " + clientSurname + ",\n\n" +
                "Votre réclamation concernant '" + selectedReclamation.getIssue() + "' a été traitée. " +
                "Le statut est maintenant '" + selectedReclamation.getStatus() + "'.\n\n" +
                "Cordialement,\nL'équipe de support.";

//        try {
//            SendGridUtil.sendEmail(clientEmail, "omsehli@gmail.com", subject, body);
//            showAlert(Alert.AlertType.INFORMATION, "E-mail envoyé avec succès à " + clientEmail);
//        } catch (Exception e) {
//            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'envoi de l'e-mail : " + e.getMessage());
//        }
    }
}