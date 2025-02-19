package tn.esprit.easytripdesktopapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.easytripdesktopapp.interfaces.CRUDService;
import tn.esprit.easytripdesktopapp.models.Agence;
import tn.esprit.easytripdesktopapp.models.Promotion;
import tn.esprit.easytripdesktopapp.services.ServiceAgence;
import tn.esprit.easytripdesktopapp.services.ServicePromotion;

import java.time.LocalDate;

public class AjouterPromotion {

    @FXML
    private TextField title;

    @FXML
    private TextArea description;

    @FXML
    private TextField discountPercentage;

    @FXML
    private DatePicker validUntil;

    CRUDService<Promotion> promotion = new ServicePromotion();

    @FXML
    void save(ActionEvent event) {
        if (title.getText().isEmpty() || description.getText().isEmpty() || discountPercentage.getText().isEmpty() || validUntil.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Convertir discountPercentage en float
            float discount = Float.parseFloat(discountPercentage.getText());

            // Convertir validUntil en java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(validUntil.getValue());

            Promotion p = new Promotion();
            p.setTitle(title.getText());
            p.setDescription(description.getText());
            p.setDiscount_percentage(discount); // Utilisation de float
            p.setValid_until(sqlDate); // Utilisation de Date

            promotion.add(p);
            showAlert("Succès", "Promotion ajoutée avec succès !", Alert.AlertType.INFORMATION);
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
