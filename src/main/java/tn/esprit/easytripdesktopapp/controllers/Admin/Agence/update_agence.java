package tn.esprit.easytripdesktopapp.controllers.Admin.Agence;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;

import java.io.File;

public class update_agence {

    @FXML
    private TextField nameField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private ImageView imageView;

    private String imageUrl;
    private Agence currentAgence;
    private final ServiceAgence serviceAgence = new ServiceAgence();

    // Méthode pour charger une agence existante pour mise à jour
    public void setAgence(Agence agence) {
        this.currentAgence = agence;
        nameField.setText(agence.getNom());
        addressField.setText(agence.getAddress());
        phoneField.setText(agence.getPhone());
        emailField.setText(agence.getEmail());
        imageUrl = agence.getImage();
        imageView.setImage(new Image(imageUrl)); // Charger l'image existante
    }

    @FXML
    private void updateAgence() {
        // Vérifier si tous les champs sont remplis
        if (nameField.getText().isEmpty() || addressField.getText().isEmpty() || phoneField.getText().isEmpty() || emailField.getText().isEmpty() || imageUrl == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        // Mettre à jour l'agence
        currentAgence.setNom(nameField.getText());
        currentAgence.setAddress(addressField.getText());
        currentAgence.setPhone(phoneField.getText());
        currentAgence.setEmail(emailField.getText());
        currentAgence.setImage(imageUrl);

        // Appeler le service pour mettre à jour l'agence dans la base de données
        serviceAgence.update(currentAgence); // Assurez-vous d'avoir une méthode update dans votre service
        showAlert("Succès", "L'agence a été mise à jour avec succès !", Alert.AlertType.INFORMATION);

        // Fermer la fenêtre après mise à jour
        closeWindow();
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imageUrl = selectedFile.toURI().toString();
            Image img = new Image(imageUrl);
            imageView.setImage(img);
        }
    }

    @FXML
    private void cancel() {
        closeWindow(); // Fermer la fenêtre sans enregistrer
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
