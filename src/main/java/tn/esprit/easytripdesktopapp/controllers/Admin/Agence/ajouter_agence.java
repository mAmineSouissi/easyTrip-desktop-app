package tn.esprit.easytripdesktopapp.controllers.Admin.Agence;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class ajouter_agence {

    @FXML
    private TextField address;

    @FXML
    private TextField email;

    @FXML
    private ImageView image;

    @FXML
    private TextField name;

    @FXML
    private TextField phone;

    @FXML
    private Button chooseImageButton;

    private String imageUrl;

    private static final String API_KEY = "02a8cb89e2a0961ee3fbcf04c7d962880b91e1cd"; // Remplace par ta clé API

    CRUDService<Agence> agence = new ServiceAgence();

    @FXML
    void save(ActionEvent event) {
        if (name.getText().isEmpty() || address.getText().isEmpty() || phone.getText().isEmpty() || email.getText().isEmpty() || imageUrl == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        // Vérification du format de l'email
        if (!email.getText().matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            showAlert("Erreur", "L'email doit être un @gmail.com", Alert.AlertType.ERROR);
            return;
        }

        // Vérification de l'existence de l'email via API
        if (!isEmailValid(email.getText())) {
            showAlert("Erreur", "L'email n'existe pas ou n'est pas valide !", Alert.AlertType.ERROR);
            return;
        }

        // Vérification du numéro de téléphone (doit contenir exactement 8 chiffres)
        if (!phone.getText().matches("\\d{8}")) {
            showAlert("Erreur", "Le numéro de téléphone doit contenir exactement 8 chiffres !", Alert.AlertType.ERROR);
            return;
        }

        Agence ag = new Agence();
        ag.setNom(name.getText());
        ag.setAddress(address.getText());
        ag.setPhone(phone.getText());
        ag.setEmail(email.getText());
        ag.setImage(imageUrl);

        agence.add(ag);

        showAlert("Succès", "L'agence a été ajoutée avec succès !", Alert.AlertType.INFORMATION);
        clearFields();
    }

    // Vérification de l'email via API Mailboxlayer
    // Vérification de l'email via l'API Hunter
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
            return data.getString("result").equals("undeliverable") == false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Affichage des alertes
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Effacer les champs après ajout
    private void clearFields() {
        name.clear();
        address.clear();
        phone.clear();
        email.clear();
        image.setImage(null);
        imageUrl = null;
    }

    // Choisir une image et l'afficher
    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imageUrl = selectedFile.toURI().toString();
            Image img = new Image(imageUrl);
            image.setImage(img);
        }
    }
}
