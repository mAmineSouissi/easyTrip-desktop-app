package tn.esprit.easytripdesktopapp.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;

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
            sr.add(r);  // Ajout de la réservation
            System.out.println("Réservation ajoutée avec succès");
        } catch (NumberFormatException e) {
            System.out.println("Le téléphone doit être un nombre valide.");
        }
    }
}
