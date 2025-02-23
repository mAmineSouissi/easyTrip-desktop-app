package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Feedback;
import tn.esprit.services.ServiceFeedback;

import java.sql.Date;

public class FeedbackAdmin {

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<Feedback> feedbackList;

    @FXML
    private TextField messageField;

    @FXML
    private TextField offerIdField;

    @FXML
    private Spinner<Integer> ratingSpinner;

    @FXML
    private TextField searchField;

    @FXML
    private TextField userIdField;

    // Labels pour les messages d'erreur
    @FXML
    private Label userIdError;

    @FXML
    private Label offerIdError;

    @FXML
    private Label ratingError;

    @FXML
    private Label messageError;

    @FXML
    private Label dateError;

    @FXML
    private Label searchError;

    // Méthode pour valider les entrées
    private boolean validateInputs() {
        boolean isValid = true;

        // Réinitialiser les messages d'erreur
        userIdError.setText("");
        offerIdError.setText("");
        ratingError.setText("");
        messageError.setText("");
        dateError.setText("");

        // Validation de l'ID utilisateur
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            if (userId <= 0) {
                userIdError.setText("L'ID utilisateur doit être un nombre positif.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            userIdError.setText("L'ID utilisateur doit être un nombre valide.");
            isValid = false;
        }

        // Validation de l'ID de l'offre
        try {
            int offerId = Integer.parseInt(offerIdField.getText().trim());
            if (offerId <= 0) {
                offerIdError.setText("L'ID de l'offre doit être un nombre positif.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            offerIdError.setText("L'ID de l'offre doit être un nombre valide.");
            isValid = false;
        }

        // Validation du message
        if (messageField.getText().trim().isEmpty()) {
            messageError.setText("Le champ message ne peut pas être vide.");
            isValid = false;
        }

        // Validation de la date
        if (datePicker.getValue() == null) {
            dateError.setText("La date doit être sélectionnée.");
            isValid = false;
        } else if (datePicker.getValue().isAfter(java.time.LocalDate.now())) {
            dateError.setText("La date ne peut pas être dans le futur.");
            isValid = false;
        }

        // Validation de la note
        if (ratingSpinner.getValue() == null || ratingSpinner.getValue() < 1 || ratingSpinner.getValue() > 5) {
            ratingError.setText("La note doit être entre 1 et 5.");
            isValid = false;
        }

        return isValid;
    }

    @FXML
    void initialize() {
        // Configuration du Spinner pour la note (1 à 5)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        ratingSpinner.setValueFactory(valueFactory);

        // Chargement des feedbacks
        loadFeedbacks();

        // Gestion de la sélection d'un feedback dans la ListView
        feedbackList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                userIdField.setText(String.valueOf(newSelection.getUserId()));
                offerIdField.setText(String.valueOf(newSelection.getOfferId()));
                ratingSpinner.getValueFactory().setValue(newSelection.getRating());
                messageField.setText(newSelection.getMessage());
                datePicker.setValue(newSelection.getDate().toLocalDate());
            }
        });

        // Gestion de la recherche en temps réel
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    @FXML
    void backToHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la navigation : " + e.getMessage());
        }
    }

    @FXML
    void deleteFeedback(ActionEvent event) {
        try {
            Feedback selectedFeedback = feedbackList.getSelectionModel().getSelectedItem();
            if (selectedFeedback == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner un feedback à supprimer.");
                return;
            }
            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.delete(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback supprimé avec succès.");
            loadFeedbacks();
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression du feedback : " + e.getMessage());
        }
    }

    @FXML
    void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ServiceFeedback feedbackService = new ServiceFeedback();
        ObservableList<Feedback> allFeedbacks = FXCollections.observableArrayList(feedbackService.getAll());
        ObservableList<Feedback> filteredList = allFeedbacks
                .filtered(feedback -> String.valueOf(feedback.getUserId()).contains(searchText) ||
                        String.valueOf(feedback.getOfferId()).contains(searchText) ||
                        String.valueOf(feedback.getRating()).contains(searchText) ||
                        feedback.getMessage().toLowerCase().contains(searchText));
        feedbackList.setItems(filteredList);
    }

    @FXML
    void updateFeedback(ActionEvent event) {
        try {
            if (!validateInputs()) {
                return;
            }
            Feedback selectedFeedback = feedbackList.getSelectionModel().getSelectedItem();
            if (selectedFeedback == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner un feedback à mettre à jour.");
                return;
            }
            selectedFeedback.setUserId(Integer.parseInt(userIdField.getText()));
            selectedFeedback.setOfferId(Integer.parseInt(offerIdField.getText()));
            selectedFeedback.setRating(ratingSpinner.getValue());
            selectedFeedback.setMessage(messageField.getText());
            selectedFeedback.setDate(Date.valueOf(datePicker.getValue()));

            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.update(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback mis à jour avec succès.");
            loadFeedbacks();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour du feedback : " + e.getMessage());
        }
    }

    private void loadFeedbacks() {
        ServiceFeedback feedbackService = new ServiceFeedback();
        ObservableList<Feedback> feedbacks = FXCollections.observableArrayList(feedbackService.getAll());
        feedbackList.setItems(feedbacks);
    }

    private void clearFields() {
        userIdField.clear();
        offerIdField.clear();
        ratingSpinner.getValueFactory().setValue(1);
        messageField.clear();
        datePicker.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Gestion des Feedbacks");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}