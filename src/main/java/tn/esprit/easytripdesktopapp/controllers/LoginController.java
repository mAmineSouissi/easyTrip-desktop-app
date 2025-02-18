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

        // Validate fields
        if (email.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter email and password.", Alert.AlertType.ERROR);
            return;
        }

        // Authenticate user
        User user = serviceUser.authenticate(email, password);

        if (user != null) {
            showAlert("Success", "Login Successful!", Alert.AlertType.INFORMATION);

            try {
                switch (AccountType.valueOf(user.getRole())) {
                    case Admin:
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Dashboard.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(loader.load());
                        stage.setScene(scene);
                        stage.setTitle("Admin Dashboard");
                        stage.show();
                        break;
                    case Agent:
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(loader.load());
                        stage.setScene(scene);
                        stage.setTitle("Agent Dashboard");
                        stage.show();
                        break;
                    case Client:
                        loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(loader.load());
                        stage.setScene(scene);
                        stage.setTitle("Client Dashboard");
                        stage.show();
                        break;
                    default:
                        showAlert("Error", "Unknown role. Access denied.", Alert.AlertType.ERROR);
                        break;
                };
            }catch (Exception e) {
                e.printStackTrace();
            }


            // Navigate to the correct dashboard based on role

        } else {
            showAlert("Error", "Invalid credentials. Please try again.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void onClick(ActionEvent event) {
         Stage stage;
         Scene scene;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/SignUp.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("SignUp");
            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
            showAlert("Error", "Failed to load sign-up page.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void resetFields(ActionEvent event) {
        emailField.clear();
        passwordField.clear();
    }

    /*private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Dashboard.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Dashboard Admin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard.", Alert.AlertType.ERROR);
        }
    }*/

   /* private void loadDashboard(User user,String title,ActionEvent event) {
        Stage stage;
        Scene scene;
        try {
            switch (AccountType.valueOf(user.getRole())) {
                case Admin:
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Admin/Dashboard.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.setTitle(title);
                    stage.show();
                    break;
                case Agent:
                    loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Agent/Dashboard.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.setTitle(title);
                    stage.show();
                    break;
                case Client:
                    loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.setTitle(title);
                    stage.show();
                    break;
                default:
                    showAlert("Error", "Unknown role. Access denied.", Alert.AlertType.ERROR);
                    break;
            };
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard.", Alert.AlertType.ERROR);
        }
    }*/

  /*  private void loadSignUpPage() {
         Stage stage;
         Scene scene;
         Parent root =

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/SignUp.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("SignUp Admin");
            stage.show();
            
        } catch (IOException e) {
            System.out.println(e.getMessage());
            showAlert("Error", "Failed to load sign-up page.", Alert.AlertType.ERROR);
        }
    }*/

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
