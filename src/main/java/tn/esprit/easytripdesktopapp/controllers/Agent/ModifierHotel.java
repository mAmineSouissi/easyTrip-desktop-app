package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
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
    private ComboBox<String> typeroom;
    @FXML
    private TextField numroom;
    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox<String> promotionComboBox;
    @FXML
    private CheckBox promotionSwitch;

    private String imageUrl;
    private Hotel hotelToUpdate;
    private final ServiceHotel hotelService = new ServiceHotel();
    private final ServicePromotion promotionService = new ServicePromotion();

    private float originalPrice; // Champ pour stocker le prix original

    public void setHotel(Hotel hotel) {
        this.hotelToUpdate = hotel;
        name.setText(hotel.getName());
        adresse.setText(hotel.getAdresse());
        city.setText(hotel.getCity());
        rating.setText(String.valueOf(hotel.getRating()));
        description.setText(hotel.getDescription());
        price.setText(String.valueOf(hotel.getPrice()));
        typeroom.setValue(hotel.getTypeRoom());
        numroom.setText(String.valueOf(hotel.getNumRoom()));

        // Stocker le prix original
        this.originalPrice = hotel.getPrice();

        if (hotel.getImage() != null && !hotel.getImage().isEmpty()) {
            Image img = new Image(hotel.getImage());
            imageView.setImage(img);
            imageUrl = hotel.getImage();
        }

        loadPromotions();

        if (hotel.getPromotion() != null) {
            promotionComboBox.setValue(hotel.getPromotion().getTitle());
            promotionSwitch.setSelected(true);
        } else {
            promotionSwitch.setSelected(false);
            promotionComboBox.setDisable(true);
        }
    }

    private void loadPromotions() {
        List<Promotion> promotions = promotionService.getAll();
        for (Promotion promotion : promotions) {
            promotionComboBox.getItems().add(promotion.getTitle());
        }
    }

    @FXML
    private void togglePromotion() {
        if (promotionSwitch.isSelected()) {
            promotionComboBox.setDisable(false);
        } else {
            promotionComboBox.setDisable(true);
            promotionComboBox.setValue(null);
            // Réinitialiser le prix à la valeur originale lorsque la promotion est désactivée
            price.setText(String.valueOf(originalPrice));
        }
    }

    @FXML
    private void saveChanges() {
        // Mettre à jour les informations de l'hôtel
        hotelToUpdate.setName(name.getText());
        hotelToUpdate.setAdresse(adresse.getText());
        hotelToUpdate.setCity(city.getText());
        hotelToUpdate.setRating(Integer.parseInt(rating.getText()));
        hotelToUpdate.setDescription(description.getText());
        hotelToUpdate.setTypeRoom(typeroom.getValue());
        hotelToUpdate.setNumRoom(Integer.parseInt(numroom.getText()));
        hotelToUpdate.setImage(imageUrl);

        // Gérer la promotion
        if (promotionSwitch.isSelected()) {
            String promotionTitle = promotionComboBox.getValue();
            if (promotionTitle != null) {
                Promotion promotion = promotionService.getByTitle(promotionTitle);
                hotelToUpdate.setPromotion(promotion);
                // Appliquer la promotion au prix
                float discountedPrice = calculateDiscountedPrice(originalPrice, promotion);
                hotelToUpdate.setPrice(discountedPrice);
            }
        } else {
            // Si la promotion est désactivée, supprimer la promotion et réinitialiser le prix
            hotelToUpdate.setPromotion(null);
            hotelToUpdate.setPrice(originalPrice);
        }

        // Mettre à jour l'hôtel dans la base de données
        hotelService.update(hotelToUpdate);

        // Fermer la fenêtre de mise à jour
        name.getScene().getWindow().hide();
    }

    private float calculateDiscountedPrice(float originalPrice, Promotion promotion) {
        if (promotion != null) {
            float discountPercentage = promotion.getDiscount_percentage();
            return originalPrice * (1 - discountPercentage / 100);
        }
        return originalPrice;
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imageUrl = selectedFile.toURI().toString();
            Image img = new Image(imageUrl);
            imageView.setImage(img);
        }
    }
}