package tn.esprit.easytripdesktopapp.controllers.Admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.sql.Date;

public class UpdatePromotion {

    @FXML
    private TextField title;

    @FXML
    private TextArea description;

    @FXML
    private TextField discountPercentage;

    @FXML
    private DatePicker validUntil;

    private Promotion selectedPromotion;

    CRUDService<Promotion> promotionService = new ServicePromotion();

    // Méthode pour recevoir la promotion sélectionnée
    public void setPromotion(Promotion promotion) {
        this.selectedPromotion = promotion;

        // Remplir les champs avec les données existantes
        title.setText(promotion.getTitle());
        description.setText(promotion.getDescription());
        discountPercentage.setText(String.valueOf(promotion.getDiscount_percentage()));

        // ✅ Convertir java.util.Date en LocalDate pour le DatePicker
        if (promotion.getValid_until() != null) {
            // Conversion de java.util.Date à java.sql.Date
            Date sqlDate = new Date(promotion.getValid_until().getTime());
            // Conversion de java.sql.Date à LocalDate
            validUntil.setValue(sqlDate.toLocalDate());
        }
    }

    @FXML
    public void updatePromotion(ActionEvent actionEvent) {
        if (title.getText().isEmpty() || description.getText().isEmpty() || discountPercentage.getText().isEmpty() || validUntil.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        // Vérifier si la date choisie est avant aujourd'hui
        if (validUntil.getValue().isBefore(java.time.LocalDate.now())) {
            showAlert("Erreur", "La date de validité ne peut pas être antérieure à aujourd'hui !", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Convertir discountPercentage en float
            float discount = Float.parseFloat(discountPercentage.getText());

            // Convertir validUntil en java.util.Date
            java.util.Date utilDate = Date.valueOf(validUntil.getValue());

            // Mettre à jour la promotion sélectionnée
            selectedPromotion.setTitle(title.getText());
            selectedPromotion.setDescription(description.getText());
            selectedPromotion.setDiscount_percentage(discount);
            selectedPromotion.setValid_until(utilDate);

            // Appeler la méthode de mise à jour du service
            promotionService.update(selectedPromotion);
            showAlert("Succès", "Promotion mise à jour avec succès !", Alert.AlertType.INFORMATION);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le pourcentage de réduction doit être un nombre valide !", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
