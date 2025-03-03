package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.models.Category;
import tn.esprit.easytripdesktopapp.models.OfferTravel;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceOfferTravel;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.File;
import java.sql.Date;
import java.util.List;

public class AjouterOfferTravelController {

    @FXML
    private TextField tfDeparture, tfDestination, tfHotelName, tfFlightName, tfPrice;
    @FXML
    private DatePicker dpDepartureDate, dpArrivalDate;
    @FXML
    private TextArea taDescription;
    @FXML
    private ComboBox<Agence> cbAgence;
    @FXML
    private ComboBox<Promotion> cbPromotion;
    @FXML
    private ComboBox<Category> cbCategory;
    @FXML
    private ImageView imgOffer;
    @FXML
    private Button btnAjouter, btnChoisirImage;

    private String imagePath = "";

    private final ServiceOfferTravel serviceOfferTravel = new ServiceOfferTravel();
    private final ServiceAgence serviceAgence = new ServiceAgence();
    private final ServicePromotion servicePromotion = new ServicePromotion();

    @FXML
    public void initialize() {
        // Charger les agences
        List<Agence> agences = serviceAgence.getAll();
        cbAgence.getItems().addAll(agences);
        cbAgence.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Agence agence, boolean empty) {
                super.updateItem(agence, empty);
                setText((agence == null || empty) ? "" : agence.getNom());
            }
        });
        cbAgence.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Agence agence, boolean empty) {
                super.updateItem(agence, empty);
                setText((agence == null) ? "" : agence.getNom());
            }
        });

        // Charger les promotions
        List<Promotion> promotions = servicePromotion.getAll();
        cbPromotion.getItems().addAll(promotions);
        cbPromotion.getItems().add(null); // Option pour aucune promotion
        cbPromotion.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Promotion promotion, boolean empty) {
                super.updateItem(promotion, empty);
                setText((promotion == null || empty) ? "" : promotion.getTitle());
            }
        });
        cbPromotion.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Promotion promotion, boolean empty) {
                super.updateItem(promotion, empty);
                setText((promotion == null) ? "" : promotion.getTitle());
            }
        });

        // Charger les catégories
        cbCategory.getItems().addAll(Category.values());
    }

    @FXML
    private void ajouterOffer(ActionEvent event) {
        try {
            String departure = tfDeparture.getText();
            String destination = tfDestination.getText();
            String hotelName = tfHotelName.getText();
            String flightName = tfFlightName.getText();
            String description = taDescription.getText();
            float price = Float.parseFloat(tfPrice.getText()); // Prix saisi par l'utilisateur
            Agence agence = cbAgence.getValue();
            Promotion promotion = cbPromotion.getValue(); // Peut être null
            Category category = cbCategory.getValue();

            // Validation des champs obligatoires
            if (departure.isEmpty() || destination.isEmpty() || hotelName.isEmpty() || flightName.isEmpty() ||
                    description.isEmpty() || category == null || agence == null ||
                    dpDepartureDate.getValue() == null || dpArrivalDate.getValue() == null || imagePath.isEmpty()) {

                showAlert("Erreur", "Tous les champs obligatoires doivent être remplis !", Alert.AlertType.ERROR);
                return;
            }

            Date departureDate = java.sql.Date.valueOf(dpDepartureDate.getValue());
            Date arrivalDate = java.sql.Date.valueOf(dpArrivalDate.getValue());

            // Validation des dates
            if (departureDate.after(arrivalDate)) {
                showAlert("Erreur", "La date de départ ne peut pas être après la date d'arrivée !", Alert.AlertType.ERROR);
                return;
            }

            // Calculer le prix final après application de la promotion
            float finalPrice = getFinalPrice(price, promotion);

            // Créer l'offre avec le prix final
            OfferTravel offer = new OfferTravel(0, departure, destination, departureDate, arrivalDate, hotelName, flightName, description, finalPrice, imagePath, agence, promotion, category);
            serviceOfferTravel.add(offer);

            showAlert("Succès", "Offre ajoutée avec succès !", Alert.AlertType.INFORMATION);
            clearFields();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout : " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void choisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            imagePath = file.getAbsolutePath();
            imgOffer.setImage(new Image(file.toURI().toString())); // Affiche l'image dans ImageView
        } else {
            showAlert("Avertissement", "Aucune image sélectionnée !", Alert.AlertType.WARNING);
        }
    }

    private void clearFields() {
        tfDeparture.clear();
        tfDestination.clear();
        dpDepartureDate.setValue(null);
        dpArrivalDate.setValue(null);
        tfHotelName.clear();
        tfFlightName.clear();
        taDescription.clear();
        tfPrice.clear();
        imgOffer.setImage(null); // Réinitialise l'image
        cbAgence.setValue(null);
        cbPromotion.setValue(null);
        cbCategory.setValue(null);
        imagePath = "";
    }

    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour calculer le prix final
    private float getFinalPrice(float price, Promotion promotion) {
        if (promotion != null) {
            return price - (price * promotion.getDiscount_percentage() / 100);
        } else {
            return price;
        }
    }
}