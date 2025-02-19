package tn.esprit.easytripdesktopapp.controllers;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListView;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;

import java.io.IOException;
import java.util.List;

public class Listreservation {

    private final ServiceReservation sr = new ServiceReservation();

    @FXML
    private ListView<String> listres; // Utilisation de String pour simplifier l'affichage

    @FXML
    void initialize() {
        List<Reservation> reservations = sr.getAll();
        //listres.getItems().addAll(reservationList);
        List<String> formattedReservations = reservations.stream()
                .map(r -> String.format("👤 %s %s | 📧 %s | 📞 %d | 📅 %s",
                        r.getNom(), r.getPrenom(), r.getEmail(), r.getPhone(), r.getOrdreDate()))
                .toList();
        listres.setItems(FXCollections.observableArrayList(formattedReservations));
    }

    @FXML
    void retour(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/addreservation.fxml"));
            listres.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


}
