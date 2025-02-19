package tn.esprit.easytripdesktopapp.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;

import java.io.IOException;

public class Addreservation {
    @FXML
    private TextField mailres;

    @FXML
    private TextField nomres;

    @FXML
    private TextField phoneres;

    @FXML
    private TextField prenomres;

    private ServiceReservation sr = new ServiceReservation();
    @FXML
    void ajouterR(ActionEvent event) {
        try {
            if (!allFieldsFilled()) {
                afficherErreur("Veuillez remplir tous les champs obligatoires.");
                return;
            }
            if (!isValidPhoneNumber(phoneres.getText())) {
                afficherErreur("Veuillez saisir un numéro de téléphone valide.");
                return;
            }
            if (!isValidEmail(mailres.getText())) {
                afficherErreur("Veuillez saisir un E-mail valide.");
                return;
            }
            Reservation r = new Reservation();
            r.setNom(nomres.getText());
            r.setPrenom(prenomres.getText());
            r.setPhone(Integer.parseInt(phoneres.getText()));
            r.setEmail(mailres.getText());
            sr.add(r);
            System.out.println("Réservation ajoutée avec succès");
        } catch (NumberFormatException e) {
            System.out.println("Le téléphone doit être un nombre valide.");
        }
    }

    private boolean allFieldsFilled() {
        return !nomres.getText().isEmpty() &&
                !prenomres.getText().isEmpty() &&
                !phoneres.getText().isEmpty() &&
                !mailres.getText().isEmpty();
    }

    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("\\d{8}");
    }

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void naviger(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/listreservation.fxml"));
            phoneres.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
