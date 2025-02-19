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
import javafx.scene.layout.VBox;
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

    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            if (userId <= 0) {
                errors.append("User ID must be a positive number\n");
            }
        } catch (NumberFormatException e) {
            errors.append("User ID must be a valid number\n");
        }

        try {
            int offerId = Integer.parseInt(offerIdField.getText().trim());
            if (offerId <= 0) {
                errors.append("Offer ID must be a positive number\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Offer ID must be a valid number\n");
        }

        if (messageField.getText().trim().isEmpty()) {
            errors.append("Message field cannot be empty\n");
        }

        if (datePicker.getValue() == null) {
            errors.append("Date must be selected\n");
        } else if (datePicker.getValue().isAfter(java.time.LocalDate.now())) {
            errors.append("Date cannot be in the future\n");
        }

        if (ratingSpinner.getValue() == null || ratingSpinner.getValue() < 1 || ratingSpinner.getValue() > 5) {
            errors.append("Rating must be between 1 and 5\n");
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, errors.toString());
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        ratingSpinner.setValueFactory(valueFactory);
        loadFeedbacks();

        feedbackList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                userIdField.setText(String.valueOf(newSelection.getUserId()));
                offerIdField.setText(String.valueOf(newSelection.getOfferId()));
                ratingSpinner.getValueFactory().setValue(newSelection.getRating());
                messageField.setText(newSelection.getMessage());
                datePicker.setValue(newSelection.getDate().toLocalDate());
            }
        });

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
            showAlert(Alert.AlertType.ERROR, "Error navigating back: " + e.getMessage());
        }


    }

    @FXML
    void deleteFeedback(ActionEvent event) {
        try {
            Feedback selectedFeedback = feedbackList.getSelectionModel().getSelectedItem();
            if (selectedFeedback == null) {
                showAlert(Alert.AlertType.WARNING, "Please select a feedback to delete");
                return;
            }
            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.delete(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback deleted successfully");
            loadFeedbacks();
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error deleting feedback: " + e.getMessage());
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
                showAlert(Alert.AlertType.WARNING, "Please select a feedback to update");
                return;
            }
            selectedFeedback.setUserId(Integer.parseInt(userIdField.getText()));
            selectedFeedback.setOfferId(Integer.parseInt(offerIdField.getText()));
            selectedFeedback.setRating(ratingSpinner.getValue());
            selectedFeedback.setMessage(messageField.getText());
            selectedFeedback.setDate(Date.valueOf(datePicker.getValue()));

            ServiceFeedback feedbackService = new ServiceFeedback();
            feedbackService.update(selectedFeedback);

            showAlert(Alert.AlertType.CONFIRMATION, "Feedback updated successfully");
            loadFeedbacks();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error updating feedback: " + e.getMessage());
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
        alert.setTitle("Feedback Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

}




