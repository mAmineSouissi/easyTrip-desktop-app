package tn.esprit.easytripdesktopapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import tn.esprit.easytripdesktopapp.models.AccountType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.services.ServiceUser;

public class SignUpController {

    @FXML
    private TextArea addressField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField nameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField profilePhotoField;

    @FXML
    private ComboBox<AccountType> roleComboBox;

    @FXML
    private Button signUpButton;

    @FXML
    private TextField surnameField;

    @FXML
    void onClick(ActionEvent event) {
        // Get the values from the form fields
        String name = nameField.getText();
        String surname = surnameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String profilePhoto = profilePhotoField.getText();

        // Get the selected role from the ComboBox
        String role = String.valueOf(roleComboBox.getValue());

        // Create a new User object using the input values
        User newUser = new User(name, surname, password, email, phone, address, profilePhoto, role);

        // Create an instance of ServiceUser to add the user to the database
        ServiceUser serviceUser = new ServiceUser();
        serviceUser.add(newUser);

        if (serviceUser.getAll() != null)
            showAlert("Success", "User created successfully!");
        else
            showAlert("Error", "User creation failed!");

        resetFields();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void resetFields() {
        // Reset all the form fields
        nameField.clear();
        surnameField.clear();
        passwordField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        profilePhotoField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }


    @FXML
    void reset(ActionEvent event) {
        nameField.clear();
        surnameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        profilePhotoField.clear();
        passwordField.clear();
        roleComboBox.getSelectionModel().clearSelection();
    }

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList(
                AccountType.Agent, AccountType.Client
        ));
        roleComboBox.getSelectionModel().select(AccountType.Client);
    }
}
