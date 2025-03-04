package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import tn.esprit.easytripdesktopapp.models.Reservation;
import tn.esprit.easytripdesktopapp.models.Ticket;
import tn.esprit.easytripdesktopapp.services.ServiceReservation;
import tn.esprit.easytripdesktopapp.utils.UserSession;
import tn.esprit.easytripdesktopapp.utils.WeatherAPI;

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
    private Label weatherLabel; // Nouveau Label pour la météo

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
                if (imagePath.startsWith("http://") || imagePath.startsWith("https://")) {
                    ticketImage.setImage(new Image(imagePath));
                } else {
                    if (!imagePath.startsWith("file:")) {
                        imagePath = "file:" + imagePath;
                    }
                    ticketImage.setImage(new Image(imagePath));
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
                ticketImage.setImage(new Image("file:src/main/resources/default_image.png"));
            }

            // Afficher les détails du ticket
            airlineLabel.setText("Compagnie : " + ticket.getAirline());
            departureLabel.setText("Départ : " + ticket.getDepartureCity() + " - " + ticket.getDepartureDate() + " " + ticket.getDepartureTime());
            arrivalLabel.setText("Arrivée : " + ticket.getArrivalCity() + " - " + ticket.getArrivalDate() + " " + ticket.getArrivalTime());
            priceLabel.setText("Prix : " + ticket.getPrice() + " €");
            ticketClassLabel.setText("Classe : " + ticket.getTicketClass());
            ticketTypeLabel.setText("Type : " + ticket.getTicketType());

            // Afficher la météo de la ville d'arrivée
            weatherLabel.setText("Météo : " + WeatherAPI.getWeather(ticket.getArrivalCity()));
        }
    }

    @FXML
    private void handleReserveButton() {
        System.out.println("Name:" + session.getUser().getName());
        try {
            Reservation r = new Reservation();
            r.setUser_id(session.getUser().getId());
            r.setNom(session.getUser().getName());
            r.setPrenom(session.getUser().getSurname());
            r.setPhone(Integer.parseInt(session.getUser().getPhone()));
            r.setEmail(session.getUser().getEmail());
            r.setTicket_id(ticket.getIdTicket());
            sr.addWithTicketOnly(r);
            System.out.println("Réservation ajoutée avec succès");
        } catch (NumberFormatException e) {
            System.out.println("Le téléphone doit être un nombre valide.");
        }
    }
}