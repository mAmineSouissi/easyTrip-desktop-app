package tn.esprit.controller;

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
import javafx.stage.Stage;
import tn.esprit.models.Reclamation;
import tn.esprit.services.ServiceReclamation;
import java.sql.Date;

public class ReclamationUser {

    private int userId = 1;

    @FXML
    private TextField categoryField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ListView<Reclamation> reclamationList;

    @FXML
    private TextField issueField;

    @FXML
    private TextField searchField;

    @FXML
    private ComboBox<String> statusComboBox;

    private boolean validateInputs() {
        StringBuilder errors = new StringBuilder();

        if (issueField.getText().trim().isEmpty()) {
            errors.append("Issue field cannot be empty\n");
        }
        if (categoryField.getText().trim().isEmpty()) {
            errors.append("Category field cannot be empty\n");
        }
        if (datePicker.getValue() == null) {
            errors.append("Date must be selected\n");
        } else if (datePicker.getValue().isAfter(java.time.LocalDate.now())) {
            errors.append("Date cannot be in the future\n");
        }

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, errors.toString());
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
        statusComboBox.setItems(FXCollections.observableArrayList("Pending", "In Progress", "Resolved", "Closed"));
        loadReclamations();

        reclamationList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                issueField.setText(newSelection.getIssue());
                categoryField.setText(newSelection.getCategory());
                statusComboBox.setValue(newSelection.getStatus());
                datePicker.setValue(newSelection.getDate().toLocalDate());
            }
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> handleSearch());
    }

    private void loadReclamations() {
        ServiceReclamation reclamationService = new ServiceReclamation();
        ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList(reclamationService.getAll());
        ObservableList<Reclamation> userReclamations = allReclamations.filtered(rec -> rec.getUserId() == userId);
        reclamationList.setItems(userReclamations);
    }

    private void clearFields() {
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
    void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        ServiceReclamation reclamationService = new ServiceReclamation();
        ObservableList<Reclamation> allReclamations = FXCollections.observableArrayList(reclamationService.getAll());
        ObservableList<Reclamation> filteredList = allReclamations
                .filtered(reclamation -> reclamation.getUserId() == userId &&
                        (reclamation.getIssue().toLowerCase().contains(searchText) ||
                                reclamation.getCategory().toLowerCase().contains(searchText) ||
                                reclamation.getStatus().toLowerCase().contains(searchText)));
        reclamationList.setItems(filteredList);

    }

    @FXML
    void submitReclamation(ActionEvent event) {

        try {


                if (!validateInputs()) {
                    return;
                }

            Reclamation reclamation = new Reclamation();
            reclamation.setUserId(userId);
            reclamation.setIssue(issueField.getText());
            reclamation.setCategory(categoryField.getText());
            reclamation.setStatus("Pending");
            reclamation.setDate(Date.valueOf(datePicker.getValue()));

            ServiceReclamation reclamationService = new ServiceReclamation();
            reclamationService.add(reclamation);

            showAlert(Alert.AlertType.CONFIRMATION, "Reclamation submitted successfully");
            loadReclamations();
            clearFields();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error submitting reclamation: " + e.getMessage());
        }




    }

}
