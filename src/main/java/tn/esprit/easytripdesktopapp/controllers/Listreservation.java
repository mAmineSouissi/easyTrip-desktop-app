package tn.esprit.easytripdesktopapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;

import java.io.IOException;
import java.util.List;

public class Listreservation {

    private final ServiceReservation sr = new ServiceReservation();

    @FXML
    private ListView<Reservation> listres;

    @FXML
    void initialize() {
        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = sr.getAll();
        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(reservations);
        listres.setItems(observableReservations);

        listres.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Reservation reservation, boolean empty) {
                super.updateItem(reservation, empty);

                if (empty || reservation == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    Button btnModifier = new Button("Modifier");
                    Button btnSupprimer = new Button("Supprimer");

                    btnModifier.setOnAction(event -> modifierReservation(reservation));
                    btnSupprimer.setOnAction(event -> supprimerReservation(reservation));

                    hbox.getChildren().addAll(
                            new javafx.scene.control.Label(String.format("👤 %s %s | 📧 %s | 📞 %d | 📅 %s",
                                    reservation.getNom(), reservation.getPrenom(), reservation.getEmail(),
                                    reservation.getPhone(), reservation.getOrdreDate())),
                            btnModifier, btnSupprimer
                    );
                    setGraphic(hbox);
                }
            }
        });
    }

    private void modifierReservation(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/updatereservation.fxml"));
            Parent root = loader.load();
            Updatereservation controller = loader.getController();
            controller.initData(reservation);
            listres.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page de modification : " + e.getMessage());
        }
    }

    private void supprimerReservation(Reservation reservation) {
        sr.delete(reservation.getIdReservation());
        listres.getItems().remove(reservation);
        System.out.println("Réservation supprimée : " + reservation.getNom());
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
