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
import tn.esprit.models.Reclamation;
import tn.esprit.services.ServiceReclamation;

public class ReclamationAdmin {

    @FXML
    private TextField categoryField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField issueField;

    @FXML
    private ListView<Reclamation> reclamationList;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> statusComboBox;

    @FXML
    private TextField userIdField;

    @FXML
    void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Resolved", "Closed"));
        loadReclamations();

        reclamationList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                userIdField.setText(String.valueOf(newSelection.getUserId()));
                issueField.setText(newSelection.getIssue());
                categoryField.setText(newSelection.getCategory());
                statusComboBox.setValue(newSelection.getStatus());
                datePicker.setValue(newSelection.getDate().toLocalDate());
            }
        });

        // Add search functionality
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

    private void loadReclamations() {
        ServiceReclamation reclamationService = new ServiceReclamation();
        ObservableList<Reclamation> reclamations = FXCollections.observableArrayList(reclamationService.getAll());
        reclamationList.setItems(reclamations);
    }

    private void clearFields() {
        userIdField.clear();
        issueField.clear();
        categoryField.clear();
        statusComboBox.setValue(null);
        datePicker.setValue(null);
    }

    private void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Reclamation Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    void deleteReclamation(ActionEvent event) {

        try {
            Reclamation selectedReclamation = reclamationList.getSelectionModel().getSelectedItem();
            if (selectedReclamation == null) {
                showAlert(Alert.AlertType.WARNING, "Please select a reclamation to delete");
                return;
            }
            ServiceReclamation reclamationService = new ServiceReclamation();
            reclamationService.delete(selectedReclamation);
            showAlert(Alert.AlertType.CONFIRMATION, "Reclamation deleted successfully");
            loadReclamations();
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error deleting reclamation: " + e.getMessage());
        }

    }

    @FXML
    void handleSearch() {

        String searchText = searchField.getText().toLowerCase();
        ServiceReclamation reclamationService = new ServiceReclamation();
        ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList(reclamationService.getAll());
        ObservableList<Reclamation> filteredList = allReclamations
                .filtered(reclamation -> reclamation.getIssue().toLowerCase().contains(searchText) ||
                        reclamation.getCategory().toLowerCase().contains(searchText) ||
                        reclamation.getStatus().toLowerCase().contains(searchText) ||
                        String.valueOf(reclamation.getUserId()).contains(searchText));
        reclamationList.setItems(filteredList);
    }


    @FXML
    void updateReclamation(ActionEvent event) {
        try {

            if (!validateInputs()) {
                return;
            }

            Reclamation selectedReclamation = reclamationList.getSelectionModel().getSelectedItem();
            if (selectedReclamation == null) {
                showAlert(Alert.AlertType.WARNING, "Please select a reclamation to update");
                return;
            }
            selectedReclamation.setStatus(statusComboBox.getValue());
            ServiceReclamation reclamationService = new ServiceReclamation();
            reclamationService.update(selectedReclamation);
            showAlert(Alert.AlertType.CONFIRMATION, "Reclamation updated successfully");
            loadReclamations();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error updating reclamation: " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        if (statusComboBox.getValue() == null || statusComboBox.getValue().trim().isEmpty()) {
            errors.append("Status must be selected\n");
        }

        try {
            if (!userIdField.getText().trim().isEmpty()) {
                int userId = Integer.parseInt(userIdField.getText());
                if (userId <= 0) {
                    errors.append("User ID must be a positive number\n");
                }
            }
        } catch (NumberFormatException e) {
            errors.append("User ID must be a valid number\n");
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, errors.toString());
            return false;
        }
        return true;
    }




    }


