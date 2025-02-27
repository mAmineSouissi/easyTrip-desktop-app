package tn.esprit.easytripdesktopapp.controllers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.services.ServiceResetPassword;

public class ResetPasswordController {

    private static final Logger LOGGER = Logger.getLogger(ResetPasswordController.class.getName());

    @FXML
    private TextField emailField;

    @FXML
    private Button sendButton;

    @FXML
    public void initialize() {
        // You can add event listeners or validation logic here if needed
    }

    @FXML
    public void sendCode(ActionEvent actionEvent) {
        String email = emailField.getText().trim();

        if (email.isEmpty()) {
            LOGGER.warning("Please enter your email.");
            return;
        }

        ServiceResetPassword serviceResetPassword = new ServiceResetPassword();
        if (serviceResetPassword.sendVerificationCode(email)) {
            LOGGER.info("Sending reset link to: " + email);
            navigateToUpdatePasswordPage(email);
        } else {
            LOGGER.warning("Failed to send reset code. Please try again.");
        }
    }

    private void navigateToUpdatePasswordPage(String emailValue) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/UpdatePassword.fxml"));
            Parent root = loader.load();

            // Pass the email to the UpdatePasswordController
            UpdatePasswordController controller = loader.getController();
            controller.setUserEmail(emailValue);

            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Update Password");
            stage.show();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error loading UpdatePassword.fxml", e);
        }
    }
}
