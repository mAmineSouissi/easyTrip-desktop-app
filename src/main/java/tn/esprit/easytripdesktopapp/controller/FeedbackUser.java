package tn.esprit.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import tn.esprit.models.Feedback;
import tn.esprit.services.ServiceFeedback;

import java.sql.Date;

public class FeedbackUser {

    private int userId = 1; // Remplacez par l'ID de l'utilisateur connecté

    // Déclarations des éléments FXML
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
    private Label offerIdError;

    @FXML
    private Label ratingError;

    @FXML
    private Label messageError;

    @FXML
    private Label dateError;

    // Initialisation du contrôleur
    @FXML
    void initialize() {
        // Configuration du Spinner pour la notation (1 à 5)
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        ratingSpinner.setValueFactory(valueFactory);

        // Chargement des feedbacks de l'utilisateur
        loadFeedbacks();

        // Gestion de la sélection d'un feedback dans la ListView
        feedbackList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                offerIdField.setText(String.valueOf(newSelection.getOfferId()));
                ratingSpinner.getValueFactory().setValue(newSelection.getRating());
                messageField.setText(newSelection.getMessage());
                datePicker.setValue(newSelection.getDate().toLocalDate());
            }
        });

        // Gestion de la recherche en temps réel
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    // Méthode pour supprimer un feedback
    @FXML
    void deleteFeedback(ActionEvent event) {
        try {
            Feedback selectedFeedback = feedbackList.getSelectionModel().getSelectedItem();
            if (selectedFeedback == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner un feedback à supprimer");
                return;
            }

            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.delete(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback supprimé avec succès");
            loadFeedbacks();
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la suppression du feedback: " + e.getMessage());
        }
    }

    // Méthode pour insérer un nouveau feedback
    @FXML
    void insertFeedback(ActionEvent event) {
        try {
            if (!validateInputs()) {
                return;
            }

            Feedback feedback = new Feedback();
            feedback.setUserId(userId);
            feedback.setOfferId(Integer.parseInt(offerIdField.getText()));
            feedback.setRating(ratingSpinner.getValue());
            feedback.setMessage(messageField.getText());
            feedback.setDate(Date.valueOf(datePicker.getValue()));

            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.add(feedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback ajouté avec succès");
            loadFeedbacks();
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de l'ajout du feedback: " + e.getMessage());
        }
    }

    // Méthode pour mettre à jour un feedback existant
    @FXML
    void updateFeedback(ActionEvent event) {
        try {
            if (!validateInputs()) {
                return;
            }

            Feedback selectedFeedback = feedbackList.getSelectionModel().getSelectedItem();
            if (selectedFeedback == null) {
                showAlert(Alert.AlertType.WARNING, "Veuillez sélectionner un feedback à mettre à jour");
                return;
            }

            selectedFeedback.setUserId(userId);
            selectedFeedback.setOfferId(Integer.parseInt(offerIdField.getText()));
            selectedFeedback.setRating(ratingSpinner.getValue());
            selectedFeedback.setMessage(messageField.getText());
            selectedFeedback.setDate(Date.valueOf(datePicker.getValue()));

            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.update(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback mis à jour avec succès");
            loadFeedbacks();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la mise à jour du feedback: " + e.getMessage());
        }
    }

    // Méthode pour revenir à la page d'accueil
    @FXML
    public void backToHome(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur lors de la navigation: " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Gestion des Feedbacks");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour réinitialiser les champs du formulaire
    private void clearFields() {
        offerIdField.clear();
        ratingSpinner.getValueFactory().setValue(1);
        messageField.clear();
        datePicker.setValue(null);
        offerIdError.setText("");
        ratingError.setText("");
        messageError.setText("");
        dateError.setText("");
    }

    // Méthode pour charger les feedbacks de l'utilisateur
    private void loadFeedbacks() {
        ServiceFeedback feedbackService = new ServiceFeedback();
        ObservableList<Feedback> allFeedbacks = FXCollections.observableArrayList(feedbackService.getAll());
        ObservableList<Feedback> userFeedbacks = allFeedbacks.filtered(feedback -> feedback.getUserId() == userId);
        feedbackList.setItems(userFeedbacks);
    }

    // Méthode pour valider les entrées du formulaire
    private boolean validateInputs() {
        boolean isValid = true;
        offerIdError.setText("");
        ratingError.setText("");
        messageError.setText("");
        dateError.setText("");

        // Validation de l'Offer ID
        try {
            int offerId = Integer.parseInt(offerIdField.getText().trim());
            if (offerId <= 0) {
                offerIdError.setText("L'ID de l'offre doit être un nombre positif");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            offerIdError.setText("L'ID de l'offre doit être un nombre valide");
            isValid = false;
        }

        // Validation du Message
        if (messageField.getText().trim().isEmpty()) {
            messageError.setText("Le champ message ne peut pas être vide");
            isValid = false;
        }

        // Validation de la Date
        if (datePicker.getValue() == null) {
            dateError.setText("La date doit être sélectionnée");
            isValid = false;
        } else if (datePicker.getValue().isAfter(java.time.LocalDate.now())) {
            dateError.setText("La date ne peut pas être dans le futur");
            isValid = false;
        }

        // Validation de la Notation
        if (ratingSpinner.getValue() == null || ratingSpinner.getValue() < 1 || ratingSpinner.getValue() > 5) {
            ratingError.setText("La note doit être entre 1 et 5");
            isValid = false;
        }

        return isValid;
    }

    // Méthode pour gérer la recherche
    @FXML
    void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ServiceFeedback feedbackService = new ServiceFeedback();
        ObservableList<Feedback> allFeedbacks = FXCollections.observableArrayList(feedbackService.getAll());

        ObservableList<Feedback> filteredList = allFeedbacks.filtered(feedback ->
                feedback.getUserId() == userId &&
                        (String.valueOf(feedback.getOfferId()).contains(searchText) ||
                                String.valueOf(feedback.getRating()).contains(searchText) ||
                                feedback.getMessage().toLowerCase().contains(searchText))
        );

        feedbackList.setItems(filteredList);
    }
}