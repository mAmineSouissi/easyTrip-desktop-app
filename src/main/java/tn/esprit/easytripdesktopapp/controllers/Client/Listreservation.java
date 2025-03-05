package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;
import tn.esprit.easytripdesktopapp.services.StripeService;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class Listreservation {

    private final ServiceReservation sr = new ServiceReservation();

    @FXML
    private GridPane listres;
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
    private Button btnPay;

    @FXML
    void initialize() {
        col1.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom() + " " + cellData.getValue().getPrenom()));
        col2.setCellValueFactory(new PropertyValueFactory<>("places"));
        col3.setCellValueFactory(cellData -> {
            double price = sr.getOfferPrice(cellData.getValue().getOfferId());
            return new SimpleObjectProperty<>(price);
        });
        List<Reservation> reservations = sr.getAll();
        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(reservations);
        table.setItems(observableReservations);
        loadReservations("");
        calculateTotalPrice();
        table.getItems().addListener((ListChangeListener<Reservation>) change -> calculateTotalPrice());
    }

    private void calculateTotalPrice() {
        double totalPrice = 0.0;
        for (Reservation reservation : table.getItems()) {
            double price = sr.getOfferPrice(reservation.getOfferId());
            totalPrice += reservation.getPlaces() * price;
        }
        totalp.setText(totalPrice + " ");
    }

    private void loadReservations(String searchText) {
        List<Reservation> reservations = sr.getAll();
        ObservableList<Reservation> filteredReservations = FXCollections.observableArrayList();

        for (Reservation reservation : reservations) {
            if (reservation.getNom().toLowerCase().contains(searchText) || reservation.getPrenom().toLowerCase().contains(searchText)) {
                filteredReservations.add(reservation);
            }
        }
        listres.getChildren().clear();
        int columns = 2;
        int row = 0;
        int col = 0;
        for (Reservation reservation : filteredReservations) {
            BorderPane card = new BorderPane();
            card.setStyle("-fx-background-color: #ffffff; -fx-border-color: #cccccc; -fx-border-radius: 10; " + "-fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 5);");

            Label infoLabel = new Label(String.format("👤 %s %s \n📧 %s\n📞 %d \n📅 %s", reservation.getNom(), reservation.getPrenom(), reservation.getEmail(), reservation.getPhone(), reservation.getOrderDate()));
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/updatereservation.fxml"));
            Parent root = loader.load();
            Updatereservation controller = loader.getController();
            controller.initData(reservation);
            listres.getScene().setRoot(root);
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de la page de modification : " + e.getMessage());
        }
    }

    private void supprimerReservation(Reservation reservation) {
        sr.delete(reservation.getId());
        loadReservations("");
        System.out.println("Réservation supprimée : " + reservation.getNom());
    }

    @FXML
    void retour(ActionEvent actionEvent) {
        Stage stage;
        try {
            ResourceBundle resourcesBundle = ResourceBundle.getBundle("tn.esprit.easytripdesktopapp.i18n.messages", Locale.getDefault());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/Dashboard.fxml"), resourcesBundle);
            Parent root = loader.load();
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement de l'interface d'accueil.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            loadReservations("");
        } else {
            loadReservations(searchText);
        }
    }

    @FXML
    public void handlePayment(ActionEvent event) {
        StripeService stripeService = new StripeService();
        double totalAmount = extractTotalAmount();
        String paymentUrl = stripeService.createCheckoutSession(totalAmount, "eur");

        if (paymentUrl != null) {
            openWebPage(paymentUrl);
        } else {
            showError("Erreur lors de la création de la session de paiement.");
        }
    }

    private void openWebPage(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
            showError("Impossible d'ouvrir la page de paiement.");
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private double extractTotalAmount() {
        try {
            String text = totalp.getText().replace(" DT", "").trim();
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            showError("Erreur : Impossible de récupérer le montant total.");
            return 0.0;
        }
    }


}