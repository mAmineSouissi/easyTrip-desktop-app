package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.utils.UserSession;

public class DetailHotel {

    @FXML
    private VBox detailContainer;

    @FXML
    private ImageView hotelImage;

    @FXML
    private Label nameLabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label cityLabel;

    @FXML
    private Label ratingLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label typeRoomLabel;

    @FXML
    private Label numRoomLabel;

    @FXML
    private Label promotionLabel; // Nouveau Label pour afficher la promotion

    private Hotel hotel;

    UserSession session = UserSession.getInstance();

    // Méthode pour définir l'hôtel à afficher
    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        loadHotelDetails(); // Charger les détails de l'hôtel
    }

    // Méthode pour charger les détails de l'hôtel dans l'interface
    private void loadHotelDetails() {
        if (hotel != null) {
            hotelImage.setImage(new Image(hotel.getImage()));
            nameLabel.setText("Nom: " + hotel.getName());
            addressLabel.setText("Adresse: " + hotel.getAdresse());
            cityLabel.setText("Ville: " + hotel.getCity());
            ratingLabel.setText("Note: " + hotel.getRating());
            priceLabel.setText("Prix: " + hotel.getPrice() + " €");
            descriptionLabel.setText("Description: " + hotel.getDescription());
            typeRoomLabel.setText("Type de chambre: " + hotel.getTypeRoom());
            numRoomLabel.setText("Nombre de chambres: " + hotel.getNumRoom());

            // Afficher la promotion si elle existe
            if (hotel.getPromotion() != null) {
                promotionLabel.setText("Promotion: " + hotel.getPromotion().getDiscount_percentage() + "% de réduction");
            } else {
                promotionLabel.setText("Promotion: Aucune promotion");
            }
        }
    }

    @FXML
    private void handleReserveButton() {
        int userID=session.getUser().getId();
        if (hotel != null) {
            int Id = hotel.getId(); // Récupérer l'ID du ticket
            System.out.println("Offer Hotel réservé avec l'ID : " + Id);

            // Vous pouvez également afficher une boîte de dialogue ou effectuer une autre action ici
            // Par exemple, ouvrir une nouvelle fenêtre pour confirmer la réservation
        } else {
            System.out.println("Aucun Hotel sélectionné.");
        }
    }
}