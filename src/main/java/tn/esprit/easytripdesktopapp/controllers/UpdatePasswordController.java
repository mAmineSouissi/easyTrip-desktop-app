package tn.esprit.easytripdesktopapp.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.services.ServiceResetPassword;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdatePasswordController {
    @FXML
    private MFXTextField verificationCodeField;

    @FXML
    private MFXPasswordField newPasswordField;

    @FXML
    private MFXPasswordField confirmPasswordField;

    @FXML
    private MFXButton resetPasswordButton;

    @FXML
    private Label statusLabel;

    private final ServiceResetPassword passwordResetService = new ServiceResetPassword();

    private String userEmail;

    public void setUserEmail(String email) {
        this.userEmail = email;
    }

    @FXML
    void initialize() {
        // Initialize UI components if needed
    }

    @FXML
    void onResetPasswordButtonClick(ActionEvent event) {
        String verificationCode = verificationCodeField.getText().trim();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate verification code
        if (verificationCode.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter the verification code.");
            return;
        }

        // Verify the code
        if (!passwordResetService.verifyCode(userEmail, verificationCode)) {
            statusLabel.setText("Invalid verification code. Please try again.");
            return;
        }

        // Validate passwords
        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please enter and confirm your new password.");
            return;
        }

        // Check if passwords match
        if (!passwordResetService.passwordsMatch(newPassword, confirmPassword)) {
            statusLabel.setText("Passwords do not match. Please try again.");
            return;
        }

        // Reset the password
        boolean passwordReset = passwordResetService.resetPassword(userEmail, newPassword);

        if (passwordReset) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Your password has been reset successfully.");

            // Navigate back to login screen
            try {
                ResourceBundle loginBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Login.fxml"), loginBundle);
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Login Screen");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            statusLabel.setText("Failed to reset password. Please try again later.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void onBackButtonClick(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/ResetPassword.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) resetPasswordButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Forgot Password");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

