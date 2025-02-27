package tn.esprit.easytripdesktopapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.AccountType;
import tn.esprit.easytripdesktopapp.services.ServiceUser;
import tn.esprit.easytripdesktopapp.models.User;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController {

    private final ServiceUser serviceUser = new ServiceUser();
    public Label greetingLabel;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Button langEnglishButton;
    @FXML
    private Button langFrenchButton;
    private ResourceBundle bundle;

    public void initialize() {
        bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
        updateTexts();
    }

    private void updateTexts() {
        greetingLabel.setText(bundle.getString("greeting"));
        loginButton.setText(bundle.getString("login"));
        signUpButton.setText(bundle.getString("signup"));
        emailField.setPromptText(bundle.getString("email"));
        passwordField.setPromptText(bundle.getString("password"));
    }

    @FXML
    void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        Stage stage;
        Scene scene;

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(bundle.getString("error"), bundle.getString("enter_credentials"), Alert.AlertType.ERROR);
            return;
        }

        User user = serviceUser.authenticate(email, password);
        if (user != null) {
            showAlert(bundle.getString("success"), bundle.getString("login_success"), Alert.AlertType.INFORMATION);
            UserSession.createSession(user);

            try {
                FXMLLoader loader;
                switch (AccountType.valueOf(user.getRole())) {
                    case Admin:
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/TablesAdmin.fxml"), bundle);
                        break;
                    case Agent:
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"), bundle);
                        break;
                    case Client:
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"), bundle);
                        break;
                    default:
                        showAlert(bundle.getString("error"), bundle.getString("unknown_role"), Alert.AlertType.ERROR);
                        return;
                }

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(loader.load());
                stage.setScene(scene);
                stage.setTitle(bundle.getString(user.getRole().toLowerCase() + "_dashboard"));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            showAlert(bundle.getString("error"), bundle.getString("invalid_credentials"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onClick(ActionEvent event) {
        try {
            ResourceBundle loginBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/SignUp.fxml"), loginBundle);
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(bundle.getString("signup"));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(bundle.getString("error"), bundle.getString("load_signup_error"), Alert.AlertType.ERROR);
        }
    }

    @FXML
    void resetFields(ActionEvent event) {
        emailField.clear();
        passwordField.clear();
    }

    @FXML
    void switchToEnglish() {
        switchLanguage(new Locale("en"));
    }

    @FXML
    void switchToFrench() {
        switchLanguage(new Locale("fr"));
    }

    private void switchLanguage(Locale locale) {
        Locale.setDefault(locale);
        bundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", locale);
        updateTexts();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void navigateToResetPassword(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/ResetPassword.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Reset Password");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading ResetPassword.fxml");
        }
    }
}
