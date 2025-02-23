package tn.esprit.easytripdesktopapp.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        col1.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getNom() + " " + cellData.getValue().getPrenom()));
        col2.setCellValueFactory(new PropertyValueFactory<>("places"));
      // col3.setCellValueFactory(new PropertyValueFactory<>("prix"));
        loadReservations1();
        loadReservations("");
    }

    private void loadReservations(String searchText) {
        List<Reservation> reservations = sr.getAll();
        ObservableList<Reservation> filteredReservations = FXCollections.observableArrayList();

        for (Reservation reservation : reservations) {
            if (reservation.getNom().toLowerCase().contains(searchText) ||
                    reservation.getPrenom().toLowerCase().contains(searchText)) {
                filteredReservations.add(reservation);
            }
        }

        listres.getChildren().clear();

        int columns = 3;
        int row = 0;
        int col = 0;
        for (Reservation reservation : filteredReservations) {
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


    private void loadReservations1() {
        List<Reservation> reservations = sr.getAll();
        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(reservations);
        table.setItems(observableReservations);
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
        loadReservations("");
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

    @FXML
    private TableView<Reservation> table;
    @FXML
    private TableColumn<Reservation, String> col1;
    @FXML
    private TableColumn<Reservation, Integer> col2;
    @FXML
    private TableColumn<Reservation, Double> col3;

    @FXML
    private Label totalp;

    @FXML
    private TextField searchField;
    @FXML
    void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            loadReservations("");
        } else {
            loadReservations(searchText);
        }
    }


}