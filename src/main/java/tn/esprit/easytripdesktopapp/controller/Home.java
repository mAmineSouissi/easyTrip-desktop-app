package tn.esprit.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Home {

    @FXML
    void navigateToAdminFeedback(ActionEvent event) {
        navigate(event, "/FeedbackAdmin.fxml");
    }



    @FXML
    void navigateToAdminReclamation(ActionEvent event) {

    }

    @FXML
    void navigateToUserFeedback(ActionEvent event) {

            navigate(event, "/FeedbackUser.fxml");


    }

    @FXML
    void navigateToUserReclamation(ActionEvent event) {

        navigate(event, "/ReclamationUser.fxml");
    }
    @FXML
    private void navigate(ActionEvent event, String fxmlPath) {


            try {
                Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Navigation Error: " + e.getMessage());
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

    }


