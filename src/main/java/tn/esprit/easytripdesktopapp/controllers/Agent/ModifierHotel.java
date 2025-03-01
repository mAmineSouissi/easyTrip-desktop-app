package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    private float originalPrice; // Stockage du prix original

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

        // Stocker le prix original avant toute promotion
        this.originalPrice = getInitialPrice(hotel.getPrice(), hotel.getPromotion());

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
        promotionComboBox.getItems().clear(); // Évite les doublons
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
            price.setText(String.valueOf(originalPrice)); // Restaurer le prix initial
        }
    }

    @FXML
    private void saveChanges() {
        try {
            // Mettre à jour les informations de l'hôtel
            hotelToUpdate.setName(name.getText());
            hotelToUpdate.setAdresse(adresse.getText());
            hotelToUpdate.setCity(city.getText());
            hotelToUpdate.setRating(Integer.parseInt(rating.getText()));
            hotelToUpdate.setDescription(description.getText());
            hotelToUpdate.setTypeRoom(typeroom.getValue());
            hotelToUpdate.setNumRoom(Integer.parseInt(numroom.getText()));
            hotelToUpdate.setImage(imageUrl);

            // Récupérer le prix initial avant promotion
            originalPrice = getInitialPrice(Float.parseFloat(price.getText()), hotelToUpdate.getPromotion());

            // Gestion de la promotion
            if (promotionSwitch.isSelected()) {
                String promotionTitle = promotionComboBox.getValue();
                if (promotionTitle != null) {
                    Promotion promotion = promotionService.getByTitle(promotionTitle);
                    hotelToUpdate.setPromotion(promotion);
                    // Appliquer la promotion
                    float finalPrice = getFinalPrice(originalPrice, promotion);
                    hotelToUpdate.setPrice(finalPrice);
                }
            } else {
                hotelToUpdate.setPromotion(null);
                hotelToUpdate.setPrice(originalPrice);
            }

            // Mettre à jour l'hôtel dans la base de données
            hotelService.update(hotelToUpdate);

            showAlert("Succès", "L'hôtel a été mis à jour avec succès !", Alert.AlertType.INFORMATION);
            closeWindow();

        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide !", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif", "*.bmp"));

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imageUrl = selectedFile.toURI().toString();
            Image img = new Image(imageUrl);
            imageView.setImage(img);
        }
    }

    // Méthode pour calculer le prix final après application de la promotion
    private float getFinalPrice(float initialPrice, Promotion promotion) {
        if (promotion != null) {
            return initialPrice - (initialPrice * promotion.getDiscount_percentage() / 100);
        } else {
            return initialPrice;
        }
    }

    // Méthode pour récupérer le prix initial avant application de la promotion
    private float getInitialPrice(float currentPrice, Promotion promotion) {
        if (promotion != null) {
            return currentPrice / (1 - (promotion.getDiscount_percentage() / 100));
        } else {
            return currentPrice;
        }
    }

    // Affichage d'une alerte
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Fermer la fenêtre
    private void closeWindow() {
        Stage stage = (Stage) name.getScene().getWindow();
        stage.close();
    }
}
