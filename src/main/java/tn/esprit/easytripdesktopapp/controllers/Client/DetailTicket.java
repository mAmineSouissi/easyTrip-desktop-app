package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;

public class DetailTicket {

    @FXML
    private VBox detailContainer;

    @FXML
    private ImageView ticketImage;

    @FXML
    private Label airlineLabel;

    @FXML
    private Label departureLabel;

    @FXML
    private Label arrivalLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label ticketClassLabel;

    @FXML
    private Label ticketTypeLabel;

    @FXML
    private Button reserveButton; // Bouton Réserver

    private Ticket ticket;

    UserSession session = UserSession.getInstance();

    private ServiceReservation sr = new ServiceReservation();

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
        loadTicketDetails();
    }

    private void loadTicketDetails() {
        if (ticket != null) {
            String imagePath = ticket.getCityImage();

            try {
                // Vérifier si le chemin est déjà une URL valide
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    // Charger l'image depuis le web
                    ticketImage.setImage(new Image(imagePath));
                } else {
                    // Si le chemin est local, ajouter le préfixe "file:"
                    if (!imagePath.startsWith("file:")) {
                        imagePath = "file:" + imagePath;
                    }
                    // Charger l'image locale
                    ticketImage.setImage(new Image(imagePath));
                }
            } catch (IllegalArgumentException e) {
                // En cas d'erreur, utiliser une image par défaut
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
                ticketImage.setImage(new Image("file:src/main/resources/default_image.png"));
            }

            // Afficher les autres détails du ticket
            airlineLabel.setText("Compagnie : " + ticket.getAirline());
            departureLabel.setText("Départ : " + ticket.getDepartureCity() + " - " + ticket.getDepartureDate() + " " + ticket.getDepartureTime());
            arrivalLabel.setText("Arrivée : " + ticket.getArrivalCity() + " - " + ticket.getArrivalDate() + " " + ticket.getArrivalTime());
            priceLabel.setText("Prix : " + ticket.getPrice() + " €");
            ticketClassLabel.setText("Classe : " + ticket.getTicketClass());
            ticketTypeLabel.setText("Type : " + ticket.getTicketType());
        }
    }

    // Gestionnaire d'événements pour le bouton Réserver
    @FXML
    private void handleReserveButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/addreservation.fxml"));
            Parent root = loader.load();
            Addreservation detailController = loader.getController();
            detailController.setTicket(ticket);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservation Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}