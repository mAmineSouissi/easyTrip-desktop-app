package tn.esprit.easytripdesktopapp.controllers.Agent;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceTicket;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.io.File;
import java.util.List;

public class ModifierTicketController {

    @FXML
    private TextField flightNumber;
    @FXML
    private TextField airline;
    @FXML
    private TextField departureCity;
    @FXML
    private TextField arrivalCity;
    @FXML
    private TextField departureDate;
    @FXML
    private TextField departureTime;
    @FXML
    private TextField arrivalDate;
    @FXML
    private TextField arrivalTime;
    @FXML
    private TextField ticketClass;
    @FXML
    private TextField price;
    @FXML
    private TextField ticketType;
    @FXML
    private TextField cityImage;
    @FXML
    private Button uploadButton;
    @FXML
    private CheckBox promotionSwitch; // CheckBox pour activer/désactiver la promotion
    @FXML
    private ComboBox<String> promotionComboBox; // ComboBox pour sélectionner la promotion

    private Ticket ticketToUpdate;
    private final ServiceTicket ticketService = new ServiceTicket();
    private final ServicePromotion promotionService = new ServicePromotion();

    private float originalPrice; // Champ pour stocker le prix original

    // Méthode pour initialiser les champs avec les informations du ticket à mettre à jour
    public void setTicket(Ticket ticket) {
        this.ticketToUpdate = ticket;
        flightNumber.setText(String.valueOf(ticket.getFlightNumber()));
        airline.setText(ticket.getAirline());
        departureCity.setText(ticket.getDepartureCity());
        arrivalCity.setText(ticket.getArrivalCity());
        departureDate.setText(ticket.getDepartureDate());
        departureTime.setText(ticket.getDepartureTime());
        arrivalDate.setText(ticket.getArrivalDate());
        arrivalTime.setText(ticket.getArrivalTime());
        ticketClass.setText(ticket.getTicketClass());
        price.setText(String.valueOf(ticket.getPrice()));
        ticketType.setText(ticket.getTicketType());
        cityImage.setText(ticket.getCityImage());

        // Stocker le prix original
        this.originalPrice = ticket.getPrice();

        // Charger les promotions dans le ComboBox
        loadPromotions();

        // Si une promotion est associée au ticket, l'afficher dans le ComboBox
        if (ticket.getPromotion() != null) {
            promotionComboBox.setValue(ticket.getPromotion().getTitle());
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
            promotionComboBox.setDisable(false); // Activer le ComboBox des promotions
        } else {
            promotionComboBox.setDisable(true); // Désactiver le ComboBox des promotions
            promotionComboBox.setValue(null);   // Effacer la sélection de la promotion
            // Réinitialiser le prix à la valeur originale lorsque la promotion est désactivée
            price.setText(String.valueOf(originalPrice));
        }
    }

    @FXML
    private void uploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        Stage stage = (Stage) uploadButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            cityImage.setText(selectedFile.getAbsolutePath()); // Affiche le chemin de l'image sélectionnée
        }
    }

    @FXML
    private void saveChanges() {
        // Valider les champs avant de procéder à la mise à jour
        if (validateFields()) {
            // Mettre à jour l'objet Ticket avec les nouvelles valeurs
            ticketToUpdate.setFlightNumber(Integer.parseInt(flightNumber.getText()));
            ticketToUpdate.setAirline(airline.getText());
            ticketToUpdate.setDepartureCity(departureCity.getText());
            ticketToUpdate.setArrivalCity(arrivalCity.getText());
            ticketToUpdate.setDepartureDate(departureDate.getText());
            ticketToUpdate.setDepartureTime(departureTime.getText());
            ticketToUpdate.setArrivalDate(arrivalDate.getText());
            ticketToUpdate.setArrivalTime(arrivalTime.getText());
            ticketToUpdate.setTicketClass(ticketClass.getText());
            ticketToUpdate.setTicketType(ticketType.getText());
            ticketToUpdate.setCityImage(cityImage.getText());

            // Gérer la promotion
            if (promotionSwitch.isSelected()) {
                String promotionTitle = promotionComboBox.getValue();
                if (promotionTitle != null) {
                    Promotion promotion = promotionService.getByTitle(promotionTitle);
                    ticketToUpdate.setPromotion(promotion);
                    // Appliquer la promotion au prix
                    float discountedPrice = calculateDiscountedPrice(originalPrice, promotion);
                    ticketToUpdate.setPrice(discountedPrice);
                }
            } else {
                // Si la promotion est désactivée, supprimer la promotion et réinitialiser le prix
                ticketToUpdate.setPromotion(null);
                ticketToUpdate.setPrice(originalPrice);
            }

            // Appeler le service pour mettre à jour le ticket dans la base de données
            ticketService.update(ticketToUpdate);

            // Fermer la fenêtre de mise à jour
            flightNumber.getScene().getWindow().hide();
        }
    }

    private float calculateDiscountedPrice(float originalPrice, Promotion promotion) {
        if (promotion != null) {
            float discountPercentage = promotion.getDiscount_percentage();
            return originalPrice * (1 - discountPercentage / 100);
        }
        return originalPrice; // Si aucune promotion n'est sélectionnée, retourner le prix original
    }

    private boolean validateFields() {
        StringBuilder errors = new StringBuilder();

        // Validation du numéro de vol
        try {
            int flightNum = Integer.parseInt(flightNumber.getText());
            if (flightNum < 0) {
                errors.append("Le numéro de vol doit être un nombre positif.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Le numéro de vol doit être un nombre entier.\n");
        }

        // Validation de la compagnie aérienne
        if (airline.getText().isEmpty()) {
            errors.append("La compagnie aérienne est obligatoire.\n");
        }

        // Validation de la ville de départ
        if (departureCity.getText().isEmpty()) {
            errors.append("La ville de départ est obligatoire.\n");
        }

        // Validation de la ville d'arrivée
        if (arrivalCity.getText().isEmpty()) {
            errors.append("La ville d'arrivée est obligatoire.\n");
        }

        // Validation de la date de départ
        if (departureDate.getText().isEmpty()) {
            errors.append("La date de départ est obligatoire.\n");
        }

        // Validation de l'heure de départ
        if (departureTime.getText().isEmpty()) {
            errors.append("L'heure de départ est obligatoire.\n");
        }

        // Validation de la date d'arrivée
        if (arrivalDate.getText().isEmpty()) {
            errors.append("La date d'arrivée est obligatoire.\n");
        }

        // Validation de l'heure d'arrivée
        if (arrivalTime.getText().isEmpty()) {
            errors.append("L'heure d'arrivée est obligatoire.\n");
        }

        // Validation de la classe
        if (ticketClass.getText().isEmpty()) {
            errors.append("La classe est obligatoire.\n");
        }

        // Validation du prix
        try {
            float pr = Float.parseFloat(price.getText());
            if (pr < 0) {
                errors.append("Le prix doit être un nombre positif.\n");
            }
        } catch (NumberFormatException e) {
            errors.append("Le prix doit être un nombre valide.\n");
        }

        // Validation du type de ticket
        if (ticketType.getText().isEmpty()) {
            errors.append("Le type de ticket est obligatoire.\n");
        }

        // Validation de l'image de la ville
        if (cityImage.getText().isEmpty()) {
            errors.append("L'image de la ville est obligatoire.\n");
        }

        // Si des erreurs sont détectées, afficher une alerte
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
}