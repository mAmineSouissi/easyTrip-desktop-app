package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.File;
import java.util.List;

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
    private ComboBox<String> typeroom; // Utilisation d'un ComboBox pour le type de chambre
    @FXML
    private TextField numroom;
    @FXML
    private ImageView image;
    @FXML
    private ComboBox<String> promotionComboBox; // Nouveau ComboBox pour les promotions

    @FXML
    private Button chooseImageButton;

    private String imageUrl;

    private final ServiceHotel hotelService = new ServiceHotel();
    private final ServicePromotion promotionService = new ServicePromotion();

    UserSession session = UserSession.getInstance();

    @FXML
    public void initialize() {
        // Charger les promotions dans le ComboBox
        loadPromotions();
    }

    private void loadPromotions() {
        List<Promotion> promotions = promotionService.getAll();
        for (Promotion promotion : promotions) {
            promotionComboBox.getItems().add(promotion.getTitle());
        }
    }

    @FXML
    private void save() {
        if (validateFields()) {
            String nom = name.getText();
            String addr = adresse.getText();
            String ville = city.getText();
            int note = Integer.parseInt(rating.getText());
            String desc = description.getText();
            float prix = Float.parseFloat(price.getText());
            String typeChambre = typeroom.getValue(); // Récupérer la valeur sélectionnée dans le ComboBox
            int nbChambres = Integer.parseInt(numroom.getText());
            String img = imageUrl;

            // Récupérer la promotion sélectionnée
            String promotionTitle = promotionComboBox.getValue();
            Promotion promotion = promotionService.getByTitle(promotionTitle);

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
            hotel.setPromotion(promotion);
            hotel.setUserId(session.getUser().getId());
            // Associer la promotion à l'hôtel

            hotelService.add(hotel);
            name.getScene().getWindow().hide();
        }
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        if (name.getText().isEmpty()) {
            errors.append("Le nom de l'hôtel est obligatoire.\n");
        }

        if (adresse.getText().isEmpty()) {
            errors.append("L'adresse de l'hôtel est obligatoire.\n");
        }

        if (city.getText().isEmpty()) {
            errors.append("La ville de l'hôtel est obligatoire.\n");
        }

        try {
            int note = Integer.parseInt(rating.getText());
            if (note < 0 || note > 5) {
                errors.append("La note doit être comprise entre 0 et 5.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("La note doit être un nombre entier.\n");
        }

        if (description.getText().isEmpty()) {
            errors.append("La description de l'hôtel est obligatoire.\n");
        }

        try {
            float prix = Float.parseFloat(price.getText());
            if (prix < 0) {
                errors.append("Le prix doit être un nombre positif.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Le prix doit être un nombre valide.\n");
        }

        if (typeroom.getValue() == null || typeroom.getValue().isEmpty()) {
            errors.append("Le type de chambre est obligatoire.\n");
        }

        try {
            int nbChambres = Integer.parseInt(numroom.getText());
            if (nbChambres < 0) {
                errors.append("Le nombre de chambres doit être un nombre positif.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Le nombre de chambres doit être un nombre entier.\n");
        }

        if (imageUrl == null || imageUrl.isEmpty()) {
            errors.append("L'URL de l'image est obligatoire.\n");
        }

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