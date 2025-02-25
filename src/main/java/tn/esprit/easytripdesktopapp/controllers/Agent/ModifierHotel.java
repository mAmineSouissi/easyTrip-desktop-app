package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceHotel;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.File;
import java.util.List;

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
    @FXML
    private ComboBox<String> promotionComboBox; // Nouveau ComboBox pour les promotions

    private String imageUrl; // Pour stocker l'URL de l'image
    private Hotel hotelToUpdate;
    private final ServiceHotel hotelService = new ServiceHotel();
    private final ServicePromotion promotionService = new ServicePromotion();

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

        // Charger les promotions dans le ComboBox
        loadPromotions();

        // Sélectionner la promotion actuelle de l'hôtel
        if (hotel.getPromotion() != null) {
            promotionComboBox.setValue(hotel.getPromotion().getTitle());
        }
    }

    // Méthode pour charger les promotions dans le ComboBox
    private void loadPromotions() {
        List<Promotion> promotions = promotionService.getAll();
        for (Promotion promotion : promotions) {
            promotionComboBox.getItems().add(promotion.getTitle());
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
        hotelToUpdate.setTypeRoom(typeroom.getValue());
        hotelToUpdate.setNumRoom(Integer.parseInt(numroom.getText()));
        hotelToUpdate.setImage(imageUrl);

        // Récupérer la promotion sélectionnée
        String promotionTitle = promotionComboBox.getValue();
        Promotion promotion = promotionService.getByTitle(promotionTitle); // Récupérer la promotion par son titre
        hotelToUpdate.setPromotion(promotion); // Associer la promotion à l'hôtel

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