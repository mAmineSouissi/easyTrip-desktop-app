package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;

import java.io.File;

public class ModifierHotel {

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
    private ComboBox<String> typeroom; // Utilisation d'un ComboBox pour le type de chambre
    @FXML
    private TextField numroom;
    @FXML
    private ImageView imageView; // Pour afficher l'image sélectionnée

    private String imageUrl; // Pour stocker l'URL de l'image
    private Hotel hotelToUpdate;
    private final ServiceHotel hotelService = new ServiceHotel();

    // Méthode pour initialiser les champs avec les informations de l'hôtel à mettre à jour
    public void setHotel(Hotel hotel) {
        this.hotelToUpdate = hotel;
        name.setText(hotel.getName());
        adresse.setText(hotel.getAdresse());
        city.setText(hotel.getCity());
        rating.setText(String.valueOf(hotel.getRating()));
        description.setText(hotel.getDescription());
        price.setText(String.valueOf(hotel.getPrice()));
        typeroom.setValue(hotel.getTypeRoom()); // Définir la valeur sélectionnée dans le ComboBox
        numroom.setText(String.valueOf(hotel.getNumRoom()));

        // Afficher l'image de l'hôtel dans l'ImageView
        if (hotel.getImage() != null && !hotel.getImage().isEmpty()) {
            Image img = new Image(hotel.getImage());
            imageView.setImage(img);
            imageUrl = hotel.getImage(); // Stocker l'URL de l'image
        }
    }

    // Méthode pour enregistrer les modifications
    @FXML
    private void saveChanges() {
        // Mettre à jour l'objet Hotel avec les nouvelles valeurs
        hotelToUpdate.setName(name.getText());
        hotelToUpdate.setAdresse(adresse.getText());
        hotelToUpdate.setCity(city.getText());
        hotelToUpdate.setRating(Integer.parseInt(rating.getText()));
        hotelToUpdate.setDescription(description.getText());
        hotelToUpdate.setPrice(Float.parseFloat(price.getText()));
        hotelToUpdate.setTypeRoom(typeroom.getValue()); // Récupérer la valeur sélectionnée dans le ComboBox
        hotelToUpdate.setNumRoom(Integer.parseInt(numroom.getText()));
        hotelToUpdate.setImage(imageUrl); // Utiliser l'URL de l'image sélectionnée

        // Appeler le service pour mettre à jour l'hôtel dans la base de données
        hotelService.update(hotelToUpdate);

        // Fermer la fenêtre de mise à jour
        name.getScene().getWindow().hide();
    }

    // Méthode pour télécharger une image
    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            // Stocker l'URL de l'image sélectionnée
            imageUrl = selectedFile.toURI().toString();

            // Afficher l'image dans l'ImageView
            Image img = new Image(imageUrl);
            imageView.setImage(img);
        }
    }
}