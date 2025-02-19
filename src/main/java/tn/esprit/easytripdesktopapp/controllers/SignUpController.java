package tn.esprit.easytripdesktopapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.AccountType;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.services.ServiceUser;

import java.io.IOException;

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
    private Button backArrow;

    @FXML
    private Button backButton;

    @FXML
    private TextField surnameField;

    @FXML
    void onClick(ActionEvent event) {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String password = passwordField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String profilePhoto = profilePhotoField.getText();
        String role = String.valueOf(roleComboBox.getValue());

        User newUser = new User(name, surname, password, email, phone, address, profilePhoto, role);
        ServiceUser serviceUser = new ServiceUser();
        serviceUser.add(newUser);

        if (serviceUser.getAll() != null) {
            showAlert("Success", "User created successfully!");
        } else {
            showAlert("Error", "User creation failed!");
        }

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
        resetFields();
    }

    @FXML
    public void initialize() {
        roleComboBox.setItems(FXCollections.observableArrayList(AccountType.Agent, AccountType.Client));
        roleComboBox.getSelectionModel().select(AccountType.Client);

    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}