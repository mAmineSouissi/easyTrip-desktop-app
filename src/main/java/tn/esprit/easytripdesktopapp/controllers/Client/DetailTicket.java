package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import tn.esprit.easytripdesktopapp.models.Ticket;

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
    private Label promotionLabel; // Nouveau Label pour afficher la promotion

    @FXML
    private Button reserveButton; // Bouton Réserver

    private Ticket ticket;

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

            // Calculer le prix réduit en fonction de la promotion
            float prixReduit = ticket.getPrice();
            if (ticket.getPromotion() != null) {
                float discount = ticket.getPromotion().getDiscount_percentage();
                prixReduit = ticket.getPrice() * (1 - discount / 100); // Appliquer la réduction
            }

            // Afficher les autres détails du ticket
            airlineLabel.setText("Compagnie : " + ticket.getAirline());
            departureLabel.setText("Départ : " + ticket.getDepartureCity() + " - " + ticket.getDepartureDate() + " " + ticket.getDepartureTime());
            arrivalLabel.setText("Arrivée : " + ticket.getArrivalCity() + " - " + ticket.getArrivalDate() + " " + ticket.getArrivalTime());
            priceLabel.setText("Prix : " + String.format("%.2f", prixReduit) + " €");
            ticketClassLabel.setText("Classe : " + ticket.getTicketClass());
            ticketTypeLabel.setText("Type : " + ticket.getTicketType());

            // Afficher la promotion si elle existe
            if (ticket.getPromotion() != null) {
                promotionLabel.setText("Promotion : " + ticket.getPromotion().getDiscount_percentage() + "% de réduction");
            } else {
                promotionLabel.setText("Promotion : Aucune promotion");
            }
        }
    }

    // Gestionnaire d'événements pour le bouton Réserver
    @FXML
    private void handleReserveButton() {
        if (ticket != null) {
            int ticketId = ticket.getIdTicket(); // Récupérer l'ID du ticket
            System.out.println("Ticket réservé avec l'ID : " + ticketId);

            // Vous pouvez également afficher une boîte de dialogue ou effectuer une autre action ici
            // Par exemple, ouvrir une nouvelle fenêtre pour confirmer la réservation
        } else {
            System.out.println("Aucun ticket sélectionné.");
        }
    }
}