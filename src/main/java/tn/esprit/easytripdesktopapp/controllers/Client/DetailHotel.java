package tn.esprit.easytripdesktopapp.controllers.Client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import tn.esprit.easytripdesktopapp.models.Hotel;
import tn.esprit.easytripdesktopapp.utils.UserSession;

import java.io.IOException;

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


    public void handleFeedbackButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Client/FeedbackUser.fxml"));
            Parent root = loader.load();

            FeedbackUser detailController = loader.getController();
            detailController.setHotel(hotel);

            Stage stage = new Stage();
            stage.setTitle("Détails de l'hôtel");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des détails de l'hôtel.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void handleReserveButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/esprit/easytripdesktopapp/FXML/Reservation/addreservation.fxml"));
            Parent root = loader.load();
            Addreservation detailController = loader.getController();
            detailController.setHotel(hotel);
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Reservation Screen");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}