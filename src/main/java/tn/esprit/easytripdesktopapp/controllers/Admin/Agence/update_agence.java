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
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

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
    private static final String API_KEY = "02a8cb89e2a0961ee3fbcf04c7d962880b91e1cd"; // Remplacez par votre clé API

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

        // Vérification du format de l'email
        if (!emailField.getText().matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            showAlert("Erreur", "L'email doit être un @gmail.com", Alert.AlertType.ERROR);
            return;
        }

        // Vérification de l'existence de l'email via API
        if (!isEmailValid(emailField.getText())) {
            showAlert("Erreur", "L'email n'existe pas ou n'est pas valide !", Alert.AlertType.ERROR);
            return;
        }

        // Vérification du numéro de téléphone (doit contenir exactement 8 chiffres)
        if (!phoneField.getText().matches("\\d{8}")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !", Alert.AlertType.ERROR);
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

    private boolean isEmailValid(String email) {
        try {
            String url = "https://api.hunter.io/v2/email-verifier?email=" + email + "&api_key=" + API_KEY;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Afficher la réponse brute pour le debug
            System.out.println("Réponse de l'API : " + response.body());

            // Vérifier si la réponse est vide
            if (response.body().isEmpty()) {
                System.out.println("⚠️ Réponse vide de l'API !");
                return false;
            }

            JSONObject jsonResponse = new JSONObject(response.body());

            // Vérifier si l'API retourne une erreur
            if (jsonResponse.has("message")) {
                System.out.println("⚠️ Erreur API : " + jsonResponse.getString("message"));
                return false;
            }

            // Vérifiez si l'email est valide
            JSONObject data = jsonResponse.getJSONObject("data");
            return !data.getString("result").equals("undeliverable");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
