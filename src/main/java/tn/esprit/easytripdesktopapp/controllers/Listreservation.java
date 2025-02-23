package tn.esprit.easytripdesktopapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;

import java.io.IOException;
import java.util.List;

public class Listreservation {

    private final ServiceReservation sr = new ServiceReservation();

    @FXML
    private GridPane listres;

    @FXML
    void initialize() {
        loadReservations();
    }

    private void loadReservations() {
        List<Reservation> reservations = sr.getAll();
        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(reservations);
        listres.getChildren().clear();

        int columns = 2;
        int row = 0;
        int col = 0;

        for (Reservation reservation : observableReservations) {
            BorderPane card = new BorderPane();
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; " +
                    "-fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");
            Label infoLabel = new Label(String.format("👤 %s %s \n📧 %s\n📞 %d \n📅 %s",
                    reservation.getNom(), reservation.getPrenom(), reservation.getEmail(),
                    reservation.getPhone(), reservation.getOrdreDate()));
            infoLabel.setStyle("-fx-font-size: 14px;");
            Button btnModifier = new Button("Modifier");
            Button btnSupprimer = new Button("Supprimer");
            btnModifier.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
            btnSupprimer.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
            btnModifier.setOnAction(event -> modifierReservation(reservation));
            btnSupprimer.setOnAction(event -> supprimerReservation(reservation));
            HBox buttonBox = new HBox(10, btnModifier, btnSupprimer);
            buttonBox.setStyle("-fx-padding: 10;");
            card.setCenter(infoLabel);
            card.setBottom(buttonBox);
            listres.add(card, col, row);

            col++;
            if (col == columns) {
                col = 0;
                row++;
            }
        }
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
        loadReservations();
        System.out.println("Réservation supprimée : " + reservation.getNom());
    }

    @FXML
    void retour() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/tn/esprit/easytripdesktopapp/addreservation.fxml"));
            listres.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }




}