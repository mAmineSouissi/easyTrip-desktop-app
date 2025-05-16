package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
    private ComboBox<String> typeroom;
    @FXML
    private TextField numroom;
    @FXML
    private ImageView imageView;
    @FXML
    private ComboBox<String> promotionComboBox;

    private String imageUrl;
    private Hotel hotelToUpdate;
    private float basePrice;
    private AffichageHotel affichageHotelController;
    private final ServiceHotel hotelService = new ServiceHotel();
    private final ServicePromotion promotionService = new ServicePromotion();

    public void setHotel(Hotel hotel) {
        this.hotelToUpdate = hotel;
        this.basePrice = (hotel.getPromotion() != null ?
                hotel.getPrice() / (1 - hotel.getPromotion().getDiscount_percentage() / 100) :
                hotel.getPrice());

        // Initialiser les champs avec les valeurs de l'hôtel
        name.setText(hotel.getName());
        adresse.setText(hotel.getAdresse());
        city.setText(hotel.getCity());
        rating.setText(String.valueOf(hotel.getRating()));
        description.setText(hotel.getDescription());
        price.setText(String.format("%.2f", hotel.getPrice()).replace(".", ","));
        typeroom.setValue(hotel.getTypeRoom());
        numroom.setText(String.valueOf(hotel.getNumRoom()));

        // Charger l'image
        if (hotel.getImage() != null && !hotel.getImage().isEmpty()) {
            try {
                Image img = new Image(hotel.getImage());
                imageView.setImage(img);
                imageUrl = hotel.getImage();
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de charger l'image de l'hôtel");
            }
        }

        // Initialiser les types de chambre
        typeroom.getItems().addAll("Simple", "Double", "Suite");

        // Charger les promotions
        loadPromotions();

        // Sélectionner la promotion actuelle si elle existe
        if (hotel.getPromotion() != null) {
            promotionComboBox.setValue(hotel.getPromotion().getTitle());
        } else {
            promotionComboBox.setValue("Aucune promotion");
        }

        // Écouteur pour mettre à jour le prix selon la promotion
        promotionComboBox.setOnAction(event -> updatePriceBasedOnPromotion());
    }

    public void setAffichageHotelController(AffichageHotel controller) {
        this.affichageHotelController = controller;
    }

    private void loadPromotions() {
        promotionComboBox.getItems().clear();
        promotionComboBox.getItems().add("Aucune promotion");
        List<Promotion> promotions = promotionService.getAll();
        for (Promotion promotion : promotions) {
            promotionComboBox.getItems().add(promotion.getTitle());
        }
    }

    private void updatePriceBasedOnPromotion() {
        String promotionTitle = promotionComboBox.getValue();
        if (promotionTitle != null && !promotionTitle.equals("Aucune promotion")) {
            Promotion promotion = promotionService.getByTitle(promotionTitle);
            if (promotion != null) {
                float discountedPrice = basePrice * (1 - promotion.getDiscount_percentage() / 100);
                price.setText(String.format("%.2f", discountedPrice).replace(".", ","));
            }
        } else {
            price.setText(String.format("%.2f", basePrice).replace(".", ","));
        }
    }

    @FXML
    private void saveChanges() {
        if (validateFields()) {
            hotelToUpdate.setName(name.getText());
            hotelToUpdate.setAdresse(adresse.getText());
            hotelToUpdate.setCity(city.getText());
            hotelToUpdate.setRating(Integer.parseInt(rating.getText()));
            hotelToUpdate.setDescription(description.getText());
            hotelToUpdate.setPrice(Float.parseFloat(price.getText().replace(",", ".")));
            hotelToUpdate.setTypeRoom(typeroom.getValue());
            hotelToUpdate.setNumRoom(Integer.parseInt(numroom.getText()));
            hotelToUpdate.setImage(imageUrl);

            // Gestion de la promotion
            String promotionTitle = promotionComboBox.getValue();
            Promotion promotion = null;
            if (promotionTitle != null && !promotionTitle.equals("Aucune promotion")) {
                promotion = promotionService.getByTitle(promotionTitle);
            }
            hotelToUpdate.setPromotion(promotion);

            // Sauvegarder les modifications
            hotelService.update(hotelToUpdate);

            // Rafraîchir la liste des hôtels dans AffichageHotel
            if (affichageHotelController != null) {
                affichageHotelController.refreshHotels();
            }

            // Fermer la fenêtre
            Stage stage = (Stage) name.getScene().getWindow();
            stage.close();
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (name.getText().isEmpty()) errors.append("Le nom de l'hôtel est obligatoire.\n");
        if (adresse.getText().isEmpty()) errors.append("L'adresse de l'hôtel est obligatoire.\n");
        if (city.getText().isEmpty()) errors.append("La ville de l'hôtel est obligatoire.\n");

        try {
            int note = Integer.parseInt(rating.getText());
            if (note < 0 || note > 5) errors.append("La note doit être entre 0 et 5.\n");
        } catch (NumberFormatException e) {
            errors.append("La note doit être un nombre entier.\n");
        }

        if (description.getText().isEmpty()) errors.append("La description est obligatoire.\n");

        try {
            float prix = Float.parseFloat(price.getText().replace(",", "."));
            if (prix < 0) errors.append("Le prix doit être positif.\n");
        } catch (NumberFormatException e) {
            errors.append("Le prix doit être un nombre valide (ex. 12,00 ou 12.00).\n");
        }

        if (typeroom.getValue() == null || typeroom.getValue().isEmpty()) {
            errors.append("Le type de chambre est obligatoire.\n");
        }

        try {
            int nbChambres = Integer.parseInt(numroom.getText());
            if (nbChambres < 0) errors.append("Le nombre de chambres doit être positif.\n");
        } catch (NumberFormatException e) {
            errors.append("Le nombre de chambres doit être un entier.\n");
        }

        if (imageUrl == null || imageUrl.isEmpty()) errors.append("L'image est obligatoire.\n");

        if (errors.length() > 0) {
            showAlert("Erreur de validation", errors.toString());
            return false;
        }
        return true;
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                imageUrl = selectedFile.toURI().toString();
                imageView.setImage(new Image(imageUrl));
            } catch (Exception e) {
                showAlert("Erreur", "Impossible de charger l'image sélectionnée");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}