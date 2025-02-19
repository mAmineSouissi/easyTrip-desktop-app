package tn.esprit.easytripdesktopapp.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
