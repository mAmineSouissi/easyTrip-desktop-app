package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import tn.esprit.easytripdesktopapp.models.Hotel;

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

    private Hotel hotel;

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
        }
    }
}