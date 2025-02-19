package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;

import java.io.File;

public class AjouterHotel {

    @FXML
    private TextField name;
    @FXML
    private TextField adresse;
    @FXML
    private TextField city;
    @FXML
    private TextField rating;
    @FXML
    private TextField description;
    @FXML
    private TextField price;
    @FXML
    private TextField typeroom;
    @FXML
    private TextField numroom;
    @FXML
    private ImageView image;

    @FXML
    private Button chooseImageButton;

    private String imageUrl; // Variable pour stocker l'URL de l'image

    private final ServiceHotel hotelService = new ServiceHotel();

    @FXML
    private void save() {
        // Valider les champs avant de procéder à l'ajout
        if (validateFields()) {
            // Récupérer les valeurs des champs
            String nom = name.getText();
            String addr = adresse.getText();
            String ville = city.getText();
            int note = Integer.parseInt(rating.getText());
            String desc = description.getText();
            float prix = Float.parseFloat(price.getText());
            String typeChambre = typeroom.getText();
            int nbChambres = Integer.parseInt(numroom.getText());
            String img = imageUrl;

            // Créer un nouvel objet Hotel
            Hotel hotel = new Hotel();
            hotel.setName(nom);
            hotel.setAdresse(addr);
            hotel.setCity(ville);
            hotel.setRating(note);
            hotel.setDescription(desc);
            hotel.setPrice(prix);
            hotel.setTypeRoom(typeChambre);
            hotel.setNumRoom(nbChambres);
            hotel.setImage(img);

            // Ajouter l'hôtel via le service
            hotelService.add(hotel);

            // Fermer la fenêtre après l'ajout
            name.getScene().getWindow().hide();
        }
    }

    // Méthode pour valider les champs du formulaire
    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        // Validation du nom
        if (name.getText().isEmpty()) {
            errors.append("Le nom de l'hôtel est obligatoire.\n");
        }

        // Validation de l'adresse
        if (adresse.getText().isEmpty()) {
            errors.append("L'adresse de l'hôtel est obligatoire.\n");
        }

        // Validation de la ville
        if (city.getText().isEmpty()) {
            errors.append("La ville de l'hôtel est obligatoire.\n");
        }

        // Validation de la note
        try {
            int note = Integer.parseInt(rating.getText());
            if (note < 0 || note > 5) {
                errors.append("La note doit être comprise entre 0 et 5.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("La note doit être un nombre entier.\n");
        }

        // Validation de la description
        if (description.getText().isEmpty()) {
            errors.append("La description de l'hôtel est obligatoire.\n");
        }

        // Validation du prix
        try {
            float prix = Float.parseFloat(price.getText());
            if (prix < 0) {
                errors.append("Le prix doit être un nombre positif.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Le prix doit être un nombre valide.\n");
        }

        // Validation du type de chambre
        if (typeroom.getText().isEmpty()) {
            errors.append("Le type de chambre est obligatoire.\n");
        }

        // Validation du nombre de chambres
        try {
            int nbChambres = Integer.parseInt(numroom.getText());
            if (nbChambres < 0) {
                errors.append("Le nombre de chambres doit être un nombre positif.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Le nombre de chambres doit être un nombre entier.\n");
        }

        // Validation de l'image
        if (imageUrl == null || imageUrl.isEmpty()) {
            errors.append("L'URL de l'image est obligatoire.\n");
        }

        // Si des erreurs sont détectées, afficher une alerte
        if (errors.length() > 0) {
            showAlert("Erreur de validation", errors.toString());
            return false;
        }

        return true;
    }

    // Méthode pour afficher un message d'alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void chooseImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Stocker l'URL de l'image sélectionnée
            imageUrl = selectedFile.toURI().toString();

            // Afficher l'image dans l'ImageView
            Image img = new Image(imageUrl);
            image.setImage(img);
        }
    }
}