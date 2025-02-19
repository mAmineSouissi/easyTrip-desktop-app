package tn.esprit.easytripdesktopapp.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;

import java.io.IOException;

public class Updatereservation {
    @FXML
    private TextField mailres;

    @FXML
    private TextField nomres;

    @FXML
    private TextField phoneres;

    @FXML
    private TextField prenomres;

    @FXML
    void modifier(ActionEvent event) {
        if (currentReservation != null) {
            currentReservation.setNom(nomres.getText());
            currentReservation.setPrenom(prenomres.getText());
            currentReservation.setEmail(mailres.getText());
            currentReservation.setPhone(Integer.parseInt(phoneres.getText()));
            ServiceReservation serviceReservation = new ServiceReservation();
            serviceReservation.update(currentReservation);
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/listreservation.fxml"));
                mailres.getScene().setRoot(root);
            } catch (IOException e) {
                System.out.println("Erreur lors du retour à la liste : " + e.getMessage());
            }
        }
    }

    private Reservation currentReservation;

    public void initData(Reservation reservation) {
        this.currentReservation = reservation;
        if (reservation != null) {
            mailres.setText(reservation.getEmail());
            nomres.setText(reservation.getNom());
            phoneres.setText(String.valueOf(reservation.getPhone()));
            prenomres.setText(reservation.getPrenom());
        }
    }


}
