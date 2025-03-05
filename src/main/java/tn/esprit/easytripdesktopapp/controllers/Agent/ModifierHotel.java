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
    private float basePrice; // Prix de base sans promotion
    private final ServiceHotel hotelService = new ServiceHotel();
    private final ServicePromotion promotionService = new ServicePromotion();


    public void setHotel(Hotel hotel) {
        this.hotelToUpdate = hotel;


        this.basePrice = (hotel.getPromotion() != null ?
                hotel.getPrice() / (1 - hotel.getPromotion().getDiscount_percentage() / 100) :
                hotel.getPrice());

        name.setText(hotel.getName());
        adresse.setText(hotel.getAdresse());
        city.setText(hotel.getCity());
        rating.setText(String.valueOf(hotel.getRating()));
        description.setText(hotel.getDescription());
        price.setText(String.format("%.2f", hotel.getPrice()).replace(".", ",")); // Afficher avec virgule
        typeroom.setValue(hotel.getTypeRoom());
        numroom.setText(String.valueOf(hotel.getNumRoom()));

        if (hotel.getImage() != null && !hotel.getImage().isEmpty()) {
            Image img = new Image(hotel.getImage());
            imageView.setImage(img);
            imageUrl = hotel.getImage();
        }

        loadPromotions();

        if (hotel.getPromotion() != null) {
            promotionComboBox.setValue(hotel.getPromotion().getTitle());
        } else {
            promotionComboBox.setValue("Aucune promotion");
        }

        promotionComboBox.setOnAction(event -> updatePriceBasedOnPromotion());
    }


    private void loadPromotions() {
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
                float discountPercentage = promotion.getDiscount_percentage();
                float discountedPrice = basePrice * (1 - discountPercentage / 100);
                price.setText(String.format("%.2f", discountedPrice).replace(".", ",")); // Afficher avec virgule
            }
        } else {
            price.setText(String.format("%.2f", basePrice).replace(".", ",")); // Réinitialiser au prix de base avec virgule
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
            hotelToUpdate.setPrice(Float.parseFloat(price.getText().replace(",", "."))); // Convertir en point pour sauvegarde
            hotelToUpdate.setTypeRoom(typeroom.getValue());
            hotelToUpdate.setNumRoom(Integer.parseInt(numroom.getText()));
            hotelToUpdate.setImage(imageUrl);

            String promotionTitle = promotionComboBox.getValue();
            Promotion promotion = null;
            if (promotionTitle != null && !promotionTitle.equals("Aucune promotion")) {
                promotion = promotionService.getByTitle(promotionTitle);
            }
            hotelToUpdate.setPromotion(promotion);

            hotelService.update(hotelToUpdate);
            name.getScene().getWindow().hide();
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
            String priceText = price.getText().replace(",", "."); // Accepter la virgule
            float prix = Float.parseFloat(priceText);
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


    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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