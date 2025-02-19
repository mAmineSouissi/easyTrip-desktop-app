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
import java.sql.Date;
import tn.esprit.models.Feedback;
import tn.esprit.services.ServiceFeedback;



public class FeedbackUser {

        private int userId = 1;

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
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5, 1);
        ratingSpinner.setValueFactory(valueFactory);
        loadFeedbacks();

        feedbackList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                offerIdField.setText(String.valueOf(newSelection.getOfferId()));
                ratingSpinner.getValueFactory().setValue(newSelection.getRating());
                messageField.setText(newSelection.getMessage());
                datePicker.setValue(newSelection.getDate().toLocalDate());
            }
        });

        // Add search functionality
        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
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
            ObservableList<Feedback> filteredList = allFeedbacks.filtered(feedback -> feedback.getUserId() == userId &&
                    (String.valueOf(feedback.getOfferId()).contains(searchText) ||
                            String.valueOf(feedback.getRating()).contains(searchText) ||
                            feedback.getMessage().toLowerCase().contains(searchText)));
            feedbackList.setItems(filteredList);
        }



        @FXML
        void insertFeedback(ActionEvent event) {
            try {
                Feedback feedback = new Feedback();
                feedback.setUserId(userId);
                feedback.setOfferId(Integer.parseInt(offerIdField.getText()));
                feedback.setRating(ratingSpinner.getValue());
                feedback.setMessage(messageField.getText());
                feedback.setDate(Date.valueOf(datePicker.getValue()));

                ServiceFeedback feedbackService = new ServiceFeedback();
                feedbackService.add(feedback);

                showAlert(Alert.AlertType.CONFIRMATION, "Feedback inserted successfully");
                loadFeedbacks();
                clearFields();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error inserting feedback: " + e.getMessage());
            }

        }

        @FXML
        void updateFeedback(ActionEvent event) {
            try {
                Feedback selectedFeedback = feedbackList.getSelectionModel().getSelectedItem();
                if (selectedFeedback == null) {
                    showAlert(Alert.AlertType.WARNING, "Please select a feedback to update");
                    return;
                }
                selectedFeedback.setUserId(userId);
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

    @FXML
    public void backToHome(ActionEvent event) {
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
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle("Navigation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        offerIdField.clear();
        ratingSpinner.getValueFactory().setValue(1);
        messageField.clear();
        datePicker.setValue(null);
    }

    private void loadFeedbacks() {
        ServiceFeedback feedbackService = new ServiceFeedback();
        ObservableList<Feedback> allFeedbacks = FXCollections.observableArrayList(feedbackService.getAll());
        ObservableList<Feedback> userFeedbacks = allFeedbacks.filtered(feed -> feed.getUserId() == userId);
        feedbackList.setItems(userFeedbacks);
    }



}


