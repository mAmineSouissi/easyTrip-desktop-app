package tn.esprit.easytripdesktopapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.AccountType;
import tn.esprit.easytripdesktopapp.services.ServiceUser;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private Button resetButton;

    private final ServiceUser serviceUser = new ServiceUser();



    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        Stage stage;
        Scene scene;

        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter email and password.", Alert.AlertType.ERROR);
            return;
        }
        // Authenticate user
        User user = serviceUser.authenticate(email, password);

        if (user != null) {
            showAlert("Success", "Login Successful!", Alert.AlertType.INFORMATION);

            // Store user in session
            UserSession.createSession(user);


            try {
                FXMLLoader loader;
                switch (AccountType.valueOf(user.getRole())) {
                    case Admin:
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/TablesAdmin.fxml"));
                        break;
                    case Agent:
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"));
                        break;
                    case Client:
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"));
                        break;
                    default:
                        showAlert("Error", "Unknown role. Access denied.", Alert.AlertType.ERROR);
                        return;
                }

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle(user.getRole() + " Dashboard");
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showAlert("Error", "Invalid credentials. Please try again.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    void onClick(ActionEvent event) {
        try {
            // Load the SignUp.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/SignUp.fxml"));
            Parent root = loader.load();  // Load the FXML

            // Get the current stage
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Set the new scene
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();  // Print the error details for debugging
            showAlert("Error", "Failed to load the sign-up page.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    void resetFields(ActionEvent event) {
        emailField.clear();
        passwordField.clear();
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
